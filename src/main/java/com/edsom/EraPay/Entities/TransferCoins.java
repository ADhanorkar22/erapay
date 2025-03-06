package com.edsom.EraPay.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transferred_or_deposited_coins")
public class TransferCoins {

    @Id
    String id;
    double amount;
    double coins;
    LocalDateTime timestamp;
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public TransferCoins(String id, double amount, double coins,  User user) {
        this.id = id;
        this.amount = amount;
        this.coins = coins;
        this.timestamp = LocalDateTime.now();
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCoins() {
        return coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
