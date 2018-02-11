package com.ish.bank;

import javax.persistence.*;

/**
 * Created by igor on 30.10.2017.
 */
@Entity
@Table
public class Courses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private double usd;

    @Column
    private double eur;

    public Courses() {
        this.usd = 26.54;
        this.eur = 31.36;
    }

    public Courses(double usd, double eur) {
        this.usd = usd;
        this.eur = eur;
    }

    public double getUsd() {
        return usd;
    }

    public double getEur() {
        return eur;
    }

    public void setUsd(double usd) {
        this.usd = usd;
    }

    public void setEur(double eur) {
        this.eur = eur;
    }
}
