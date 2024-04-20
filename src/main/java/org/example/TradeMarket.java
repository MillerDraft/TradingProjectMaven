package org.example;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TradeMarket {
    private Output output;
    private StockMarket wallStreet;
    private Timer timer;
    private TimerTask myTask;
    private LocalDateTime startTime;
    private List<Broker> brokerList;
    public TradeMarket(StockMarket wallStreet){
        this.wallStreet = wallStreet;
        timer = new Timer();
        brokerList = createBrokerList();

    }

    // Parameter injection via Setter
    protected void setOutput(Output output){
        this.output = output;
    }

    // Open market method
    public void openMarket(){
        startTime = LocalDateTime.now();
        output.openMarket(startTime); // Invoke a method to display the message
        starTrading();
        resetTimerMarket();
        setBroker();
    }

    // Create a List of Broker
    private List<Broker> createBrokerList(){
        return new ArrayList<>();
    }

    // Store new Broker in the Broker List
    public void addBroker(Broker broker){
        brokerList.add(broker);
    }

    // Update the Stock price for each broker in the Broker List when the market opens
    private void setBroker(){
        if (!brokerList.isEmpty()){
            for (Broker b: brokerList){
                // Call the method in the Broker to update all investor portfolios
                b.updateInvestorPortfolioValue();
            }
        }
    }

    // Managing Trading Market Hours
    private void starTrading(){
        myTask = new TimerTask() {
            @Override
            public void run() {
                bull_Bear();
            }
        };
        // Set the delay (in milliseconds) before the task is executed
        long delay = 0; // 0 seconds

        // Set the period (time between successive task executions)
        long period = 500; // 0.5 seconds

        // Schedule the task for repeated fixed-delay execution
        // The first execution will occur after the specified delay
        timer.schedule(myTask, delay, period);

        // Keep the program running for demonstration purposes
        try {
            Thread.sleep(2000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Method for Simulating Trading Activity
    private void bull_Bear(){
        // Create a random object
        Random r = new Random();
        HashMap<Stock, Double> myStockMap = wallStreet.getStockMap();
        for (Map.Entry<Stock, Double> entry: myStockMap.entrySet()){
            Stock currentStock = entry.getKey();

            // Call method to generate volatility manager
            int volatilityLevel = volatilityManager(currentStock);

            double stockPrice = currentStock.price();

            double volatilityMultiplier = switch (volatilityLevel) {
                case 'L' -> // Low volatility
                        0.05; // 5%
                case 'S' -> // Standard volatility
                        0.1; // 10%
                case 'H' -> // High volatility
                        0.2; // 20%
                case 'X' -> // Super high volatility
                        0.3; // 30%
                default -> // Standard volatility if invalid input
                        0.1; // 10%
            };

            // Generate a random percentage change using volatility
            // Random value between -volatilityMultiplier/2 and +volatilityMultiplier/2
            // the random percentage change is centered around 0 (no change),
            // but the range within which it can vary is wider compared to lower volatility levels.
            double percentageChange = (r.nextDouble() - 0.5) * volatilityMultiplier;

            // Calculate the new price based on the percentage change
            // Add 1 is necessary to increase price if the percentage change is positive or decrease otherwise
            double newPrice = stockPrice * (1 + percentageChange);
            if(newPrice < 0){
                newPrice = 0;
            }

            // Update stock price
            currentStock.setPrice(newPrice);

            // Update stock record price HashMap
            wallStreet.updateStockTimeLine(currentStock.stockId(), currentStock.price(), LocalDateTime.now());
        }
    }

    // Methods for Managing Stock Price Volatility
    private char volatilityManager(Stock stock){
        LocalDateTime currentTime = LocalDateTime.now();

        // Calculate how many seconds passed since the market open till now
        long elapsedTime = ChronoUnit.SECONDS.between(startTime, currentTime);

        // Period of time
        long firstRange = 2; // First 2 sec after the Market open
        long secondRange = 4; // Between 2 and 4 sec market medium time and After 4 sec about to finish

        // Managing Volatility for Each Stock in the Early Market Period
        if (elapsedTime <= firstRange){
            if (stock.stockId().equals("AAPL") || stock.stockId().equals("NVDA")){
                return 'S'; // Standard volatility
            }
            else if (stock.stockId().equals("BITCOIN") || stock.stockId().equals("ETHEREUM")) {
                return 'H'; // High volatility
            }
            else if (stock.stockId().equals("TSLA") || stock.stockId().equals("AMZN")) {
                return 'L'; // Low volatility
            }
        }

        // Managing Volatility for Each Stock in the Medium Market Period
        else if (elapsedTime <= secondRange){
            if (stock.stockId().equals("AAPL") || stock.stockId().equals("NVDA")){
                return 'L'; // Low volatility
            }
            else if (stock.stockId().equals("BITCOIN") || stock.stockId().equals("ETHEREUM")) {
                return 'X'; // Super high volatility
            }
            else if (stock.stockId().equals("TSLA") || stock.stockId().equals("AMZN")) {
                return 'S'; // Standard volatility
            }
        }

        // Managing Volatility for Each Stock in the Final Market Period
        else {
            if (stock.stockId().equals("AAPL") || stock.stockId().equals("NVDA")){
                return 'H'; // High volatility
            }
            else if (stock.stockId().equals("BITCOIN") || stock.stockId().equals("ETHEREUM")) {
                return 'L'; // Low volatility
            }
            else if (stock.stockId().equals("TSLA") || stock.stockId().equals("AMZN")) {
                return 'S'; // Standard volatility
            }
        }

        // Add a default return statement to handle cases where none of the conditions are met
        return 'L'; // Low volatility
    }

    // Method to reset the timer in preparation for the next day’s trading session.
    private void resetTimerMarket(){
        // Reset the Timer instead of canceling it
        timer.purge();
        timer = new Timer();

        LocalDateTime currentTime = LocalDateTime.now();
        output.closeMarket(currentTime); // Invoke a method to display the message
    }

}
