package my.tutorial.assignment;

public class DataGroceryList {

    // Attributes of datagrocery list
    public String groceryID;
    public String description;
    public String dateTime;
    public byte[] img;
    public boolean deletecheck;


    //getters and setters
    public String getGroceryID(){return this.groceryID;}
    public String getDescription(){
        return this.description;
    }
    public String getDateTime(){
        return this.dateTime;
    }


    //default constructor
    public DataGroceryList() {
    }

    //General consttuctor
    public DataGroceryList(String groceryID, String description, String dateTime, byte[] img) {
        this.groceryID = groceryID;
        this.description = description;
        this.dateTime = dateTime;
        this.img = img;
        this.deletecheck = false;
    }
}
