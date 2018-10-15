package ebee.auction;
    

public abstract class User {
    private Double cash;
    private String country;
}

public class eBee  extends User {

    private static eBee self;
    private eBee(){
        
    }

    public static eBee eBeeCompany(){
        if(self == null){
            self = new eBee();
        }
        return self;
    }

    public Double calculatePercentFee(Double amount){

        if(amount > 1000 && amount <= 2000){
            return 0.1 * amount;
        }
        if(amount > 2000 && amount <= 5000){
            return 0.05 * amount;
        }
        if(amount > 5000){
            return 0.01 * amount;
        }
        return 0.0;
    }

    public Double calculateConstantFee(Double amount){
        if(amount <= 1000){
            return 10.0;
        }
        return 0.0;
    }

    public Double applyPromotion(Seller owner, Double feeAmount){
        
        if(owner.category == "promoted"){
            return 0.9 * feeAmount;
        }
        if(owner.category == "premium"){
            return 0.85 * feeAmount;
        }
        if(owner.category == "platinium"){
            return 0.8 * feeAmount;
        }
        return feeAmount;
    }
    
    public Double eBeeEndOfAuction(Item item){
        Double currencyRate = Currency.currencyRate(item.owner.country, eBeeCompany.country, Date.today());
        Double lastBuyerBetInTresoCurrency = item.lastBuyerBet / currencyRate ;
        Double percentFee = calculatePercentFee(lastBuyerBetInTresoCurrency);
        Double constantFee = calculateConstantFee(lastBuyerBetInTresoCurrency);
        Double fees =  percentFee + constantFee;
        fees = applyPromotion(item.owner, fees);
        Double vat = Currency.vatByCountryCode(eBeeCompany.country);
        Double netFees = 0.0;
        if(vat > 0){
            netFees = fees / (1 + vat);
        }else{
            netFees = fees;
        }
        cash += netFees;
        return netFees;
    }
}

public class Buyer  extends User {
    private Double bet;
    List<Item> items;
    public Double buyerEndOfAuction(Item item){
        Double currencyRate = Currency.currencyRate(item.owner.country, eBeeCompany.country, Date.today());
        Double lastBuyerBetInBuyerCurrency = item.lastBuyerBet / currencyRate;
        
        cash -= lastBuyerBetInBuyerCurrency;
        return lastBuyerBetInBuyerCurrency;
    }
}

public class Seller  extends User {
    private String category;
    public Double sellerEndOfAuction(Item item, Double eBeeFees){
        Double netLastBuyerBet = item.lastBuyerBet - eBeeFees;
        cash += netLastBuyerBet;
        return netLastBuyerBet;
    }
    
}
    
