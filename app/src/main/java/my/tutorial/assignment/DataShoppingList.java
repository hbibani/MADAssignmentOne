package my.tutorial.assignment;

public class DataShoppingList {

    public String shoppinglistid;
    public String shopid;
    public String dateTime;


    //Not stored in database
    public String shopName;
    public String location;
    public boolean deletecheck;
    public boolean present;


    public DataShoppingList(String shoppinglistid, String shopid, String dateTime, String shopName, String location) {
        this.shoppinglistid = shoppinglistid;
        this.shopid = shopid;
        this.dateTime = dateTime;
        this.shopName = shopName;
        this.location = location;
        this.deletecheck = false;
        this.present = false;
    }

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


    public DataShoppingList()
    {

    }

}
