package com.paymentservice.repository;

import com.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PaymentsRepository extends JpaRepository<Payment, Long> {
    @Query("""
    SELECT COUNT(p), COALESCE(SUM(p.amount), 0)
    FROM Payment p
    WHERE p.merchantId = :merchantId
      AND p.createdAt >= :fromTime
""")
    Object[] findCountAndAmountSumByMerchantId(
            @Param("merchantId") Long merchantId,
            @Param("fromTime") LocalDateTime fromTime
    );
}
