package com.weshare.model;

import com.weshare.util.MoneyConverter;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "bills", schema = "weshare")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner", nullable = false)
    private User owner;

    private String description;

    @Convert(converter = MoneyConverter.class)
    private double amount;

    private int category;

    private LocalDate date;

    @Convert(converter = MoneyConverter.class)
    private double ownAmount;

    private boolean isPaid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
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

    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }
}