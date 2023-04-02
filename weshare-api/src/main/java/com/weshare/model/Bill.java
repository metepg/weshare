package com.weshare.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("bills")
public class Bill {

    @Id
    private String id;
    private String owner;
    private double amount;
    private String description;
    private String category;
    private Date date;
    private double ownAmount;

    @Indexed
    private boolean isPaid;

    public Bill(String owner, String description, double amount, String category, Date date, double ownAmount, boolean isPaid) {
        super();
        this.owner = owner;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.ownAmount = ownAmount;
        this.isPaid = isPaid;
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getOwnAmount() {
        return ownAmount;
    }

    public void setOwnAmount(double ownAmount) {
        this.ownAmount = ownAmount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

}
