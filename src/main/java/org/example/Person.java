package org.example;

public class Person {
    private String name;
    private double money;

    public Person(String name, double money){
        this.name = name;
        this.money = money;
    }

    public double getMoney(){
        return money;
    }

    public String getName(){
        return name;
    }

    public void setMoney (double money){
        this.money = money;
    }
}
