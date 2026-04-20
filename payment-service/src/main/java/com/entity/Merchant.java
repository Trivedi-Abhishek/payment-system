package com.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="merchant")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Column(name="secret_key", nullable=false)
    private String secretKey;

    @Column(name="is_active", nullable=false)
    private Boolean isActive=Boolean.TRUE;
}
