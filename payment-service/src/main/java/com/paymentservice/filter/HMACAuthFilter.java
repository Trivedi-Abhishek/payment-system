package com.paymentservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentservice.models.PaymentRequestDTO;
import com.paymentservice.repository.MerchantRepository;
import com.paymentservice.utils.CachedHttpServletRequest;
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
            response.sendError(404, "Header missing");
            return;
        }

        Long parsedTimestampInMillis;
        try {
            parsedTimestampInMillis=Long.parseLong(timestamp)*1000;
        } catch (NumberFormatException e) {
            response.sendError(404, "Invalid timestamp provided.");
            return;
        }

        if((System.currentTimeMillis()-parsedTimestampInMillis)>TIME_DIFF_ALLOWED) {
            response.sendError(404, "Authorization time expired.");
            return;
        }

        Long parsedMerchantId;
        try {
            parsedMerchantId=Long.valueOf(merchantId);
        } catch (NumberFormatException e) {
            response.sendError(404, "Invalid merchant_id provided.");
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
            response.sendError(403, "Authorization failed.");
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
            throw new RuntimeException("HMAC computation failed", e);
        }
    }
}
