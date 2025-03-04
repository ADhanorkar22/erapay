package com.edsom.EraPay.Dtos;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class DepositCoinsDto {

    @NotNull(message = "Coins amount cannot be null")
    @Positive(message = "Coins amount must be positive")
    @DecimalMin(value = "0.01", message = "Minimum deposit is 0.01 coins")
    private Double coins;

    @NotNull(message = "Deposit timestamp cannot be null")
    @PastOrPresent(message = "Deposit time cannot be in the future")
    private LocalDateTime depositTime;

    @NotBlank(message = "Source wallet address cannot be empty")
//    @Size(min = 26, max = 35, message = "Invalid wallet address format")
    private String depositedFromWallet;

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
}
