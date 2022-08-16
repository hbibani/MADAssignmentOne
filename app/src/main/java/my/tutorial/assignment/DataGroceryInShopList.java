package my.tutorial.assignment;

public class DataGroceryInShopList {

    public String id;
    public String shopListID;
    public String groceryID;
    public String description;
    public String amount;
    public String dateTime;

    public DataGroceryInShopList() {
    }

    public DataGroceryInShopList(String id2,String shopListID, String groceryID, String description, String amount, String dateTime) {
        this.id = id2;
        this.shopListID = shopListID;
        this.groceryID = groceryID;
        this.description = description;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    public String getId(){return this.id;}
    public String getShopListID(){
        return this.shopListID;
    }
    public String getGroceryID(){return this.groceryID;}
    public String getDescription(){
        return this.description;
    }
    public String getDateTime(){
        return this.dateTime;
    }
    public String getAmount() {return this.amount;}



    @Override
    public String toString() {
        return "DataGroceryInShopList{" +
                "id='" + id + '\'' +
                ", shopListID='" + shopListID + '\'' +
                ", groceryID='" + groceryID + '\'' +
                ", description='" + description + '\'' +
                ", amount='" + amount + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
