package ebee.auction;

public class CurrencyUtils {
    
}

public class Currency {
    private static HashMap<String, Currency> listOfCurrencies;
    private static HashMap<String, Double> listOfVAT;
    
    public static Currency currencyByCountryCode(String country){
        return listOfCurrencies.getCurrency(country);
    }

    public static Double vatByCountryCode(Sring country){
        return listOfVAT.getVAT(country);
    }

    public static Double currencyRate(String fromCountry, String toCountry, Date today){
        MarketProvider mkt = reutersConnector();
        return mkt.currencyRate(fromCountry, toCountry, today);
    }
}
