package my.tutorial.assignment;

public class DataShoppingList {

    //attributes
    public String shoppinglistid;
    public String shopid;
    public String dateTime;


    //Not stored in database
    public String shopName;
    public String location;
    public boolean deletecheck;
    public boolean present;


    //datashopping list constructor
    public DataShoppingList(String shoppinglistid, String shopid, String dateTime, String shopName, String location) {
        this.shoppinglistid = shoppinglistid;
        this.shopid = shopid;
        this.dateTime = dateTime;
        this.shopName = shopName;
        this.location = location;
        this.deletecheck = false;
        this.present = false;
    }


    //getters and setters
    public String getShopName()
    {
        return shopName;
    }

    public String getLocation() {
        return location;
    }
    public String getShoppinglistid(){return this.shoppinglistid;}
    public String getShopid(){return this.shopid;}
    public String getDateTime(){
        return this.dateTime;
    }


    //default constructor
    public DataShoppingList()
    {

    }

}
