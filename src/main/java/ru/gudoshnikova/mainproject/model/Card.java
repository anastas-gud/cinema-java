package ru.gudoshnikova.mainproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "image")
    private String image;

    @Column(name = "cashback")
    private int cashback;

    @Column(name = "sum_from")
    private Double sumFrom;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "card")
    private Set<User> users;
    public Card(){}

    public Card(int id, String image, int cashback, Double sumFrom, String status, Set<User> users) {
        this.id = id;
        this.image = image;
        this.cashback = cashback;
        this.sumFrom = sumFrom;
        this.status = status;
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCashback() {
        return cashback;
    }

    public void setCashback(int cashback) {
        this.cashback = cashback;
    }

    public Double getSumFrom() {
        return sumFrom;
    }

    public void setSumFrom(Double sumFrom) {
        this.sumFrom = sumFrom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", cashback=" + cashback +
                ", sumFrom=" + sumFrom +
                ", status='" + status + '\'' +
                '}';
    }
}
