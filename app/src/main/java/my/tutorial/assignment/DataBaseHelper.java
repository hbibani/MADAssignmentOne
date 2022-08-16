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
    public static final String COLUMN_DATETIME_SHOPPINGLISTGROCERY = "COLUMN_DATETIME_SHOPPINGLISTGROCERY";
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
    public DataBaseHelper(@Nullable Context context) {
        super(context, "shoppinglist.db", null, 1);
    }

    //This is called the first time a database is accessed.
    @Override
    public void onCreate(SQLiteDatabase db) {

        //CREATE GROCERY ITEM TABLE
        String createTableStatement = "CREATE TABLE " + GROCERY_ITEM_TABLE + " ( " + COLUMN_GROCERYID + " INTEGER PRIMARY KEY NOT NULL,\n" +
                "  " + COLUMN_DESCRIPTION + " TEXT \n" +
                " , " + COLUMN_DATETIME_GROCERYITEM + " TEXT)";
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
                "  " + COLUMN_SHOPPINGLISTID + " INTEGER, " + COLUMN_GROCERYID + " INTEGER, " + COLUMN_AMOUNT + " TEXT, " + COLUMN_DATETIME_SHOPPINGLISTGROCERY + " TEXT, FOREIGN KEY (" + COLUMN_SHOPPINGLISTID + ") REFERENCES " +  SHOPPING_LIST_TABLE + "(" + COLUMN_SHOPPINGLISTID + " ), \n" +
                " FOREIGN KEY (" + COLUMN_GROCERYID + ") REFERENCES " +  GROCERY_ITEM_TABLE + "(" + COLUMN_GROCERYID + " ) )";
        db.execSQL(createTableStatement);

    }

    //This is called if the database version number changes.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addGroceryItem(DataGroceryList datagrocery)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DESCRIPTION, datagrocery.description);
        cv.put(COLUMN_DATETIME_GROCERYITEM, datagrocery.dateTime);
        long insert = db.insert(GROCERY_ITEM_TABLE, null, cv);
        if(insert == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

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
                DataGroceryList groceryitem = new DataGroceryList(Integer.toString(groceritemid), description, datetime);
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

    public boolean deleteGroceryItem(DataGroceryList dataitem){
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "DELETE FROM " + GROCERY_ITEM_TABLE + " WHERE " + COLUMN_GROCERYID + " = " + dataitem.groceryID;
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst())
        {
            return false;
        }
        else
        {
            return true;
        }

    }


    //Update grocery Details
    public boolean updateGroceryItem(String s1, String s2, String s3)
    {
        boolean isSuccessFul = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_GROCERYID, s1);
        cv.put(COLUMN_DESCRIPTION, s2);
        cv.put(COLUMN_DATETIME_GROCERYITEM, s3);

        try {
            // Execute the update
            db.update(GROCERY_ITEM_TABLE, cv, COLUMN_GROCERYID+"=?", new String[]{s1});
            isSuccessFul = true;
        } catch(Exception e) {

        } finally {
            return isSuccessFul;
        }
    }


    //Get Grocery Item by ID
    public DataGroceryList getGroceryItemById(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + GROCERY_ITEM_TABLE + " WHERE " + COLUMN_GROCERYID + " = " + id;
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst())
        {
            int groceritemid = cursor.getInt(0);
            String description = cursor.getString(1);
            String datetime = cursor.getString(2);
            DataGroceryList groceryitem = new DataGroceryList(Integer.toString(groceritemid), description, datetime);
            return groceryitem;
        }

        return null;
    }


    //---------------------------------------------------------------------------------------------------------------


    //Add shopping centre to the database
    public boolean addShoppingCentre(DataShoppingCentre datashopping)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SHOPNAME, datashopping.shopName);
        cv.put(COLUMN_DATETIME_SHOPPINGCENTRE, datashopping.dateTime);
        cv.put(COLUMN_LOCATION, datashopping.location);
        long insert = db.insert(SHOPPING_CENTRE_TABLE, null, cv);
        if(insert == -1)
        {
            return false;
        }
        else
        {
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


    public boolean deleteShoppingCentre(DataShoppingCentre dataitem)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "DELETE FROM " + SHOPPING_CENTRE_TABLE + " WHERE " + COLUMN_SHOPID+ " = " + dataitem.shopID;
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst())
        {
            return false;
        }
        else
        {
            return true;
        }

    }


    //Get Shopping centre for view page to modify or delete
    public DataShoppingCentre getShoppingCentreById(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + SHOPPING_CENTRE_TABLE + " WHERE " + COLUMN_SHOPID + " = " + id;
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst())
        {
            int shopid = cursor.getInt(0);
            String shopname = cursor.getString(1);
            String location = cursor.getString(2);
            String datetime = cursor.getString(3);
            DataShoppingCentre shoppingcentre = new DataShoppingCentre(Integer.toString(shopid),shopname,location, datetime);
            return shoppingcentre;
        }

        return null;
    }


    //Update shopping centre details
    public boolean updateShoppingCentre(String s1, String s2, String s3, String s4)
    {
        boolean isSuccessFul = false;
        SQLiteDatabase db = this.getWritableDatabase();
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
            return isSuccessFul;
        } finally {
            return isSuccessFul;
        }
    }


    //Shopping list Database executions
    //-----------------------------------------------------------------------------------------------

    //Add shopping list to the database
    public boolean addShoppingList(DataShoppingList datashopping)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SHOPID, datashopping.shopid);
        cv.put(COLUMN_DATETIME_SHOPPINGLIST, datashopping.dateTime);
        long insert = db.insert(SHOPPING_LIST_TABLE, null, cv);
        if(insert == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    public List<DataShoppingList> getAllShoppingLists()
    {
        List<DataShoppingList> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + SHOPPING_LIST_TABLE + " join SHOPPING_CENTRE_TABLE on SHOPPING_CENTRE_TABLE.COLUMN_SHOPID = SHOPPING_LIST_TABLE.COLUMN_SHOPID";
        Cursor cursor = db.rawQuery(queryString,null);


        if(cursor.moveToFirst())
        {
            //loop through the cursor for the results
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

        cursor.close();
        db.close();
        return returnList;
    }

    public boolean deleteShoppingList(DataShoppingList dataitem)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "DELETE FROM " + SHOPPING_LIST_TABLE + " WHERE " + COLUMN_SHOPPINGLISTID+ " = " + dataitem.shoppinglistid;
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst())
        {
            return false;
        }
        else
        {
            return true;
        }

    }



    //----------------------------------------------------------------------------------------------


    //add grocery item to shopping list
    public boolean addGroceryItemToShoppingList(DataGroceryInShopList datashopping)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SHOPPINGLISTID, datashopping.shopListID);
        cv.put(COLUMN_GROCERYID, datashopping.groceryID);
        cv.put(COLUMN_AMOUNT, datashopping.amount);
        cv.put(COLUMN_DATETIME_SHOPPINGLISTGROCERY, datashopping.dateTime);
        long insert = db.insert(SHOPPINGLIST_GROCERY_TABLE, null, cv);
        if(insert == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    //Retrieve all grocery items for list of groceries in adapter
    public List<DataGroceryInShopList> getAllGroceryItemsInShoppingLists()
    {
        List<DataGroceryInShopList> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + SHOPPINGLIST_GROCERY_TABLE + " join GROCERY_ITEM_TABLE on SHOPPINGLIST_GROCERY_TABLE.groceryid = GROCERY_ITEM_TABLE.groceryid";
        Cursor cursor = db.rawQuery(queryString,null);


        if(cursor.moveToFirst())
        {
            //loop through the cursor for the results
            do{
                int shoppinglistgroeryid = cursor.getInt(0);
                int shoplistid = cursor.getInt(1);
                int groceryid = cursor.getInt(2);
                String amount = cursor.getString(3);
                String datetime = cursor.getString(4);
                String description = cursor.getString(6);
                DataGroceryInShopList groceryitem = new DataGroceryInShopList(Integer.toString(shoppinglistgroeryid),Integer.toString(shoplistid ), Integer.toString(groceryid),description ,amount, datetime);
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

    public boolean deleteGroceryItemInShoppingList(DataGroceryInShopList groceryitems)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "DELETE FROM " + SHOPPINGLIST_GROCERY_TABLE + " WHERE " + COLUMN_SHOPPINGLIST_GROCERYID+ " = " + groceryitems.id;
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst())
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    //Modify shop and date in shopping list
    public boolean modifyShopInShoppingList(DataShoppingList shoppinglist)
    {
        boolean isSuccessFul = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SHOPID, shoppinglist.shopid);
        cv.put(COLUMN_DATETIME_SHOPPINGLIST, shoppinglist.dateTime);

        try {
            // Execute the update
            db.update(SHOPPING_LIST_TABLE, cv, COLUMN_SHOPPINGLISTID+"=?", new String[]{shoppinglist.shoppinglistid});
            isSuccessFul = true;
        } catch(Exception e) {
            return isSuccessFul;
        } finally {
            return isSuccessFul;
        }
    }
}
