package com.paymentservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentservice.models.PaymentRequestDTO;
import com.paymentservice.repository.MerchantRepository;
import com.paymentservice.utils.CachedHttpServletRequest;
import com.paymentservice.utils.ExceptionUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

@Component
@RequiredArgsConstructor
public class HMACAuthFilter extends OncePerRequestFilter {

    //1. collect headers and check for null values
    //2. time check
    //3. fetch secret
    //4. compute timestamp.body hmac
    //5. compare expected and signature

    private final Long TIME_DIFF_ALLOWED=30L*60*1000; //30 mins

    private final MerchantRepository merchantRepository;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String merchantId=request.getHeader("X-merchant-id");
        String timestamp=request.getHeader("X-timestamp");
        String signature=request.getHeader("X-signature");

        if(!(StringUtils.hasText(merchantId) && StringUtils.hasText(timestamp) &&
                StringUtils.hasText(signature))) {
            ExceptionUtil.throwBadRequestException("REQUIRED_HEADERS_MISSING", "Mandatory headers missing.");
            return;
        }

        Long parsedTimestampInMillis;
        try {
            parsedTimestampInMillis=Long.parseLong(timestamp)*1000;
        } catch (NumberFormatException e) {
            ExceptionUtil.throwBadRequestException("INVALID_TIMESTAMP", "Invalid value in X-timestamp header.");
            return;
        }

        if((System.currentTimeMillis()-parsedTimestampInMillis)>TIME_DIFF_ALLOWED) {
            ExceptionUtil.throwBadRequestException("AUTHORIZATION_EXPIRED", "Authorization time expired.");
            return;
        }

        Long parsedMerchantId;
        try {
            parsedMerchantId=Long.valueOf(merchantId);
        } catch (NumberFormatException e) {
            ExceptionUtil.throwBadRequestException("INVALID_MERCHANT_ID", "Invalid value in X-merchant-id header.");
            return;
        }

        String secret=merchantRepository.findSecretKeyByIdAndIsActive(parsedMerchantId, Boolean.TRUE);

        CachedHttpServletRequest cachedHttpServletRequest=new CachedHttpServletRequest(request);
        byte[] cachedBody = cachedHttpServletRequest.getCachedBody();
        String body = new String(cachedBody, StandardCharsets.UTF_8);
        PaymentRequestDTO paymentRequestDTO = objectMapper.readValue(body, PaymentRequestDTO.class);
        String message = timestamp + "." + merchantId+"."+paymentRequestDTO.getAmountDetails().getAmount()+'.'+paymentRequestDTO.getAmountDetails().getCurrencyCode()+'.'+paymentRequestDTO.getReason();

        String expected = computeHmac(message, secret);

        if(!MessageDigest.isEqual(expected.getBytes(), signature.getBytes())) {
            ExceptionUtil.throwBadRequestException("AUTHORIZATION_FAILED", "Authorization failed.");
        }

        request.setAttribute("merchantId", merchantId);

        filterChain.doFilter(cachedHttpServletRequest, response);
    }

    private String computeHmac(String message, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            ExceptionUtil.throwInternalServerException(null, "HMAC computation failed");
        }
        return null;
    }
}
