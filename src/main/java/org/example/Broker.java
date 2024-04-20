package org.example;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.*;

public class Broker {
    private Output output;
    private DecimalFormat df;
    private List<Investor> investors;
    private StockMarket market;

    public Broker(StockMarket market) {
        this.market = market;
        investors = new ArrayList<>();

        // Call a method to automatically update the portfolio value for each investor
        updateInvestorPortfolioValue();
    }

    // Parameter injection via Setter
    protected void setOutput(Output output){
        this.output = output;
    }

    // Method to update the portfolio value for each investor
    protected void updateInvestorPortfolioValue(){
        if (!investors.isEmpty()){
            for (Investor investor: investors) {
                double actualPortfolioValue = calculateInvestorPortfolioValue(investor);
                investor.setPortfolioValue(actualPortfolioValue);
            }
        }
    }

    // Method to add new Investor to Investors list
    public void addInvestor(Investor investor){
        investors.add(investor);
        output.addInvestor(investor.getName());
    }

    // Method to remove Investor from Investors list
    public void deleteInvestor(Investor investor){
        if (investors.contains(investor)){
            output.remove1(investor.getName());
            investors.remove(investor);
        }
        else{
            output.remove2();
        }

    }

    public void showInvestorsList(){
        for (Investor investor : investors){
            investor.myDetails();
        }
    }

    // Method for Investor by buy stock
    public void buyStock(String stockId, Investor investor, double quantity){
        double capital = investor.getCapital(); // Capital to buy shares

        boolean stockFound = false; // Flag to track if the stock is found

        Stock currentStock = market.getStock(stockId); // Retrieve Stock
        if(currentStock != null){
            stockFound = true; // Set the flag to true
            double sharesAvailableInTheMarket = market.getStockAmount(stockId);

            // Check if there are available shares to buy and a valid quantity value
            if (sharesAvailableInTheMarket > 0 && (quantity > 0 && quantity <= 100)){
                output.startOperation(); // Invoke a method to display the message

                // Call method to obtain the share actual price
                double stockPrice = getShareActualPrice(stockId);

                // Call method to check if investor have enough capital to buy this amount of shares
                if (acceptOperation(capital, quantity, stockPrice)){
                    output.startBuying(); // Invoke a method to display the message

                    // Check if there are enough stock available to complete this order
                    if (sharesAvailableInTheMarket >= quantity) {
                        double remainQuantity = sharesAvailableInTheMarket - quantity;

                        // Call method to update available shares in stockMap
                        market.updateStockMap(stockId, remainQuantity);

                        // Add new shares in Investor stockOwned
                        double amountOfShares = investor.increaseOwnedStock(stockId,quantity);

                        // Update Investor buyingPower
                        investor.updateBuyingPower(amountOfShares, stockPrice );

                        // Update Investor Capital Invested
                        investor.updateCapitalInvested(quantity, stockPrice);
                        // Invoke a method to display the message
                        output.buyingProcessConfirmation1(investor.getName(), quantity, stockId);
                    }

                    else {
                        // Add new shares in Investor stockOwned
                        double amountOfShares = investor.increaseOwnedStock(stockId,sharesAvailableInTheMarket);
                        // Update Investor buyingPower
                        investor.updateBuyingPower(amountOfShares, stockPrice );

                        // Update Investor Capital Invested
                        investor.updateCapitalInvested(sharesAvailableInTheMarket, stockPrice);

                        // Invoke a method to display the message
                        output.buyingProcessConfirmation2(investor.getName(), sharesAvailableInTheMarket, stockId);

                        // All shares were sold
                        sharesAvailableInTheMarket = 0;

                        // Update available shares in stockMap
                        market.updateStockMap(stockId, sharesAvailableInTheMarket);
                    }
                }

                else{
                    // Invoke a method to display the message
                    output.buyingProcessFailure1(investor.getName(), stockPrice, investor.getCapital(), stockId);
                }
            }

            else{
                output.buyingProcessFailure2(); // Invoke a method to display the message
            }
        }
        // If the stock is not found after iterating through the loop print message
        if (!stockFound) {
            output.stockNotFound(stockId); // Invoke a method to display the message
        }

        else{
            double actualPortfolioValue = calculateInvestorPortfolioValue(investor);
            investor.setPortfolioValue(actualPortfolioValue);
        }
    }

    // Method to check if Investor have enough capital to buy the shares
    private boolean acceptOperation(double capital, double quantity, double price){
        return (capital - quantity * price) > 0;
    }
    
