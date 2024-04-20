package org.example;

import java.util.HashMap;

public class Investor extends Person{
    private Output output;
    private HashMap<String, Double> stockOwned;
    private double buyingPower; //  Initial Capital to start the trading operation
    private double capitalInvested; // Amount invested
    private double portfolioValue; // This amount varies due to market fluctuations


    public Investor(String name, double money) {
        super(name, money);
        output = new Output();
        buyingPower = money * 0.5; // Just the 50% of the money will be used for investments
        stockOwned = new HashMap<>();
        capitalInvested = 0;  // Money spent when an Investor buys stock
        portfolioValue = 0; // Actual value of all Stock shares owned
    }

    public HashMap getStockOwned(){
        return stockOwned;
    }

    // Reducing Stock Shares owned when Investor Sells
    public double decreaseOwnedStock(String stockId, double quantity){
        double totalShares = 0;
        if (stockOwned.containsKey(stockId)){
            double stockAmount = stockOwned.get(stockId);
            if (stockAmount >= quantity){
                stockAmount -= quantity;
                totalShares = quantity;

                if (stockAmount > 0){
                    stockOwned.put(stockId, stockAmount);
                }

                else{
                    stockOwned.remove(stockId);
                }
            }

            else{
                totalShares = stockAmount;
                stockOwned.remove(stockId);
            }
        }
        return totalShares;
    }

    // Method to update buyingPower
    public void updateBuyingPower(double amountOfShares, double stockPrice){
        buyingPower += (amountOfShares * stockPrice);
    }

    // Method to Increase the Number of Stock Shares When Investors Go to Buy
    public double increaseOwnedStock(String newStockId, double quantity){
        double totalShare = 0;
        if (stockOwned.containsKey(newStockId)){
            double currentStockAmount = stockOwned.get(newStockId);
            stockOwned.put(newStockId, currentStockAmount + quantity);
        }
        else{
            stockOwned.put(newStockId, quantity);
        }
        totalShare = quantity;

        // Return totalShareBough in negative to effectively reduce the capital amount
        return totalShare * -1;
    }
    

    public double getCapital(){
        return buyingPower;
    }

    public double getPortfolioValue(){
        return portfolioValue;
    }

    public void setPortfolioValue(double sumOfAllSharesAtCurrentPrice){
        portfolioValue = sumOfAllSharesAtCurrentPrice;
    }

    public double getCapitalInvested(){
        return capitalInvested;
    }

    public void updateCapitalInvested(double quantity, double stockPrice){
        capitalInvested += (quantity * stockPrice);
    }

    protected void myDetails() {
        output.myDetails(getName(), getCapital(), stockOwned, getCapitalInvested(), getPortfolioValue());
    }
}
