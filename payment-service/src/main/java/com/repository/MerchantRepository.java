package com.repository;

import com.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    @Query("Select m.secretKey from Merchant m where m.id=:parsedMerchantId and m.isActive=:isActive")
    String findSecretKeyByIdAndIsActive(Long parsedMerchantId, Boolean isActive);
}
