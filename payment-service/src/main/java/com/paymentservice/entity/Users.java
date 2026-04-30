package com.paymentservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Table(name="users")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="address", nullable = false)
    private String address;

    @Column(name="phone_number", nullable = false)
    private String phoneNumber;

    @Column(name="country_code", nullable = false)
    private String countryCode;

    @Column(name="created_at", nullable = false)
    private Date createdAt;

    @Column(name="is_active", nullable = false)
    private Boolean isActive;
}
