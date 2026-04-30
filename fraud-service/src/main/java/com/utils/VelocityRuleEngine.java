package com.utils;

import com.enums.FraudCheckEnum;
import com.paymentservice.models.CountAmountProjections;
import com.paymentservice.models.PaymentInitiatedEvent;
import com.models.RuleResult;
import com.paymentservice.repository.PaymentsRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class VelocityRuleEngine {

    //for past 1 minute
    private Integer MAXIMUM_ALLOWED_TRANSACTIONS=5;
    private Integer FRAUD_TRANSACTION_COUNT=10;
    private Integer MAXIMUM_ALLOWED_AMOUNT=500000;
    private Integer FRAUD_TRANSACTION_AMOUNT=1000000;

    private final PaymentsRepository paymentsRepository;

    public RuleResult getRuleResult(PaymentInitiatedEvent paymentInitiatedEvent) {

        RuleResult ruleResult=new RuleResult();

        Long merchantId = paymentInitiatedEvent.getMerchantId();
        LocalDateTime fromTime = LocalDateTime.now().minusMinutes(5L);

        // collect the count of txns and txn amount for the given merchant over the past 5 minutes and check whether it is fraud/suspicious/valid
        CountAmountProjections countAmountProjections = paymentsRepository.findCountAndAmountSumByMerchantId(merchantId, fromTime);
        Long count = countAmountProjections.getCount();
        Long amount = countAmountProjections.getAmount();

        computeFlags(count, amount, ruleResult);

        return ruleResult;
    }

    private void computeFlags(Long count, Long amount, RuleResult ruleResult) {
        List<String> flagList=new ArrayList<>();
        boolean isAmountSuspicious=false;
        boolean isCountSuspicious=false;
        boolean isAmountFraud=false;
        boolean isCountFraud=false;

        if(count >MAXIMUM_ALLOWED_TRANSACTIONS && count <FRAUD_TRANSACTION_COUNT) {
            isCountSuspicious=true;
            flagList.add("Suspicious number of transactions: "+ count + " (exceeds MAXIMUM_ALLOWED_TRANSACTIONS: "+MAXIMUM_ALLOWED_TRANSACTIONS+" value) of the given merchant.");
        }
        else if(count >=FRAUD_TRANSACTION_COUNT) {
            isCountFraud=true;
            flagList.add("Number of transactions: "+ count + " (exceeds FRAUD_TRANSACTION_COUNT: "+FRAUD_TRANSACTION_COUNT+" value) of the given merchant.");
        }

        if(amount >MAXIMUM_ALLOWED_AMOUNT && amount <FRAUD_TRANSACTION_AMOUNT) {
            isAmountSuspicious=true;
            flagList.add("Total amount of transactions: "+ amount +" seems too be suspicious for the given merchant as it exceeds MAXIMUM_ALLOWED_AMOUNT: "+MAXIMUM_ALLOWED_AMOUNT);
        }
        else if(amount >=FRAUD_TRANSACTION_AMOUNT) {
            isAmountFraud=true;
            flagList.add("Total amount of transactions : "+ amount +" exceeds the fraud check amount for the given merchant as it exceeds FRAUD_TRANSACTION_AMOUNT: "+FRAUD_TRANSACTION_AMOUNT);
        }

        if(isCountFraud && isAmountFraud) {
            ruleResult.setFraudCheck(FraudCheckEnum.FRAUD);
        }
        else if(isAmountSuspicious || isCountSuspicious) {
            ruleResult.setFraudCheck(FraudCheckEnum.SUSPICIOUS);
        }
        else {
            ruleResult.setFraudCheck(FraudCheckEnum.VALID);
            flagList.add("No red flag for the given merchant");
        }

        ruleResult.setFlagList(flagList);
    }
}

// 1. define velocity rule engine parameters
// 2. function accepts PaymentInitiated event and check whether txn is fraud/suspicious/valid and returns result in the form RuleResults
// 3. FraudDetectionService to further test if  using llm
