package com.ish.bank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by igor on 30.10.2017.
 */
@Entity
@Table
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false)
    private int currency;

    @Column(nullable = false)
    private double amount;

    @OneToMany(mappedBy = "accountFrom", cascade = CascadeType.ALL)
    private List<Transaction> transactionsIn = new ArrayList<>();

    @OneToMany(mappedBy = "accountTo", cascade = CascadeType.ALL)
    private List<Transaction> transactionsOut = new ArrayList<>();

    public Account() {
    }

    public Account(int currency) {
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCurrency() {
        return currency;
    }

    public List<Transaction> getTransactionsIn() {
        return transactionsIn;
    }

    public void setTransactionsIn(Transaction transactions) {
        transactions.setAccountTo(this);
        transactionsIn.add(transactions);
    }

    public List<Transaction> getTransactionsOut() {
        return transactionsOut;
    }

    public void setTransactionsOut(Transaction transaction) {
        transaction.setAccountFrom(this);
        transactionsOut.add(transaction);
    }

    public String toString() {
        return "Account #" + id + ": customer - " + customer.getName() + ", currency - " + currency + ", amount - " + amount;
    }
}
