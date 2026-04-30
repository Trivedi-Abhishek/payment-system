package com.paymentservice.repository;

import com.paymentservice.entity.Payment;
import com.paymentservice.models.CountAmountProjections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PaymentsRepository extends JpaRepository<Payment, Long> {
    @Query("""
    SELECT COUNT(p) as count, COALESCE(SUM(p.amount), 0) as amount
    FROM Payment p
    WHERE p.merchantId = :merchantId
      AND p.createdAt >= :fromTime
""")
    CountAmountProjections findCountAndAmountSumByMerchantId(
            Long merchantId,
            LocalDateTime fromTime
    );

    boolean existsByMerchantIdAndIdempotentKey(Long merchantId, String idempotentKey);
}
