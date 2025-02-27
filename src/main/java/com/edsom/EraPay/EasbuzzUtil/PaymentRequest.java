package com.edsom.EraPay.EasbuzzUtil;

public class PaymentRequest {
    private String userid;
    private String txnid;
    private String amount;
    private String productinfo;
    private String surl;
    private String furl;
    private String clientId;
    private static String show_payment_mode="NB,CC,DC";

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public static String getPaymentMode(){
        return show_payment_mode;
    }


    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductinfo() {
        return productinfo;
    }

    public void setProductinfo(String productinfo) {
        this.productinfo = productinfo;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }

    public String getFurl() {
        return furl;
    }

    public void setFurl(String furl) {
        this.furl = furl;
    }


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "userid='" + userid + '\'' +
                ", txnid='" + txnid + '\'' +
                ", amount='" + amount + '\'' +
                ", productinfo='" + productinfo + '\'' +
                ", surl='" + surl + '\'' +
                ", furl='" + furl + '\'' +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}
