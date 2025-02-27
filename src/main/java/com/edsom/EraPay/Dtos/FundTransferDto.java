package com.edsom.EraPay.Dtos;

public class FundTransferDto {

    String recieverMobile;
    Double amount;
    String otp;

    public String getRecieverMobile() {
        return recieverMobile;
    }

    public void setRecieverMobile(String recieverMobile) {
        this.recieverMobile = recieverMobile;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    @Override
    public String toString() {
        return "FundTransferDto{" +
                "recieverMobile='" + recieverMobile + '\'' +
                ", amount=" + amount +
                '}';
    }
}
