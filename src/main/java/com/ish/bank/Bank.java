package com.ish.bank;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;
import java.util.Scanner;

/**
 * Created by igor on 30.10.2017.
 */
public class Bank {

    private EntityManagerFactory emf;
    private EntityManager em;

    public Bank() {
    }

    public void open() {
        emf = Persistence.createEntityManagerFactory("JPATest");
        em = emf.createEntityManager();
    }

    public void close() {
        em.close();
        emf.close();
    }

    public void addCustomer() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter customer name:");
        String name = scan.nextLine();

        em.getTransaction().begin();
        Customer customer = new Customer(name);
        em.persist(customer);
        em.getTransaction().commit();
        System.out.println("Customer " + name + " has been added!");
    }

    public void addAccount() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter customer's name:");
        String name = scan.nextLine();
        System.out.println("Enter currency of account 980 (uah), 840 (usd), 978 (eur):");
        int currency = scan.nextInt();
        System.out.println("Enter amount on account:");
        double amount = scan.nextDouble();

        Query request = em.createQuery("select c from Customer c where c.name = :name", Customer.class);
        request.setParameter("name", name);
        Customer customer = (Customer) request.getSingleResult();

        em.getTransaction().begin();
        Account account = new Account(currency);
        customer.addAccount(account);
        account.setAmount(amount);
        em.persist(account);
        em.getTransaction().commit();
        System.out.println(account + " has been added!");
    }

    public void replenishAccount() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter account's id to replenish:");
        System.out.println("Available accounts:");
        showAllAccounts();
        int id = scan.nextInt();
        Account account = em.find(Account.class, id);
        int currency = account.getCurrency();

        System.out.println("Enter replenishment amount in currency " + currency + ":");
        double amount = scan.nextDouble();
        double amountBefore = account.getAmount();
        double amountAfter = amountBefore + amount;

        em.getTransaction().begin();
        account.setAmount(amountAfter);
        em.persist(account);
        em.getTransaction().commit();
        System.out.println("Account #" + account.getId() + " has been replenished on amount " + amount + " in currency "
                + currency);
        System.out.println("Current amount on account = " + amountAfter + " in currency " + currency);
    }

    public void fundsConversion() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter customer's name to execute funds currency conversion:");
        String name = scan.nextLine();

        Query request = em.createQuery("select c from Customer c where c.name = :name", Customer.class);
        request.setParameter("name", name);
        Customer customer = (Customer) request.getSingleResult();

        em.getTransaction().begin();
        System.out.println("Accounts of customer " + name + " :" + customer.getAccounts());
        System.out.println("Enter account id to convert funds from: ");
        int idFrom = scan.nextInt();
        Account accountFrom = em.find(Account.class, idFrom);
        int currencyFrom = accountFrom.getCurrency();
        double amountFromBegin = accountFrom.getAmount();

        System.out.println("Enter account id to convert funds to: ");
        int idTo = scan.nextInt();
        Account accountTo = em.find(Account.class, idTo);
        int currencyTo = accountTo.getCurrency();
        double amountToBegin = accountTo.getAmount();
        double amountFromEnd = 0;

        System.out.println("Enter amount in currency: " + currencyFrom + " to convert");
        double amount = scan.nextDouble();
        if (amountFromBegin >= amount) {
            amountFromEnd = amountFromBegin - amount;
        } else System.out.println("Not enough funds on account to convert!");

        Courses courses = em.find(Courses.class, 1);
        double usd = courses.getUsd();
        double eur = courses.getEur();

        double amountToEnd = 0;
        if (currencyFrom == currencyTo) {
            amountToEnd = amountToBegin + amount;
        }
        if (currencyFrom == 980 && currencyTo == 840) {
            amountToEnd = amountToBegin + amount / usd;
        }
        if (currencyFrom == 980 && currencyTo == 978) {
            amountToEnd = amountToBegin + amount / eur;
        }
        if (currencyFrom == 840 && currencyTo == 980) {
            amountToEnd = amountToBegin + amount * usd;
        }
        if (currencyFrom == 840 && currencyTo == 978) {
            amountToEnd = amountToBegin + amount * usd / eur;
        }
        if (currencyFrom == 978 && currencyTo == 980) {
            amountToEnd = amountToBegin + amount * eur;
        }
        if (currencyFrom == 978 && currencyTo == 840) {
            amountToEnd = amountToBegin + amount * eur / usd;
        }

        accountFrom.setAmount(amountFromEnd);
        accountTo.setAmount(amountToEnd);
        em.persist(accountFrom);
        em.persist(accountTo);
        em.getTransaction().commit();
        System.out.println("Conversion of funds from account " + accountFrom.getId() + " to account " + accountTo.getId()
                + " in currency " + currencyFrom + " in amount " + amount + " completed successfully!");
    }

    public void countTotalAmountOnAccounts() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter customer's name to count total funds amount on accounts:");
        String name = scan.nextLine();

        Query request = em.createQuery("select c from Customer c where c.name = :name", Customer.class);
        request.setParameter("name", name);
        Customer customer = (Customer) request.getSingleResult();
        List<Account> accounts = customer.getAccounts();

        Courses courses = em.find(Courses.class, 1);
        double usd = courses.getUsd();
        double eur = courses.getEur();

        double total = 0;
        for (Account account : accounts) {
            double amount = account.getAmount();
            int currency = account.getCurrency();
            if (currency == 980) {
                total += amount;
            }
            if (currency == 840) {
                total += amount * usd;
            }
            if (currency == 978) {
                total += amount * eur;
            }
        }
        System.out.println("Total funds amount on " + name + " accounts equals " + total + " UAH");
    }

    public void addTransaction() {
        System.out.println("Available accounts for transactions:");
        showAllAccounts();
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter id of account to make transaction from:");
        int from = scan.nextInt();
        System.out.println("Enter id of account to make transaction to:");
        int to = scan.nextInt();
        em.getTransaction().begin();

        Query request1 = em.createQuery("select a from Account a where a.id = :id", Account.class);
        request1.setParameter("id", from);
        Account accountFrom = (Account) request1.getSingleResult();

        Query request2 = em.createQuery("select a from Account a where a.id = :id", Account.class);
        request2.setParameter("id", to);
        Account accountTo = (Account) request2.getSingleResult();

        int currencyFrom = accountFrom.getCurrency();
        int currencyTo = accountTo.getCurrency();
        System.out.println("Enter amount of transaction in currency " + currencyFrom);
        double amount = scan.nextDouble();
        Transaction transaction = new Transaction(accountFrom, accountTo, currencyFrom, amount);

        if (accountFrom.getAmount() >= amount && currencyFrom == currencyTo) {
            double newAmountFrom = accountFrom.getAmount() - amount;
            double newAmountTo = accountTo.getAmount() + amount;
            accountFrom.setAmount(newAmountFrom);
            accountTo.setAmount(newAmountTo);
            accountFrom.setTransactionsOut(transaction);
            accountTo.setTransactionsIn(transaction);
        } else System.out.println("Not enough funds to execute transaction or accounts' currency isn't the same!");

        em.persist(accountFrom);
        em.persist(accountTo);
        em.persist(transaction);
        em.getTransaction().commit();
        System.out.println("Transaction from account " + accountFrom.getId() + " to account " + accountTo.getId()
                + " in currency " + currencyFrom + " in amount " + amount + " completed successfully!");
    }

    public void showAllCustomers() {
        Query request = em.createQuery("select d from Customer d", Customer.class);
        List<Customer> customers = request.getResultList();
        System.out.println("Customers: ");
        for (Customer customer : customers) System.out.println(customer);
    }

    public void showAllAccounts() {
        Query request = em.createQuery("select a from Account a", Account.class);
        List<Account> accounts = request.getResultList();
        System.out.println("Accounts: ");
        for (Account account : accounts) System.out.println(account);
    }

    public void showAllTransactions() {
        Query request = em.createQuery("select t from Transaction t", Transaction.class);
        List<Transaction> transactions = request.getResultList();
        System.out.println("Accounts: ");
        for (Transaction transaction : transactions) System.out.println(transaction);
    }

    public void setCourses() {
        em.getTransaction().begin();
        Courses courses = new Courses();
        em.persist(courses);
        em.getTransaction().commit();
    }
}
