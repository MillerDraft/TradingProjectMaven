package org.example;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Output {
    private DecimalFormat df;
    public Output(){
        // Format the number in decimal form, truncating it to 2 decimal places.
        df = new DecimalFormat("#,##0.00");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);
    }

    // Common method StockMarket and Broker class
    protected void stockNotFound(String s){
        System.out.println("Stock " + s + " not found");
    }

    protected void showAvailableShares(String s, double d){
        System.out.println(s + " has " + d + " available in the market.");
    }


    // StockMarket class outputs shown in the order of appearance top to bottom
    protected void historicalPricesHeadLine(String s){
        System.out.println("Historical Stock prices of: " + s);
    }

    protected void historicalPrices(double p, LocalDateTime t){
        System.out.println("Price: " + df.format(p) +
                " Time: " + t.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

    }

    //output.stockNotFound(stockId). See implementation in common method
    //protected void showAvailableShares. See implementation in common method


    // Broker class outputs shown in the order of appearance top to bottom
    protected void addInvestor(String s1){System.out.println("New Investor " + s1 + " has been added");}

    protected void remove1(String s1){ System.out.println("Investor " + s1 + " is successfully removed");}
    protected void remove2(){ System.out.println("Investor not found");}

    protected void startOperation(){
        System.out.println("Processing buy operation....");
    }

    protected void startBuying(){
        System.out.println("Buying process start...");
    }

    protected void buyingProcessConfirmation1(String s1, double d, String s2){
        System.out.println("Investor " + s1 + " bough " + d + " shares of " + s2);
    }

    protected void buyingProcessConfirmation2(String s1, double d, String s2){
        System.out.println("We couldn't trade you entire order because there was only " + d + " shares available");
        System.out.println("Investor " + s1 + " bough " + d + " shares of " + s2);
    }

    protected void buyingProcessFailure1(String s1, double p, double c, String s2){
        System.out.println("Investor " + s1 + " don't have enough capital to buy the inserted amount\n" +
                "Stock " + s2 + " price: " + df.format(p) + "\n" +
                " with a capital of: " + df.format(c) + " you just can buy " + df.format(c/p) + " shares" );
    }

    protected void buyingProcessFailure2(){
        System.out.println("Invalid quantity value or there's not available stock share to sell " +
                "at this moment");
    }

    // stockNotFound(stockId). See implementation in common method

    protected void showPortfolio(String s, double v){
        System.out.println("Investor " + s + ": Portfolio value $" + df.format(v));
    }

    protected void showCurrentStockPrice(String s1, double p){
        System.out.println(s1 + ": " + df.format(p));
    }


    protected void cancelOperation1(String s1, String s2){
        System.out.println("Investor " + s1 + " don't posses " + s2 + " Stock share");
    }

    protected void cancelOperation2(){
        System.out.println("Investor not found");
    }

    // Investor class outputs shown in the order of appearance top to bottom
    protected void myDetails(String s1, double c, HashMap map, double cI, double pv){
        System.out.println( "Name " + s1 + " Capital available: " + df.format(c)+ "\n" +
                "Stock Owned " + map + " Capital invested " + df.format(cI) + "\n" +
                "Portfolio actual value: " + df.format(pv)+ "\n");
    }

    protected void sellOperationStart(){
        System.out.println("The sales process has been started...");
    }

    protected void sellOperationConfirmed(String s1, double d, String s2){
        System.out.println("Investor " + s1 + " sold " + d + " shares of " + s2);

    }

    // showAvailableShares. See implementation in common method


    // TradeMarket class outputs shown in the order of appearance top to bottom
    protected void openMarket(LocalDateTime t){
        System.out.println("Market Open at: " + t.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    protected void closeMarket(LocalDateTime t){
        System.out.println("Market Close at: " + t.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))+"\n");
    }



}
