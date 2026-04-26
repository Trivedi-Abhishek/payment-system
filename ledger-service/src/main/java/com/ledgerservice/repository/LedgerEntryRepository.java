package com.ledgerservice.repository;

import com.ledgerservice.entity.LedgerEntry;
import com.ledgerservice.models.LedgerEntryAmountProjections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {

    @Query("Select le.amount as amount, le.txnType as txnType from LedgerEntry le where le.paymentId=:paymentId")
    List<LedgerEntryAmountProjections> findByPaymentId(Long paymentId);
}
