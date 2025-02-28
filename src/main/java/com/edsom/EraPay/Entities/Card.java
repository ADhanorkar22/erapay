package com.edsom.EraPay.Entities;

import com.edsom.EraPay.Enums.CardNetwork;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String cardNumber;
    String cvv;
    String name;
    LocalDate start;
    LocalDate end;
    CardNetwork cardNetwork;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Card() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CardNetwork getCardNetwork() {
        return cardNetwork;
    }

    public void setCardNetwork(CardNetwork cardNetwork) {
        this.cardNetwork = cardNetwork;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", cvv='" + cvv + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", cardNetwork='" + cardNetwork + '\'' +
                ", user=" + user +
                '}';
    }
}
