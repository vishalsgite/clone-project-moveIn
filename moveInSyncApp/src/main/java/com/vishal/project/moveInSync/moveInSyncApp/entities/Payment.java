package com.vishal.project.moveInSync.moveInSyncApp.entities;


import com.vishal.project.moveInSync.moveInSyncApp.entities.enums.PaymentMethod;
import com.vishal.project.moveInSync.moveInSyncApp.entities.enums.PaymentStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @OneToOne(fetch = FetchType.LAZY)
    private Ride ride;


    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @CreationTimestamp
    private LocalDateTime paymentTime;

}
