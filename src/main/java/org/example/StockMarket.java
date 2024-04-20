package org.example;

import java.time.LocalDateTime;
import java.util.*;

public class StockMarket {
    private Output output;
    private HashMap <Stock, Double> stockMap;
    private HashMap <Stock, Stack<HashMap<Double,LocalDateTime>>> stockTimeLine;
    // Map of Stock and amount
    public StockMarket(){
        stockMap = new HashMap<>();
        stockTimeLine = new HashMap<>();
        List<Stock> wallStreet = stockList();

        for (Stock stock : wallStreet){
            // Initialize market with 100 shares for stock;
            stockMap.put(stock, 100.0);

            // Initialize the price record HashMap for each stock
            HashMap<Double,LocalDateTime> innerMap = new HashMap<>();
            innerMap.put(stock.price(), LocalDateTime.now());

            // Initialize stack and push innerMap
            Stack stack = new Stack();
            stack.push(innerMap);
            stockTimeLine.put(stock, stack);
        }
    }

    // Parameter injection via Setter
    protected void setOutput(Output output){
        this.output = output;
    }

    // Method to generate stock list
    private List<Stock> stockList() {
        List<Stock> wallStreet = new ArrayList<>();
        Stock apple = new Stock("AAPL", 35);
        Stock nvidia = new Stock("NVDA", 15);
        Stock amazon = new Stock("AMZN", 12);
        Stock tesla = new Stock("TSLA", 21);
        Stock bitcoin = new Stock("BITCOIN", 50);
        Stock ethereum = new Stock("ETHEREUM", 41);

        wallStreet.add(apple);
        wallStreet.add(nvidia);
        wallStreet.add(amazon);
        wallStreet.add(nvidia);
        wallStreet.add(tesla);
        wallStreet.add(bitcoin);
        wallStreet.add(ethereum);
        return wallStreet;
    }

    // Method to retrieve stock
    public Stock getStock(String stockId) {
        for (Stock s: stockMap.keySet()){
            if (s.stockId().equals(stockId)){
                return s;
            }
        }
        return null;
    }

    public HashMap getStockMap(){
        return stockMap;
    }

    public HashMap getStockTimeLine(){
        return stockTimeLine;
    }


    // Method to store the price range of a stock in a period of time
    public void updateStockTimeLine(String stockId, Double newPrice, LocalDateTime currentTime){
        for (Stock stock: stockTimeLine.keySet()) {
            if (stockId.equals(stock.stockId())) {
                Stack<HashMap<Double, LocalDateTime>> stack = stockTimeLine.get(stock); // Get the stack tha contains the HashMap
                // Create a new Map to store the price and time
                HashMap<Double, LocalDateTime> innerMap = new HashMap<>();
                innerMap.put(newPrice, currentTime);
                // Put the innerMap in the stack
                stack.push(innerMap);
                return;
            }
        }
    }

    // Method to print out Historical Stock Prices
    public void showStockRecord(String stockId) {
        Stack<HashMap<Double, LocalDateTime>> stack = null;

        // Find the stock
        for (Stock stock: stockTimeLine.keySet()){
            if (stockId.equals(stock.stockId())) {
                output.historicalPricesHeadLine(stockId); // Invoke a method to display the message

                stack = stockTimeLine.get(stock);

                // Create a copy of the stack to avoid ConcurrentModificationException
                var copyStack = (Stack<HashMap<Double, LocalDateTime>>) stack.clone();

                // Use Iterator to iterate over the stack without remove elements
                Iterator<HashMap<Double, LocalDateTime>> iterator = copyStack.iterator();
                while (iterator.hasNext()) {
                    HashMap<Double, LocalDateTime> currentMap = iterator.next();

                    // Iterate over map to print historical stock prices
                    for (Map.Entry<Double, LocalDateTime> entry : currentMap.entrySet()) {
                        output.historicalPrices(entry.getKey(), entry.getValue()); // Invoke a method to display the message
                    }
                }

            }
        }

        // Print a message if the stockId is not valid or stock is not in the stockMap
        if (stack == null){
            output.stockNotFound(stockId); // Invoke a method to display the message
        }

    }

    // Method to show the amount of Stock share available for trade in the market
    public void showAvailableShares(String stockId) {
        double availableShares = getStockAmount(stockId);
        output.showAvailableShares(stockId, availableShares);// Invoke a method to display the message
    }

    // Retrieve Stock amount if stockMap
    protected double getStockAmount(String stockId){
        double stockAmount = 0;
        for (Stock stock : stockMap.keySet()) {
            if (stock.stockId().equals(stockId)) {
                stockAmount = stockMap.get(stock);
            }
        }
        return stockAmount;
    }

    //Method to update StockMap
    public void updateStockMap(String stockId, double quantity){
        for (Stock stock : stockMap.keySet()) {
            if (stock.stockId().equals(stockId)) {
                stockMap.put(stock, quantity);
            }
        }
    }
}
