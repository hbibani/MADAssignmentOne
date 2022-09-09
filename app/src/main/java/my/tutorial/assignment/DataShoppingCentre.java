package my.tutorial.assignment;

public class DataShoppingCentre {


    //Attributes of datashopping centre
    public String shopID;
    public String shopName;
    public String location;
    public String dateTime;


    //getters and setters
    public String getShoppingID(){return this.shopID;}
    public String getShopName(){
        return this.shopName;}
    public String getShopID(){ return this.shopID;}
    public String getLocation(){return this.location;}
    public String getDateTime(){
        return this.dateTime;
    }


    //General constructor
    public DataShoppingCentre(String shopID, String shopName, String location, String dateTime) {
        this.shopID = shopID;
        this.shopName = shopName;
        this.location = location;
        this.dateTime = dateTime;
    }

    //default constructor
    public DataShoppingCentre() {
    }
    
}
