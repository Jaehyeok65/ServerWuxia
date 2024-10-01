package net.skhu.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "rate")
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String userEmail;
    String title;
    double rate;


    public Rate(String userEmail, String title, double rate) {
    	this.userEmail = userEmail;
    	this.title = title;
    	this.rate = rate;
    }

    public Rate() {

    }
}