package org.example;

import java.util.Objects;

public class Stock {
    private String stockId;
    private double price;
    public Stock(String stockId, double price){
        this.stockId = stockId;
        this.price = price;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(stockId, stock.stockId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stockId);
    }

    public String stockId(){
        return stockId;
    }

    public double price(){
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }
}
