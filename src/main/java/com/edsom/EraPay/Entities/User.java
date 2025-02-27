package com.edsom.EraPay.Entities;

import com.edsom.EraPay.Enums.UserStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name= "user")
public class User {
    @Id
    private String userId;
    private String name;
    private String email;
    private String pan;
    private String mobile;
    private String adhaar;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDate dob;
    private String walletAddress;
    private Double wallet;
    @ManyToOne
    @JoinColumn(name = "roleId")
    private Role role;

    public User() {
    }

    public User(String userId, String name, String email, String pan, String mobile, String adhaar, UserStatus status, LocalDateTime createdAt, LocalDate dob, String walletAddress, Double wallet, Role role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.pan = pan;
        this.mobile = mobile;
        this.adhaar = adhaar;
        this.status = status;
        this.createdAt = createdAt;
        this.dob = dob;
        this.walletAddress = walletAddress;
        this.wallet = wallet;
        this.role = role;
        this.password="null";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAdhaar() {
        return adhaar;
    }

    public void setAdhaar(String adhaar) {
        this.adhaar = adhaar;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public Double getWallet() {
        return wallet;
    }

    public void setWallet(Double wallet) {
        this.wallet = wallet;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", pan='" + pan + '\'' +
                ", mobile='" + mobile + '\'' +
                ", adhaar='" + adhaar + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", dob=" + dob +
                ", walletAddress='" + walletAddress + '\'' +
                ", wallet=" + wallet +
                ", role=" + role +
                '}';
    }
}
