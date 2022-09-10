package my.tutorial.assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {


    public static final String GROCERY_ITEM_TABLE = "GROCERY_ITEM_TABLE";
    public static final String COLUMN_DESCRIPTION = "COLUMN_DESCRIPTION";
    public static final String COLUMN_DATETIME_GROCERYITEM = "COLUMN_DATETIME_GROCERYITEM";
    public static final String COLUMN_DATETIME_SHOPPINGCENTRE = "COLUMN_DATETIME_SHOPPINGCENTRE";
    public static final String COLUMN_DATETIME_SHOPPINGLIST = "COLUMN_DATETIME_SHOPPINGLIST";
    public static final String COLUMN_GROCERYID = "groceryid";
    public static final String SHOPPING_CENTRE_TABLE = "SHOPPING_CENTRE_TABLE";
    public static final String COLUMN_SHOPID = "COLUMN_SHOPID";
    public static final String COLUMN_SHOPNAME = "COLUMN_SHOPNAME";
    public static final String COLUMN_LOCATION = "COLUMN_LOCATION";
    public static final String SHOPPING_LIST_TABLE = "SHOPPING_LIST_TABLE";
    public static final String COLUMN_SHOPPINGLISTID = "COLUMN_SHOPPINGLISTID";
    public static final String SHOPPINGLIST_GROCERY_TABLE = "SHOPPINGLIST_GROCERY_TABLE";
    public static final String COLUMN_SHOPPINGLIST_GROCERYID = "COLUMN_SHOPPINGLIST_GROCERYID";
    public static final String COLUMN_AMOUNT = "COLUMN_AMOUNT";
    public static final String COLUMN_GROCERYCHECKED = "COLUMN_GROCERYCHECKED";
    public static final String COLUMN_IMAGE = "COLUMN_IMAGE";
    public DataBaseHelper(@Nullable Context context) {
        super(context, "shoppinglistdataperfectperfect.db", null, 2);
    }

    //This is called the first time a database is accessed.
    @Override
    public void onCreate(SQLiteDatabase db) {

        //CREATE GROCERY ITEM TABLE
        String createTableStatement = "CREATE TABLE " + GROCERY_ITEM_TABLE + " ( " + COLUMN_GROCERYID + " INTEGER PRIMARY KEY NOT NULL,\n" +
                "  " + COLUMN_DESCRIPTION + " TEXT \n" +
                " , " + COLUMN_DATETIME_GROCERYITEM + " TEXT, " + COLUMN_IMAGE + " BLOB)";
        db.execSQL(createTableStatement);

        //CREATE SHOPPING CENTRE TABLE
        createTableStatement = "CREATE TABLE " + SHOPPING_CENTRE_TABLE + " ( " + COLUMN_SHOPID + " INTEGER PRIMARY KEY NOT NULL,\n" +
                "  " + COLUMN_SHOPNAME + " TEXT, " + COLUMN_LOCATION + " TEXT, " + COLUMN_DATETIME_SHOPPINGCENTRE + " TEXT)";
        db.execSQL(createTableStatement);

        //CREATE SHOPPING LIST TABLE
        createTableStatement = "CREATE TABLE " + SHOPPING_LIST_TABLE + " ( " + COLUMN_SHOPPINGLISTID + " INTEGER PRIMARY KEY NOT NULL,\n" +
                "  " + COLUMN_SHOPID + " INTEGER, " + COLUMN_DATETIME_SHOPPINGLIST + " TEXT, FOREIGN KEY (" + COLUMN_SHOPID + ") REFERENCES " +  SHOPPING_CENTRE_TABLE + "(" + COLUMN_SHOPID + " ))";
        db.execSQL(createTableStatement);

        //CREATE SHOPPING LIST GROCERY ITEM TABLE
        createTableStatement = "CREATE TABLE " + SHOPPINGLIST_GROCERY_TABLE + " ( " + COLUMN_SHOPPINGLIST_GROCERYID + " INTEGER PRIMARY KEY NOT NULL,\n" +
                "  " + COLUMN_SHOPPINGLISTID + " INTEGER, " + COLUMN_GROCERYID + " INTEGER, " + COLUMN_AMOUNT + " TEXT, " + COLUMN_GROCERYCHECKED + " INTEGER, " + "  FOREIGN KEY (" + COLUMN_SHOPPINGLISTID + ") REFERENCES " +  SHOPPING_LIST_TABLE + "(" + COLUMN_SHOPPINGLISTID + " ), \n" +
                " FOREIGN KEY (" + COLUMN_GROCERYID + ") REFERENCES " +  GROCERY_ITEM_TABLE + "(" + COLUMN_GROCERYID + " ) )";
        db.execSQL(createTableStatement);

    }

    //This is called if the database version number changes.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //add grocery item to database
    public boolean addGroceryItem(DataGroceryList datagrocery)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_GROCERYID, datagrocery.groceryID);
        cv.put(COLUMN_DESCRIPTION, datagrocery.description);
        cv.put(COLUMN_DATETIME_GROCERYITEM, datagrocery.dateTime);
        cv.put(COLUMN_IMAGE, datagrocery.img);
        long insert = db.insert(GROCERY_ITEM_TABLE, null, cv);
        if(insert == -1)
        {
            db.close();
            return false;
        }
        else
        {
            db.close();
            return true;
        }
    }

    //get all grocery items to display in a list
    public List<DataGroceryList> getAllGroceryItems(){
        List<DataGroceryList> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + GROCERY_ITEM_TABLE;
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst())
        {
            //loop through the cursor for the results
            do{
                int groceritemid = cursor.getInt(0);
                String description = cursor.getString(1);
                String datetime = cursor.getString(2);
                byte[] img = cursor.getBlob(3);
                DataGroceryList groceryitem = new DataGroceryList(Integer.toString(groceritemid), description, datetime,img);
                returnList.add(groceryitem);
            }while(cursor.moveToNext());
        }
        else
        {
            // nothing will be added to list
        }

        cursor.close();
        db.close();
        return returnList;
    }

    //delete grocery item
    public boolean deleteGroceryItem(DataGroceryList dataitem){
        SQLiteDatabase db = this.getReadableDatabase();

        //delete all entries of the grocery in a shopping list
        String queryString1 = "DELETE FROM " + SHOPPINGLIST_GROCERY_TABLE + " WHERE " + COLUMN_GROCERYID + " = " + dataitem.groceryID;
        Cursor cursor1 = db.rawQuery(queryString1, null);
        if(cursor1.moveToFirst())
        {
            cursor1.close();
            db.close();
            return false;
        }

        //finally delete the grocery item
        String queryString = "DELETE FROM " + GROCERY_ITEM_TABLE + " WHERE " + COLUMN_GROCERYID + " = " + dataitem.groceryID;
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst())
        {
            cursor1.close();
            cursor.close();
            db.close();
            return false;
        }
        else
        {
            cursor1.close();
            cursor.close();
            db.close();
            return true;
        }

    }


    //Update grocery Details
    public boolean updateGroceryItem(String s1, String s2, String s3, byte[] img, String s)
    {
        boolean isSuccessFul = false;
        SQLiteDatabase db = this.getWritableDatabase();
        //set content values for update using the strings specified in the function
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_GROCERYID, s);
        cv.put(COLUMN_DESCRIPTION, s2);
        cv.put(COLUMN_DATETIME_GROCERYITEM, s3);
        cv.put(COLUMN_IMAGE, img);

        try {
            // Execute the update
            db.update(GROCERY_ITEM_TABLE, cv, COLUMN_GROCERYID+"=?", new String[]{s1});
            ContentValues cv1 = new ContentValues();
            cv1.put(COLUMN_GROCERYID, s);
            db.update(SHOPPINGLIST_GROCERY_TABLE, cv1, COLUMN_GROCERYID+"=?", new String[]{s1});
            isSuccessFul = true;
        } catch(Exception e) {
            db.close();
            return isSuccessFul;
        } finally {
            db.close();
            return isSuccessFul;
        }
    }


    //Get Grocery Item by ID
    public DataGroceryList getGroceryItemById(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        //return grocery item by searching the id in the database, and return the one which matches the query
        String queryString = "SELECT * FROM " + GROCERY_ITEM_TABLE + " WHERE " + COLUMN_GROCERYID + " = " + id;
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst())
        {
            int groceritemid = cursor.getInt(0);
            String description = cursor.getString(1);
            String datetime = cursor.getString(2);
            byte[] img = cursor.getBlob(3);

            //return item
            DataGroceryList groceryitem = new DataGroceryList(Integer.toString(groceritemid), description, datetime, img);
            cursor.close();
            db.close();
            return groceryitem;
        }


        //close database
        cursor.close();
        db.close();
        return null;
    }


    //---------------------------------------------------------------------------------------------------------------


    //Add shopping centre to the database
    public boolean addShoppingCentre(DataShoppingCentre datashopping)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //Get the content values from the inputs
        cv.put(COLUMN_SHOPNAME, datashopping.shopName);
        cv.put(COLUMN_DATETIME_SHOPPINGCENTRE, datashopping.dateTime);
        cv.put(COLUMN_LOCATION, datashopping.location);

        //add this to the database
        long insert = db.insert(SHOPPING_CENTRE_TABLE, null, cv);
        if(insert == -1)
        {
            db.close();
            return false;
        }
        else
        {
            db.close();
            return true;
        }
    }



    //Return shopping centre lists to the list page
    public List<DataShoppingCentre> getAllShoppingCentres(){
        List<DataShoppingCentre> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + SHOPPING_CENTRE_TABLE;
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst())
        {
            //loop through the cursor for the results
            do{
                int shopid = cursor.getInt(0);
                String shopanme = cursor.getString(1);
                String location = cursor.getString(2);
                String datetime = cursor.getString(3);
                DataShoppingCentre shoppincentre = new DataShoppingCentre(Integer.toString(shopid),shopanme,location, datetime);
                returnList.add(shoppincentre);
            }while(cursor.moveToNext());
        }
        else
        {
            // nothing will be added to list
        }

        cursor.close();
        db.close();
        return returnList;
    }

    //delete shopping centre from the database
    public boolean deleteShoppingCentre(DataShoppingCentre dataitem)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        //delete all the items in the grocery list associated with that shop
        String queryString2 = "DELETE FROM " + SHOPPINGLIST_GROCERY_TABLE + " WHERE COLUMN_SHOPPINGLISTID IN ( SELECT COLUMN_SHOPPINGLISTID FROM SHOPPING_LIST_TABLE WHERE COLUMN_SHOPID = " + dataitem.shopID + " )";
        Cursor cursor3 = db.rawQuery(queryString2, null);
        if(cursor3.moveToFirst())
        {
            cursor3.close();
            db.close();
            return false;
        }

        //delete shopping list that have that have shop id
        String queryString1 = "DELETE FROM " + SHOPPING_LIST_TABLE + " WHERE " + COLUMN_SHOPID+ " = " + dataitem.shopID;
        Cursor cursor2 = db.rawQuery(queryString1, null);
        if(cursor2.moveToFirst())
        {
            cursor3.close();
            cursor2.close();
            db.close();
            return false;
        }

        //finally delete shop
        String queryString = "DELETE FROM " + SHOPPING_CENTRE_TABLE + " WHERE " + COLUMN_SHOPID+ " = " + dataitem.shopID;
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst())
        {
            cursor3.close();
            cursor2.close();
            cursor.close();
            db.close();
            return false;
        }
        else
        {
            cursor3.close();
            cursor2.close();
            cursor.close();
            db.close();
            return true;
        }

    }


    //Get Shopping centre for view page to modify or delete
    public DataShoppingCentre getShoppingCentreById(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        //get the shopping centre table using the shopid
        String queryString = "SELECT * FROM " + SHOPPING_CENTRE_TABLE + " WHERE " + COLUMN_SHOPID + " = " + id;
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst())
        {
            int shopid = cursor.getInt(0);
            String shopname = cursor.getString(1);
            String location = cursor.getString(2);
            String datetime = cursor.getString(3);

            //return the values from the shopping centre using datashoppingcentre class
            DataShoppingCentre shoppingcentre = new DataShoppingCentre(Integer.toString(shopid),shopname,location, datetime);
            cursor.close();
            db.close();
            return shoppingcentre;
        }


        cursor.close();
        db.close();
        return null;
    }


    //Update shopping centre details
    public boolean updateShoppingCentre(String s1, String s2, String s3, String s4)
    {
        boolean isSuccessFul = false;
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values and update them using the strings as input
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SHOPID, s1);
        cv.put(COLUMN_SHOPNAME, s2);
        cv.put(COLUMN_LOCATION, s3);
        cv.put(COLUMN_DATETIME_SHOPPINGCENTRE, s4);

        try {
            // Execute the update
            db.update(SHOPPING_CENTRE_TABLE, cv, COLUMN_SHOPID+"=?", new String[]{s1});
            isSuccessFul = true;
        } catch(Exception e) {
            db.close();
            return isSuccessFul;
        } finally {
            db.close();
            return isSuccessFul;
        }
    }


    //Shopping list Database executions
    //-----------------------------------------------------------------------------------------------

    //Add shopping list to the database
    public boolean addShoppingList(DataShoppingList datashopping)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values from the data input and insert the shopping list into datbase
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SHOPPINGLISTID, datashopping.shoppinglistid);
        cv.put(COLUMN_SHOPID, datashopping.shopid);
        cv.put(COLUMN_DATETIME_SHOPPINGLIST, datashopping.dateTime);
        long insert = db.insert(SHOPPING_LIST_TABLE, null, cv);
        if(insert == -1)
        {
            db.close();
            return false;
        }
        else
        {
            db.close();
            return true;
        }
    }

    //retrieve all shopping lists for the display of the results
    public List<DataShoppingList> getAllShoppingLists()
    {
        List<DataShoppingList> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //Retrieve the shopping list and join them to the other tables
        String queryString = "SELECT * FROM " + SHOPPING_LIST_TABLE + " join SHOPPING_CENTRE_TABLE on SHOPPING_CENTRE_TABLE.COLUMN_SHOPID = SHOPPING_LIST_TABLE.COLUMN_SHOPID";
        Cursor cursor = db.rawQuery(queryString,null);


        if(cursor.moveToFirst())
        {
            //loop through the cursor for the results and send them to the return list
            do{
                int shoplistid = cursor.getInt(0);
                int shopid = cursor.getInt(1);
                String datetime = cursor.getString(2);
                String shopname = cursor.getString(4);
                String location = cursor.getString(5);
                DataShoppingList shoppincentre = new DataShoppingList(Integer.toString(shoplistid ), Integer.toString(shopid),datetime ,shopname, location);
                returnList.add(shoppincentre);
            }while(cursor.moveToNext());
        }
        else
        {
            // nothing will be added to list
        }

        //return list is sent back
        cursor.close();
        db.close();
        return returnList;
    }

    //delete shopping list
    public boolean deleteShoppingList(DataShoppingList dataitem)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        //delete all the grocery items associated with that shopping list
        String queryString1 = "DELETE FROM " + SHOPPINGLIST_GROCERY_TABLE + " WHERE " + COLUMN_SHOPPINGLISTID+ " = " + dataitem.shoppinglistid;
        Cursor cursor1 = db.rawQuery(queryString1, null);
        if(cursor1.moveToFirst())
        {
            return false;
        }

        //delete the shopping list next
        String queryString = "DELETE FROM " + SHOPPING_LIST_TABLE + " WHERE " + COLUMN_SHOPPINGLISTID+ " = " + dataitem.shoppinglistid;
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst())
        {
            cursor1.close();
            cursor.close();
            db.close();
            return false;
        }
        else
        {
            cursor1.close();
            cursor.close();
            db.close();
            return true;
        }

    }



    //----------------------------------------------------------------------------------------------


    //add grocery item to shopping list
    public boolean addGroceryItemToShoppingList(DataGroceryInShopList datashopping)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values and place the information from the datagroceryinshoplist class and insert into database
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SHOPPINGLISTID, datashopping.shopListID);
        cv.put(COLUMN_GROCERYID, datashopping.groceryID);
        cv.put(COLUMN_AMOUNT, datashopping.amount);
        cv.put(COLUMN_GROCERYCHECKED, datashopping.grocerychecked);
        long insert = db.insert(SHOPPINGLIST_GROCERY_TABLE, null, cv);
        if(insert == -1)
        {

            db.close();
            return false;
        }
        else
        {
            db.close();
            return true;
        }
    }


    //Retrieve all grocery items for list of groceries in adapter
    public List<DataGroceryInShopList> getAllGroceryItemsInShoppingLists(String s)
    {
        List<DataGroceryInShopList> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //get all grocery items in the shopping list
        String queryString = "SELECT * FROM " + SHOPPINGLIST_GROCERY_TABLE + " join GROCERY_ITEM_TABLE on SHOPPINGLIST_GROCERY_TABLE.groceryid = GROCERY_ITEM_TABLE.groceryid WHERE " + COLUMN_SHOPPINGLISTID+ " = " + s;
        Cursor cursor = db.rawQuery(queryString,null);


        if(cursor.moveToFirst())
        {
            //loop through the cursor for the results
            do{
                int shoppinglistgroeryid = cursor.getInt(0);
                int shoplistid = cursor.getInt(1);
                int groceryid = cursor.getInt(2);
                String amount = cursor.getString(3);
                int checked = cursor.getInt(4);
                String description = cursor.getString(6);
                String datetime = cursor.getString(7);
                byte[] img = cursor.getBlob(8);
                //get each item and put in the list
                DataGroceryInShopList groceryitem = new DataGroceryInShopList(Integer.toString(shoppinglistgroeryid),Integer.toString(shoplistid ), Integer.toString(groceryid),description ,amount, datetime,checked, img);
                returnList.add(groceryitem);
            }while(cursor.moveToNext());
        }
        else
        {
            // nothing will be added to list
        }

        cursor.close();
        db.close();
        return returnList;
    }

    //delete grocery item in a shopping list
    public boolean deleteGroceryItemInShoppingList(DataGroceryInShopList groceryitems)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "DELETE FROM " + SHOPPINGLIST_GROCERY_TABLE + " WHERE " + COLUMN_SHOPPINGLIST_GROCERYID+ " = " + groceryitems.id;
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst())
        {
            cursor.close();
            db.close();
            return false;
        }
        else
        {
            cursor.close();
            db.close();
            return true;
        }


    }

    //Modify shop and date in shopping list
    public boolean modifyShopInShoppingList(DataShoppingList shoppinglist, String shoppinglistid)
    {
        boolean isSuccessFul = false;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SHOPPINGLISTID, shoppinglist.shoppinglistid);
        cv.put(COLUMN_SHOPID, shoppinglist.shopid);
        cv.put(COLUMN_DATETIME_SHOPPINGLIST, shoppinglist.dateTime);

        try {
            // Execute the update
            db.update(SHOPPING_LIST_TABLE, cv, COLUMN_SHOPPINGLISTID+"=?", new String[]{shoppinglistid});
            ContentValues cv1 = new ContentValues();
            cv1.put(COLUMN_SHOPPINGLISTID, shoppinglist.shoppinglistid);
            db.update(SHOPPINGLIST_GROCERY_TABLE, cv1, COLUMN_SHOPPINGLISTID+"=?", new String[]{shoppinglistid});
            isSuccessFul = true;
        } catch(Exception e) {
            db.close();
            return isSuccessFul;
        } finally {
            db.close();
            return isSuccessFul;
        }
    }

    //check item to say it has been taken in grocery table
    public boolean checkTheGroceryIteam(String s1,int i)
    {
        boolean isSuccessFul = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_GROCERYCHECKED, i);

        try {
            // Execute the update
            db.update(SHOPPINGLIST_GROCERY_TABLE, cv, COLUMN_SHOPPINGLIST_GROCERYID+"=?", new String[]{s1});
            isSuccessFul = true;
        } catch(Exception e) {
            db.close();
            return isSuccessFul;
        } finally {
            db.close();
            return isSuccessFul;
        }

    }

    public DataShoppingList getShoppingListItemById(String shoppinglistid)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        //return grocery item by searching the id in the database, and return the one which matches the query
        String queryString = "SELECT * FROM " + SHOPPING_LIST_TABLE + " WHERE " + COLUMN_SHOPPINGLISTID + " = " + shoppinglistid;
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst())
        {
            int id = cursor.getInt(0);
            int shopid = cursor.getInt(1);
            String datetime = cursor.getString(2);

            //return item
            DataShoppingList item = new DataShoppingList(Integer.toString(id), Integer.toString(shopid), datetime);
            cursor.close();
            db.close();
            return item;
        }


        //close database
        cursor.close();
        db.close();
        return null;
    }
}
