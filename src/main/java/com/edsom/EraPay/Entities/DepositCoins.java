package com.edsom.EraPay.Entities;

import com.edsom.EraPay.Enums.DepositStaus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "depositCoins")
public class DepositCoins {

    @Id
    private String id;

    private Double coins;
    private LocalDateTime depositTime;
    private String depositedFromWallet;
    @Enumerated(EnumType.STRING)
    private DepositStaus status;
    private String url;
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public DepositCoins() {
    }

    public DepositCoins(String id, Double coins, LocalDateTime depositTime, String depositedFromWallet, DepositStaus status, String url, User user) {
        this.id = id;
        this.coins = coins;
        this.depositTime = depositTime;
        this.depositedFromWallet = depositedFromWallet;
        this.status = status;
        this.url = url;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getCoins() {
        return coins;
    }

    public void setCoins(Double coins) {
        this.coins = coins;
    }

    public LocalDateTime getDepositTime() {
        return depositTime;
    }

    public void setDepositTime(LocalDateTime depositTime) {
        this.depositTime = depositTime;
    }

    public String getDepositedFromWallet() {
        return depositedFromWallet;
    }

    public void setDepositedFromWallet(String depositedFromWallet) {
        this.depositedFromWallet = depositedFromWallet;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DepositStaus getStatus() {
        return status;
    }

    public void setStatus(DepositStaus status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
