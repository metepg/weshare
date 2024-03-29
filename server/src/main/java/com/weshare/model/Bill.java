package com.weshare.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "bill", schema = "weshare")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String owner;
    private String description;
    private double amount;
    private int category;
    private LocalDate date;
    private double ownAmount;
    private boolean isPaid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getOwnAmount() {
        return ownAmount;
    }

    public void setOwnAmount(double ownAmount) {
        this.ownAmount = ownAmount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

}
