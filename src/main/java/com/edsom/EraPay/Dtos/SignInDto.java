package com.edsom.EraPay.Dtos;

public class SignInDto {

    private String empId;
    private String password;

    public SignInDto() {
    }

    public SignInDto(String empId, String password) {
        this.empId = empId;
        this.password = password;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
