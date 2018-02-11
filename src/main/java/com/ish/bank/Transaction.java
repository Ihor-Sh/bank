package com.ish.bank;

import javax.persistence.*;

/**
 * Created by igor on 30.10.2017.
 */
@Entity
@Table
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_from")
    private Account accountFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_to")
    private Account accountTo;

    @Column(nullable = false)
    private int currency;

    @Column(nullable = false)
    private double amount;

    public Transaction () {};

    public Transaction (Account accountFrom, Account accountTo, int currency, double amount) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.currency = currency;
        this.amount = amount;
    }

    public Account getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Account accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Account getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Account accountTo) {
        this.accountTo = accountTo;
    }

    public String toString () {
        return "Transaction #" + id + ": Account from - " + accountFrom + ", Account to - " + accountTo +
                ", currency - " + currency + ", amount - " + amount;
    }
}