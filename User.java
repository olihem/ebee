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

    public Double percentFee(Double amount){
        Double percentFee = 0;
        if(amount > 1000 && amount <=2000){
            percentFee = 0.1;
        }
        if(amount > 2000 && amount <=500){
            percentFee = 0.05;
        }
        if(amount > 5000){
            percentFee = 0.01;
        }
        return percentFee;
    }

    public Double constantFee(Dobule amount){
        Double constantFee = 0.0;
        if(amount <= 1000){
             constantFee = 10;
        }
        return constantFee;
    }

    public Double applyPromotion(Seller owner, Double feeAmount){
        Double promotedFees = feeAmount;
        if(owner.category == 'promoted'){
            promotedFees = 0.9 * feeAmount;
        }
        if(owner.category == 'premium'){
            promotedFees = 0.85 * feeAmount;
        }
        if(owner.category == 'platinium'){
            promotedFees = 0.8 * feeAmount;
        }
        return promotedFees;
    }
    
    public Double eBeeEndOfAuction(Item item){
        Double currencyRate = Currency.currencyRate(item.owner.country, eBeeCompany.country, Date.today());
        Double lastBuyerBetInTresoCurrency = item.lastBuyerBet / currencyRate ;
        Double percentFee = this.percentFee(lastBuyerBetInTresoCurrency);
        Double constantFee = this.constantFee(lastBuyerBetInTresoCurrency);
        Double fees = this.applyPromotion(item.owner, percentFee + constantFee);
        Double netFees = fees / (1 + Currency.vatByCountryCode(eBeeCompany.country));
        this.cash += netFees;
        return fees;
    }
}

public class Buyer  extends User {
    private Double bet;
    List<Item> items;
    public Double buyerEndOfAuction(Item item){
        Double currencyRate = Currency.currencyRate(item.owner.country, eBeeCompany.country, Date.today());
        Double lastBuyerBetInBuyerCurrency = item.lastBuyerBet / currencyRate;
        
        this.cash -= lastBuyerBetInBuyerCurrency;
        return lastBuyerBetInBuyerCurrency;
    }
}

public class Seller  extends User {
    private String category;
    public Double sellerEndOfAuction(Item item, Double eBeeFees){
        this.cash += (item.lastBuyerBet - eBeeFees);
    }
    
}
    
