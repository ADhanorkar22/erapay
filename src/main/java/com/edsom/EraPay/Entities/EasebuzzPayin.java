package com.edsom.EraPay.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="easebuzzpayin")
public class EasebuzzPayin {

    @Id
    @Column(name = "txnid", nullable = false)
    private String txnid;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "key_id")
    private String keyId;

    @Column(name = "mode")
    private String mode;

    @Column(name = "unmappedstatus")
    private String unmappedstatus;

    @Column(name = "cardCategory")
    private String cardCategory;

    @Column(name = "addedon")
    private LocalDateTime addedon;

    @Column(name = "PG_TYPE")
    private String pgType;

    @Column(name = "bank_ref_num")
    private String bankRefNum;

    @Column(name = "bankcode")
    private String bankcode;

    @Column(name = "error")
    private String error;

    @Column(name = "error_Message")
    private String errorMessage;

    @Column(name = "name_on_card")
    private String nameOnCard;

    @Column(name = "upi_va")
    private String upiVa;

    @Column(name = "cardnum")
    private String cardnum;

    @Column(name = "issuing_bank")
    private String issuingBank;

    @Column(name = "easepayid")
    private String easepayid;

    @Column(name = "amount")
    private String amount;

    @Column(name = "net_amount_debit")
    private String netAmountDebit;

    @Column(name = "cash_back_percentage")
    private String cashBackPercentage;

    @Column(name = "deduction_percentage")
    private String deductionPercentage;

    @Column(name = "productinfo")
    private String productinfo;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "status")
    private String status;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "auth_code")
    private String authCode;

    @Column(name="txn_settelment")
    private String settlement;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public EasebuzzPayin() {
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getUnmappedstatus() {
        return unmappedstatus;
    }

    public void setUnmappedstatus(String unmappedstatus) {
        this.unmappedstatus = unmappedstatus;
    }

    public String getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(String cardCategory) {
        this.cardCategory = cardCategory;
    }

    public LocalDateTime getAddedon() {
        return addedon;
    }

    public void setAddedon(LocalDateTime addedon) {
        this.addedon = addedon;
    }

    public String getPgType() {
        return pgType;
    }

    public void setPgType(String pgType) {
        this.pgType = pgType;
    }

    public String getBankRefNum() {
        return bankRefNum;
    }

    public void setBankRefNum(String bankRefNum) {
        this.bankRefNum = bankRefNum;
    }

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getUpiVa() {
        return upiVa;
    }

    public void setUpiVa(String upiVa) {
        this.upiVa = upiVa;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public void setIssuingBank(String issuingBank) {
        this.issuingBank = issuingBank;
    }

    public String getEasepayid() {
        return easepayid;
    }

    public void setEasepayid(String easepayid) {
        this.easepayid = easepayid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNetAmountDebit() {
        return netAmountDebit;
    }

    public void setNetAmountDebit(String netAmountDebit) {
        this.netAmountDebit = netAmountDebit;
    }

    public String getCashBackPercentage() {
        return cashBackPercentage;
    }

    public void setCashBackPercentage(String cashBackPercentage) {
        this.cashBackPercentage = cashBackPercentage;
    }

    public String getDeductionPercentage() {
        return deductionPercentage;
    }

    public void setDeductionPercentage(String deductionPercentage) {
        this.deductionPercentage = deductionPercentage;
    }

    public String getProductinfo() {
        return productinfo;
    }

    public void setProductinfo(String productinfo) {
        this.productinfo = productinfo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }
}
