package com.example.luxuryhotel.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class ApartmentStatus {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "apartment_id")
    private Apartment apartmentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private  User userId;

    private LocalDate arrivalDay;
    private LocalDate endDay;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime payTimeLimit;

    public ApartmentStatus() {
    }

    public ApartmentStatus(Apartment apartmentId, User userId, LocalDate arrivalDay, LocalDate endDay, LocalDateTime payTimeLimit,Status status) {
        this.apartmentId = apartmentId;
        this.userId = userId;
        this.arrivalDay = arrivalDay;
        this.endDay = endDay;
        this.payTimeLimit = payTimeLimit;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Apartment getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Apartment apartmentId) {
        this.apartmentId = apartmentId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public LocalDate getArrivalDay() {
        return arrivalDay;
    }

    public void setArrivalDay(LocalDate arrivalDay) {
        this.arrivalDay = arrivalDay;
    }

    public LocalDate getEndDay() {
        return endDay;
    }

    public void setEndDay(LocalDate endDay) {
        this.endDay = endDay;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getPayTimeLimit() {
        return payTimeLimit;
    }

    public void setPayTimeLimit(LocalDateTime payTimeLimit) {
        this.payTimeLimit = payTimeLimit;
    }

    public Status isStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getTimeLimit() {
        return payTimeLimit;
    }

    public void setTimeLimit(LocalDateTime timeLimit) {
        this.payTimeLimit = timeLimit;
    }
}
