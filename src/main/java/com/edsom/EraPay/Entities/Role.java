package com.edsom.EraPay.Entities;

import com.edsom.EraPay.Enums.UserType;
import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Enumerated(EnumType.STRING)
    UserType userType;

    public Role() {

    }

    public Role(int id, UserType userType) {
        this.id = id;
        this.userType = userType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }


    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", userType=" + userType +
                '}';
    }
}
