package com.application.nick.pizzaplanet.entity.Purchases;

/**
 * Created by Nick on 7/10/2015.
 */
public class EmployeePurchase {

    long numEmployees, price;

    public EmployeePurchase(long numEmployees, long price) {
        this.numEmployees = numEmployees;
        this.price = price;
    }

    public long getNumEmployees() {
        return numEmployees;
    }

    public long getPrice() {
        return price;
    }
}
