package com.example.luxuryhotel.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private  User userId;



    private Integer beds;
    private LocalDate arrivalDay;
    private LocalDate endDay;
    @Enumerated(EnumType.STRING)
    private Clazz clazz;
    private String text;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    public ApartmentStatus answerStatus;

    public ApartmentStatus getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(ApartmentStatus answerStatus) {
        this.answerStatus = answerStatus;
    }

    public Request() {
    }

    public Request(User userId, Integer beds, LocalDate arrivalDay, LocalDate endDay, Clazz clazz, String text) {
        this.userId = userId;
        this.beds = beds;
        this.arrivalDay = arrivalDay;
        this.endDay = endDay;
        this.clazz = clazz;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public LocalDate getArrivalDay() {
        return arrivalDay;
    }

    public void setArrivalDay(LocalDate arrivalDay) {
        this.arrivalDay = arrivalDay;
    }

    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz clazz) {
        this.clazz = clazz;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getEndDay() {
        return endDay;
    }

    public void setEndDay(LocalDate endDay) {
        this.endDay = endDay;
    }

}
