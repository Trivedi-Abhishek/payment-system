package com.service;

import com.enums.FraudCheckEnum;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentservice.models.FraudCheckResultEvent;
import com.paymentservice.models.PaymentInitiatedEvent;
import com.models.RuleResult;
import com.utils.VelocityRuleEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FraudDetectionService {

    @Value("classpath:/promptTemplates/paymentDetailsPromptTemplate.st")
    Resource paymentDetailsPromptTemplate;

    private final VelocityRuleEngine velocityRuleEngine;
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public FraudCheckResultEvent getFraudCheckResultEvent(PaymentInitiatedEvent paymentInitiatedEvent) {

        // 1. check with VelocityRuleEngine if SUSPICIOUS then send request to LLM
        RuleResult ruleResult = velocityRuleEngine.getRuleResult(paymentInitiatedEvent);

        FraudCheckResultEvent fraudCheckResultEvent=new FraudCheckResultEvent();
        fraudCheckResultEvent.setPaymentId(paymentInitiatedEvent.getPaymentId());
        fraudCheckResultEvent.setMerchantId(paymentInitiatedEvent.getMerchantId());
        fraudCheckResultEvent.setUserId(paymentInitiatedEvent.getUserId());
        fraudCheckResultEvent.setAmountDetails(paymentInitiatedEvent.getAmountDetails());

        // ask llm
        if(FraudCheckEnum.SUSPICIOUS.equals(ruleResult.getFraudCheck())) {

            try {
                String content = chatClient.prompt().user(promptUserSpec -> {
                    promptUserSpec.text(paymentDetailsPromptTemplate).param("merchantId", paymentInitiatedEvent.getMerchantId())
                            .param("flags", String.join(", ", ruleResult.getFlagList()));
                }).call().content();
                if(Objects.nonNull(content)) {
                    String cleaned = content
                            .replace("```json", "")
                            .replace("```", "")
                            .trim();

                    // Parse the JSON
                    JsonNode node = objectMapper.readTree(cleaned);
                    String decision   = node.get("decision").asText();
                    double confidence = node.get("confidence").asDouble();
                    String reason = node.get("reason").asText();

                    fraudCheckResultEvent.setDecision(decision);
                    fraudCheckResultEvent.setReason(reason+" With confidence: "+confidence);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                fraudCheckResultEvent.setDecision(FraudCheckEnum.VALID.name());
                fraudCheckResultEvent.setReason("Auto approved, error while receiving request from llm.");
            }

        }
        else {
            fraudCheckResultEvent.setDecision(ruleResult.getFraudCheck().name());
            fraudCheckResultEvent.setReason(String.join(", ", ruleResult.getFlagList()));
        }

        return fraudCheckResultEvent;
    }
}
