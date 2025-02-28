package com.edsom.EraPay.Dtos;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class UserUpdateDto {

    @NotBlank(message = "Userid cannot be blank")
    String userid;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "PAN cannot be blank")
    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "Invalid PAN format")
    private String pan;

    @NotBlank(message = "Mobile number cannot be blank")
    @Pattern(regexp = "[6789]\\d{9}", message = "Invalid mobile number")
    private String mobile;

    @NotBlank(message = "Aadhaar number cannot be blank")
    @Pattern(regexp = "\\d{12}", message = "Aadhaar must be a 12-digit number")
    private String adhaar;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Wallet address cannot be blank")
    private String walletAddress;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdhaar() {
        return adhaar;
    }

    public void setAdhaar(String adhaar) {
        this.adhaar = adhaar;
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

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "UserUpdateDto{" +
                "userid='" + userid + '\'' +
                ", name='" + name + '\'' +
                ", adhaar='" + adhaar + '\'' +
                ", email='" + email + '\'' +
                ", pan='" + pan + '\'' +
                ", mobile='" + mobile + '\'' +
                ", walletAddress='" + walletAddress + '\'' +
                ", dob=" + dob +
                '}';
    }
}
