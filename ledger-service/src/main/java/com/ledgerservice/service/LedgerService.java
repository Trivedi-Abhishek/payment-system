package com.ledgerservice.service;

import com.paymentservice.enums.CurrencyCodeEnum;
import com.ledgerservice.entity.LedgerEntry;
import com.ledgerservice.enums.TxnTypeEnum;
import com.ledgerservice.models.LedgerEntryAmountProjections;
import com.ledgerservice.repository.LedgerEntryRepository;
import com.paymentservice.models.FraudCheckResultEvent;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LedgerService {

    private final LedgerEntryRepository ledgerEntryRepository;

    @Transactional
    public void createLedgerEntries(FraudCheckResultEvent fraudCheckResultEvent) {

        Date currentDate=new Date();
        Long paymentId = fraudCheckResultEvent.getPaymentId();
        Long merchantId = fraudCheckResultEvent.getPaymentInitiatedEvent().getMerchantId();
        Long userId = fraudCheckResultEvent.getPaymentInitiatedEvent().getUserId();
        Long amount = fraudCheckResultEvent.getPaymentInitiatedEvent().getAmountDetails().getAmount();
        CurrencyCodeEnum currencyCode = fraudCheckResultEvent.getPaymentInitiatedEvent().getAmountDetails().getCurrencyCode();

        LedgerEntry creditLedgerEntry = LedgerEntry.builder().paymentId(paymentId)
                .accountId(merchantId).txnType(TxnTypeEnum.CREDIT).amount(amount)
                .currencyCode(currencyCode).createdAt(currentDate).build();

        LedgerEntry debitLedgerEntry = LedgerEntry.builder().paymentId(paymentId)
                .accountId(userId).txnType(TxnTypeEnum.DEBIT).amount(amount)
                .currencyCode(currencyCode).createdAt(currentDate).build();

        ledgerEntryRepository.saveAll(List.of(creditLedgerEntry, debitLedgerEntry));

        assertLedgerEntries(paymentId);

    }

    private void assertLedgerEntries(Long paymentId) {
        List<LedgerEntryAmountProjections> ledgerEntryAmountProjectionsList = ledgerEntryRepository.findByPaymentId(paymentId);

        if(!CollectionUtils.isEmpty(ledgerEntryAmountProjectionsList) && ledgerEntryAmountProjectionsList.size()==2) {
            long sum=0L;
            for(LedgerEntryAmountProjections ledgerEntryAmountProjections: ledgerEntryAmountProjectionsList) {
                sum+=TxnTypeEnum.CREDIT.equals(ledgerEntryAmountProjections.getTxnType()) ? ledgerEntryAmountProjections.getAmount() : -ledgerEntryAmountProjections.getAmount();
            }
            if(sum!=0L) {
                log.error("LEDGER IMBALANCE DETECTED paymentId={} balance={}", paymentId, sum);
                throw new RuntimeException("LEDGER IMBALANCE DETECTED paymentId= "+paymentId+" balance= "+ sum);
            }
            return;
        }
        throw new EntityNotFoundException("ledger entry not found");

    }
}
