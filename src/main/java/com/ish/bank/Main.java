package com.ish.bank;

import java.util.Scanner;

/**
 * Created by igor on 30.10.2017.
 */
public class Main {

    public static void main(String[] args) {

        Bank bank = new Bank();
        bank.open();
        bank.setCourses();

        Scanner scan = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("------------------------" + "\n" + "Choose the action: ");
            System.out.println("To add new customer enter 1");
            System.out.println("To add new account enter 2");
            System.out.println("To replenish account enter 3");
            System.out.println("To add new transaction enter 4");
            System.out.println("To convert funds between customer's accounts enter 5");
            System.out.println("To count total funds amount on customer's accounts enter 6");
            System.out.println("To view all customers enter 7");
            System.out.println("To view all accounts enter 8");
            System.out.println("To view all transactions enter 9");
            System.out.println("To quit enter 0" + "\n" + "->");

            int option = scan.nextInt();
            switch (option) {
                case 1:
                    bank.addCustomer();
                    break;
                case 2:
                    bank.addAccount();
                    break;
                case 3:
                    bank.replenishAccount();
                    break;
                case 4:
                    bank.addTransaction();
                    break;
                case 5:
                    bank.fundsConversion();
                    break;
                case 6:
                    bank.countTotalAmountOnAccounts();
                    break;
                case 7:
                    bank.showAllCustomers();
                    break;
                case 8:
                    bank.showAllAccounts();
                    break;
                case 9:
                    bank.showAllTransactions();
                    break;
                case 0:
                    exit = true;
                    break;
            }
        }
        bank.close();
    }
}
