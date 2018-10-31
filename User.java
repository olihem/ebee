package ebee.auction;
    

public abstract class User {
    private Double cash;
    private String country;
}

public class eBee  extends User {

    private static eBee self;
    private eBee(){
        
    }

    public static eBee company(){
        if(self == null){
            self = new eBee();
        }
        return self;
    }

    public Double calculatePercentFee(Double amount){
        if(amount <= 1000){
            return 0;
        }
        if(amount > 1000 && amount <= 2000){
            return (10/100) * amount;
        }
        if(amount > 2000 && amount <= 5000){
            return (5/100) * amount;
        }
        if(amount > 5000){
            return (1/100) * amount;
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
            return (90/100) * feeAmount;
        }
        if(owner.category == "premium"){
            return (85/100) * feeAmount;
        }
        if(owner.category == "platinium"){
            return (80/100) * feeAmount;
        }
        return feeAmount;
    }
    
    public Double eBeeEndOfAuction(Item item){
        //Double lastBuyerBetInTresoCurrency = item.lastBuyerBet / Currency.currencyRate(item.owner.country, country, Date.today());
        
        Double sellerCountryVat = Currency.vatByCountryCode(item.owner.country);
        Double amount;
        if(sellerCountryVat > 0){
            amount = item.lastBuyerBet / (1 + sellerCountryVat);
        }
        else{
            amount = item.lastBuyerBet;
        }
        Double fees = calculatePercentFee(amount) + calculateConstantFee(amount);
        fees = applyPromotion(item.owner, fees);
        
        amount = amount - fees;
        cash += fees;
        return amount;
    }
}

public class Buyer  extends User {
    private Double bet;
    List<Item> items;
    public Double buyerEndOfAuction(Item item){
        
        cash -= item.lastBuyerBet;
        return item.lastBuyerBet;
    }
}

public class Seller  extends User {
    private String category;
    public Double sellerEndOfAuction(Item item){
        double sellerRevenue = eBee.company().eBeeEndOfAuction(item);
        cash += sellerRevenue;
        return sellerRevenue;
    }
    
}
    
