package org.example;

public class Main {
    public static void main(String[] args) {
        Output output = new Output();
        StockMarket nasdaq = new StockMarket();
        nasdaq.setOutput(output);
        Broker broker = new Broker(nasdaq);
        broker.setOutput(output);

        System.out.println("..........................");
        TradeMarket london = new TradeMarket(nasdaq);
        london.addBroker(broker);
        london.setOutput(output);
        london.openMarket();
        /*
        Initial prices of each stock
        AAPL, 35
        NVDA, 15
        AMZN, 12
        TSLA, 21
        BITCOIN, 50
        ETHEREUM, 41
         */

        System.out.println("Variation in stock prices over one business period.\n");
        nasdaq.showStockRecord("AAPL");
        System.out.println(".");

        nasdaq.showStockRecord("NVDA");
        System.out.println(".");

        nasdaq.showStockRecord("AMZN");
        System.out.println(".");

        nasdaq.showStockRecord("ETHEREUM");
        System.out.println(".");

        nasdaq.showStockRecord("BITCOIN");
        System.out.println("...........");


        Investor pepe = new Investor("Pepe", 3000);
        Investor julio = new Investor("Julio", 100);
        Investor kira = new Investor("Kira", 20000);
        Investor lola = new Investor("Lola", 30);

        System.out.println("New investors are signing into the broker site");
        broker.addInvestor(pepe);
        broker.addInvestor(julio);
        broker.addInvestor(kira);
        broker.addInvestor(lola);
        System.out.println(".");

        System.out.println("Let's see all the investors that have join to this broker");
        broker.showInvestorsList();

        System.out.println("Let's see Pepe initial details");
        pepe.myDetails();
        System.out.println(".");

        System.out.println("Pepe is buying 6 shares of apple");
        broker.buyStock("AAPL", pepe, 6);
        System.out.println(".");

        System.out.println("Let's see Pepe details");
        pepe.myDetails();
        System.out.println(".");

        System.out.println("Let's see Julio initial details");
        julio.myDetails();
        System.out.println(".");

        System.out.println("Julio is buying 20 shares of Amazon");
        broker.buyStock("AMZN", julio, 20);
        System.out.println(".");

        System.out.println("Julio is now buying 2 shares of Amazon");
        broker.buyStock("AMZN", julio, 2);
        System.out.println(".");

        System.out.println("Let's see Julio details after buy 2 shares of Amazon");
        julio.myDetails();
        System.out.println(".");

        System.out.println("Lola is buying 1 shares of Nvidia");
        broker.buyStock("NVDA", julio, 1);
        System.out.println(".");

        System.out.println("Let's see Julio details after buy 2 shares of Amazon");
        julio.myDetails();
        System.out.println(".");

        System.out.println("Testing getStock method with amazon");
        System.out.println(nasdaq.getStock("AMZN"));
        System.out.println(".");
        nasdaq.showAvailableShares("AMZN");

        System.out.println("Let's see Kira initial details");
        kira.myDetails();
        System.out.println(".");

        System.out.println("Kira is buying 5 shares of nvidia");
        broker.buyStock("NVDA", kira, 5);
        nasdaq.showAvailableShares("NVDA");
        System.out.println(".");

        System.out.println("Kira is buying 20 shares of ETHEREUM");
        broker.buyStock("ETHEREUM", kira, 20);
        nasdaq.showAvailableShares("ETHEREUM");
        System.out.println(".");

        System.out.println("Kira is buying 99 shares of apple");
        broker.buyStock("AAPL", kira, 99);
        nasdaq.showAvailableShares("AAPL");
        System.out.println(".");

        System.out.println("Testing showStockRecord with apple on a 2nd market session");
        london.openMarket();
        System.out.println(".");
        broker.showStockActualPrice("AAPL");
        System.out.println(".");

        System.out.println("Testing broker showInvestorPortfolioValue method");
        broker.showInvestorPortfolioValue(pepe);
        System.out.println(".");

        System.out.println("Let's see Julio details after 2nd  market session to compare");
        julio.myDetails();
        System.out.println(".");

        System.out.println("Let's see Kira details after 2nd market session to compare");
        kira.myDetails();
        System.out.println(".");


        System.out.println("Testing showStockRecord with ethereum on a 3rd market session");
        london.openMarket();
        broker.showStockActualPrice("ETHEREUM");
        System.out.println(".");

        System.out.println("Let's see Pepe details after 3rd market session to compare");
        pepe.myDetails();
        System.out.println(".");

        System.out.println("Let's see Julio details after 3rd market session to compare");
        julio.myDetails();
        System.out.println(".");

        System.out.println("Let's see Kira details after 3rd market session to compare");
        kira.myDetails();
        System.out.println(".");

        System.out.println("Testing showStockRecord with nvidia on a 4th market session");
        london.openMarket();
        broker.showStockActualPrice("NVDA");
        System.out.println(".");

        System.out.println("Testing showStockRecord with bitcoin on a 5th market session");
        london.openMarket();
        broker.showStockActualPrice("BITCOIN");
        System.out.println(".");

        System.out.println("Let's see Pepe details after 5th market session to compare");
        pepe.myDetails();
        System.out.println(".");

        System.out.println("Let's see Julio details after 5th market session to compare");
        julio.myDetails();
        System.out.println(".");

        System.out.println("Julio is selling 1 share");
        broker.sellStock(julio,"AMZN",1);
        System.out.println(".");

        System.out.println("Let's see Kira details after 5th market session to compare");
        kira.myDetails();
        System.out.println(".");

        System.out.println("Testing message of StockMarket showStockRecord for invalid stockId (TRR)");
        nasdaq.showStockRecord("TRR");
        System.out.println(".");

        System.out.println("Lola is trying to sell share(s) that she doesn't have ");
        broker.sellStock(lola,"TSLA",3);
        System.out.println(".");

        System.out.println("Lola is removed from broker to try to commit fraud");
        broker.deleteInvestor(lola);
        System.out.println(".");

        System.out.println("Let's try to remove lola again to test using an investor that is not in the broker list");
        broker.deleteInvestor(lola);
        System.out.println(".");

        System.out.println("Let's see all the investors currently in broker list");
        broker.showInvestorsList();



    }
}