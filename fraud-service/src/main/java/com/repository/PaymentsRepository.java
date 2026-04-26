package com.repository;

import com.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PaymentsRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT count(*), COALESCE(SUM(amount, 0)) from Payment p where p.merchantId=:merchantId and p.createdAt>=:fromTime")
    Object[] findCountAndAmountSumByMerchantId(Long merchantId, LocalDateTime fromTime);
}
