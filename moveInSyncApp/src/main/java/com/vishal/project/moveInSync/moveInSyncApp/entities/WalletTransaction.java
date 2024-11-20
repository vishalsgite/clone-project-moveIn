package com.vishal.project.moveInSync.moveInSyncApp.entities;

import com.vishal.project.moveInSync.moveInSyncApp.entities.enums.TransactionMethod;
import com.vishal.project.moveInSync.moveInSyncApp.entities.enums.TransactionType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Double amount;

    private TransactionType transactionType;

    private TransactionMethod transactionMethod;

    @OneToOne
    private Ride ride;

    private String transactionId;

    @ManyToOne
    private Wallet wallet;

    @CreationTimestamp
    private LocalDateTime timeStamp;
}
