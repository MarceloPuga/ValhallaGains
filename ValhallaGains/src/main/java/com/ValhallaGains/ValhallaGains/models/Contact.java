package com.ValhallaGains.ValhallaGains.models;

import jakarta.persistence.*;

@Entity
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String comment;
    public String from;
    @OneToOne
    public Client client;

    public Contact(String comment, String from){
        this.comment = comment;
        this.from= from;

    }

    public Long getId() {
        return id;
    }



    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
