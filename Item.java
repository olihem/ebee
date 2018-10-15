package ebee.auction;

public class Item {
    private Double startPrice;
    private Double lastBuyerBet;
    private Seller owner;

    public Item(Seller owner){
        this.owner = owner;
    }
}


