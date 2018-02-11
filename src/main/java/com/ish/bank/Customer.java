package com.ish.bank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by igor on 30.10.2017.
 */
@Entity
@Table
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

    public Customer() {
    }

    public Customer(String name) {
        this.name = name;
    }

    public void addAccount(Account account) {
        account.setCustomer(this);
        accounts.add(account);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return "Customer #" + id + ", name: " + name;
    }
}