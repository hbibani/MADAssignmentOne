package my.tutorial.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ViewShoppingListPage extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{


    MaterialAutoCompleteTextView autoedittext;
    EditText amount;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private RecyclerView mRVGrocery;
    private AdapterGroceryInShoplist mAdapter;
    List<DataGroceryInShopList> data3;
    List<DataGroceryList> data4;
    String groceryID;
    ArrayList<String> groceryItemList;
    ArrayAdapter<String> shopAdapater;
    String shoppinglistID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shopping_list_page);

        //retrieve the id from the itent so as  to view the grocery item and set widgets with ids

        //set tool bar for drawer layout
        toolbar  = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        shoppinglistID = intent.getStringExtra("shopinglistid");
        amount = findViewById(R.id.amount_grocery);
        autoedittext = (MaterialAutoCompleteTextView) findViewById(R.id.auto_text);
        initializeEditList();

        groceryID = " ";
        //set auto-edit text set on item click listener
        autoedittext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id)
            {
                int positionactual = 0;
                String selected = (String) parent.getItemAtPosition(position);
                for(int i = 0; i < groceryItemList.size(); i++)
                {
                    if(groceryItemList.get(i).equals(selected))
                    {
                        positionactual = i;
                        break;
                    }
                }

                groceryID = data4.get(positionactual).groceryID;
            }
        });

        //get the drawer layout functions
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //Fetch grocery details for the list
        fetchGroceryDetails2();
    }

    //initialize search for shop function
    private void initializeEditList()
    {
        groceryItemList = new ArrayList<String>();
        fetchGroceryList();
        shopAdapater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, groceryItemList);
        autoedittext.setAdapter(shopAdapater);
    }

    //Fetch grocery list from database to initialize auto text
    public void fetchGroceryList()
    {
        data4 = new ArrayList<>();
        DataBaseHelper databasehelper = new DataBaseHelper(ViewShoppingListPage.this);
        data4 = databasehelper.getAllGroceryItems();

        for(int i = 0; i< data4.size(); i++)
        {
            String shoppingdetails = data4.get(i).groceryID + " " + data4.get(i).description;
            groceryItemList.add(shoppingdetails);
        }
    }

    //set navigation links for movement from page to page for the navigation drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.action_shopping_list:
                startActivity(new Intent(ViewShoppingListPage.this, HomePage.class));
                break;
            case R.id.action_grocery_list:
                startActivity(new Intent(ViewShoppingListPage.this,GroceryListPage.class));
                break;
            case R.id.action_shopping_centre:
                startActivity(new Intent(ViewShoppingListPage.this, ShoppingCentrePage.class));
                break;
            case R.id.action_add_shopping:
                startActivity(new Intent(ViewShoppingListPage.this, AddShoppingListPage.class));
                break;
            case R.id.action_add_grocery:
                startActivity(new Intent(ViewShoppingListPage.this, AddGroceryItem.class));
                break;
            case R.id.add_shopping_centre:
                startActivity(new Intent(ViewShoppingListPage.this, AddShoppingCentrePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(ViewShoppingListPage.this, HomePage.class));
                break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }


    //Add Grocery Item to shopping list
    public void addGroceryItem(View view)
    {
        //Create database helper
        DataBaseHelper databasehelper = new DataBaseHelper(ViewShoppingListPage.this);
        DataGroceryInShopList datagroceryitem = new DataGroceryInShopList();
        boolean validategroceryid = validateGroceryID(groceryID);
        boolean validateamount = validateAmount(amount.getText().toString());

        //validate the amount
        if(validateamount && validategroceryid)
        {
            datagroceryitem.shopListID= shoppinglistID;
            datagroceryitem.groceryID = groceryID;
            datagroceryitem.amount = amount.getText().toString();
            datagroceryitem.grocerychecked = 0;

            if(databasehelper.addGroceryItemToShoppingList(datagroceryitem))
            {
                Toast.makeText(getApplicationContext(),"Added to the database.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ViewShoppingListPage.this, ViewShoppingListPage.class);
                intent.putExtra("shopinglistid", shoppinglistID );
                ViewShoppingListPage.this.startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Could not add to the database.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //Get Grocery item details for recycler view to view the grocery list for that page
    public void fetchGroceryDetails2() {

        //Open database helper and retrieve all the grocery items
        DataBaseHelper databasehelper = new DataBaseHelper(ViewShoppingListPage.this);
        data3 = new ArrayList<>();
        data3.clear();
        data3 = databasehelper.getAllGroceryItemsInShoppingLists(shoppinglistID);
        mRVGrocery = (RecyclerView) findViewById(R.id.admissionList2);
        mAdapter = new AdapterGroceryInShoplist(ViewShoppingListPage.this, data3);
        mRVGrocery.setAdapter(mAdapter);
        mRVGrocery.setLayoutManager(new LinearLayoutManager(ViewShoppingListPage.this));
    }


    // validate grocery and set-input errors if needed
    private boolean validateGroceryID(String s)
    {
        //check string is equal to the beginning
        if(s.equals(" "))
        {
            autoedittext.setText("");
            autoedittext.requestFocus();
            autoedittext.setError("Pick a grocery from the list.");
            return false;
        }
        else if(s.isEmpty()) //check if empty
        {
            autoedittext.setText("");
            autoedittext.requestFocus();
            autoedittext.setError("Pick a grocery from the list.");
            return false;
        }
        else
        {
            autoedittext.setError(null);
            return true;
        }
    }


    //validate amount and set input errors
    private boolean validateAmount(String s)
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,50}+";
        //check if empty
        if(s.isEmpty())
        {
            amount.requestFocus();
            amount.setError("Field cannot be empty.");
            return false;
        }
        else if(!s.matches(regex )) //check to match regex
        {
            amount.requestFocus();
            amount.setError("Amount of length 1-50.");
            return false;
        }
        else
        {
            amount.setError(null);
            return true;
        }
    }

    //delete grocery item and show dialog before deleting
    public void deleteGroceryItem(View view)
    {
        //Present Alert Dialog box
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(view.getContext())
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_baseline_delete_forever_24)

                .setPositiveButton("Delete", (dialog, whichButton) -> {
                    deleteShoppingItem();
                    dialog.dismiss();
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        myQuittingDialogBox.show();
    }

    //delete shopping items from shopping list
    private void deleteShoppingItem()
    {
        boolean test = false;

        //check to see if any items need to be deleted
        for(int i = 0; i < data3.size(); i++)
        {
            if(data3.get(i).deleteitemchecked)
            {
                test = true;
                break;
            }
        }

        //if deleted items are in array delete the items
        if(test)
        {
            for(int i = 0; i < data3.size(); i++)
            {
                if(data3.get(i).deleteitemchecked == true)
                {
                    DataGroceryInShopList groceryitems = data3.get(i);
                    DataBaseHelper databasehelper = new DataBaseHelper(ViewShoppingListPage.this);
                    databasehelper.deleteGroceryItemInShoppingList(groceryitems);
                }
            }
            Intent intent = new Intent(ViewShoppingListPage.this, ViewShoppingListPage.class);
            intent.putExtra("shopinglistid", shoppinglistID );
            ViewShoppingListPage.this.startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"There are no items to delete.", Toast.LENGTH_SHORT).show();
        }

    }
}