package com.unindra.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.unindra.model.util.PaymentMethod;
import com.unindra.model.util.PaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
    
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Double total;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private LocalDateTime transactionTime;

    private String referenceNumber;

    @OneToMany(mappedBy = "transaction")
    private List<Payment> payments;

}