    // Method to update Investor portfolio
    private double calculateInvestorPortfolioValue (Investor investor){
        double totalValue = 0;

        // Call method to get stockTimeLine(Stock price record)
        HashMap <Stock, Stack<HashMap<Double,LocalDateTime>>> myStockTimeLine = market.getStockTimeLine();
        // Check if investor is in Investors List
        if (investors.contains(investor)){
            //Call a method to retrieve all the stock shares that the investor owns
            // and their corresponding amounts
            HashMap<String, Double> myStockOwnedMap = investor.getStockOwned();
            for (Map.Entry<String,Double> entry: myStockOwnedMap.entrySet()) {
                String currentStockId = entry.getKey();
                double currentStockAmount = entry.getValue();

                // Iterate over myStockTimeLine to retrieve the latest price for each stock
                for (Stock stock : myStockTimeLine.keySet()) {
                    if (stock.stockId().equals(currentStockId)) {
                        Stack<HashMap<Double, LocalDateTime>> stack = myStockTimeLine.get(stock);
                        if (!stack.isEmpty()) {
                            // Get the top HashMap of stack
                            HashMap<Double, LocalDateTime> currentInnerMap = stack.peek();
                            double stockPrice = 0;

                            // Get the latest stock price directly
                            if (!stack.isEmpty()) {
                                stockPrice = currentInnerMap.keySet().iterator().next();
                                // If you need the LocalDateTime as well, you can access it like this:
                                // LocalDateTime timeStamp = currentInnerMap.get(stockPrice);
                            }

                            totalValue += (stockPrice * currentStockAmount);
                        }
                        break; // No need to continue iteration once found
                    }
                }
            }
        }
        return totalValue;
    }

    public void showInvestorPortfolioValue(Investor investor){
        double actualPortfolioValue = calculateInvestorPortfolioValue(investor);
        output.showPortfolio(investor.getName(), actualPortfolioValue); // Invoke a method to display the message
    }

    // Method to Get Stock share actual Price    
    private double getShareActualPrice(String stockId){
        double stockPrice = 0;
        HashMap <Stock, Stack<HashMap<Double,LocalDateTime>>> myStockTimeLine = market.getStockTimeLine();

        for (Stock stock: myStockTimeLine.keySet()) {
            if (stock.stockId().equals(stockId)) {
                Stack<HashMap<Double, LocalDateTime>> stack = myStockTimeLine.get(stock);
                if (!stack.isEmpty()){
                    // Get the top HashMap of stack
                    HashMap<Double, LocalDateTime> currentInnerMap = stack.peek();

                    // Get the latest stock price directly
                    if (!stack.isEmpty()){
                        stockPrice = currentInnerMap.keySet().iterator().next();
                        // If you need the LocalDateTime as well, you can access it like this:
                        // LocalDateTime timeStamp = currentInnerMap.get(stockPrice);
                    }
                }
                break; // No need to continue iteration once found
            }
        }
        return stockPrice;
    }

    // Method to show stock actual price
    public void showStockActualPrice(String stockId){
        double stockPrice = getShareActualPrice(stockId);
        output.showCurrentStockPrice(stockId, stockPrice);
    }

    public void sellStock(Investor investor, String stockId, double quantity){
        if (investors.contains(investor)){
            String investorName = investor.getName();
            HashMap<String, Double> myStockOwned = investor.getStockOwned();
            if (myStockOwned.containsKey(stockId)){
                output.sellOperationStart(); // Invoke a method to display the message

                // Update Investor stockOwned
                double stockAmount = investor.decreaseOwnedStock(stockId,quantity);

                // Call method to retrieve actual stock price
                double stockPrice = getShareActualPrice(stockId);

                // Update Investor buyingPower
                investor.updateBuyingPower(stockAmount, stockPrice);

                // Retrieve current stock amount in stockMap
                double currentShareAmount = market.getStockAmount(stockId);
                double newAmount = currentShareAmount + quantity; // Add new shares to the previous amount
                market.updateStockMap(stockId, newAmount); // Update stockMap

                // Invoke a method to display the message
                output.sellOperationConfirmed(investorName, quantity, stockId);
                market.showAvailableShares(stockId); // Show stock amount after update
            }

            else{
                output.cancelOperation1(investor.getName(), stockId);
            }
        }

        else {
            output.cancelOperation2();
        }
    }
}
