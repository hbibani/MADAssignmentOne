package my.tutorial.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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


    EditText date_time_in;
    MaterialAutoCompleteTextView autoedittext;
    EditText amount;
    String datetime3;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences pref;

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
        date_time_in=findViewById(R.id.date_time_input);
        date_time_in.setInputType(InputType.TYPE_NULL);
        datetime3 = "";
        toolbar  = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        shoppinglistID = intent.getStringExtra("shopid");
        amount = findViewById(R.id.amount_grocery);
        autoedittext = (MaterialAutoCompleteTextView) findViewById(R.id.auto_text);
        initializeEditList();


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

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        date_time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(date_time_in);
            }
        });

        fetchGroceryDetails2();
    }

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
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(ViewShoppingListPage.this,  MainActivity.class));
            }
            break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }




    private void showDateTimeDialog(final EditText date_time_in)
    {
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                        datetime3 = simpleDateFormat.format(calendar.getTime());
                    }
                };

                new TimePickerDialog(ViewShoppingListPage.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(ViewShoppingListPage.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    //Add Grocery Item to shopping list
    public void addGroceryItem(View view)
    {
        DataBaseHelper databasehelper = new DataBaseHelper(ViewShoppingListPage.this);
        DataGroceryInShopList datagroceryitem = new DataGroceryInShopList();
        datagroceryitem.shopListID= shoppinglistID;
        datagroceryitem.groceryID = groceryID;
        datagroceryitem.amount = amount.getText().toString();
        datagroceryitem.dateTime = datetime3;

        if(databasehelper.addGroceryItemToShoppingList(datagroceryitem))
        {
            Toast.makeText(getApplicationContext(),"Added to the database.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ViewShoppingListPage.this, ViewShoppingListPage.class);
            intent.putExtra("shopid", shoppinglistID );
            ViewShoppingListPage.this.startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Could not add to the database.", Toast.LENGTH_SHORT).show();
        }
    }


    //Get Grocery item details for recycler view to view the grocery list for that page
    public void fetchGroceryDetails2() {

        DataBaseHelper databasehelper = new DataBaseHelper(ViewShoppingListPage.this);
        data3 = new ArrayList<>();
        data3.clear();
        data3 = databasehelper.getAllGroceryItemsInShoppingLists();
        mRVGrocery = (RecyclerView) findViewById(R.id.admissionList2);
        mAdapter = new AdapterGroceryInShoplist(ViewShoppingListPage.this, data3);
        mRVGrocery.setAdapter(mAdapter);
        mRVGrocery.setLayoutManager(new LinearLayoutManager(ViewShoppingListPage.this));
    }
}