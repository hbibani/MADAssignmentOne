package my.tutorial.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddShoppingListPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    MaterialAutoCompleteTextView autoedittext;
    String shopID;
    ArrayList<String> shopLocationList;
    ArrayAdapter<String> shopAdapater;
    EditText date_time_in;
    String datetime3;
    List<DataShoppingCentre> data3;
    EditText identification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping_list_page);
        identification = findViewById(R.id.id_des);

        //set tool bar and navigation drawer
        toolbar  = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //implement auto set on location
        autoedittext = (MaterialAutoCompleteTextView) findViewById(R.id.auto_text);
        //initialize autoedit text values
        initializeEditList();

        //initialize date and set on click listner
        date_time_in=findViewById(R.id.date_time_input);
        date_time_in.setInputType(InputType.TYPE_NULL);
        datetime3 = "";
        date_time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(date_time_in);
            }
        });

        shopID = " ";

        //set auto edit text  to search for shoplocation
        autoedittext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id)
            {
                int positionactual = 0;
                String selected = (String) parent.getItemAtPosition(position);
                for(int i = 0; i < shopLocationList.size(); i++)
                {
                    //set position of auto-edit text
                    if(shopLocationList.get(i).equals(selected))
                    {
                        positionactual = i;
                        break;
                    }
                }

                shopID = data3.get(positionactual).shopID;
            }
        });

    }


    //Populate the search text here
    private void initializeEditList()
    {
        shopLocationList = new ArrayList<String>();

        //fetch shop list from database to intitalize auto-text
        fetchShopList();

        //get the adapater after it has been fetched and place the list in it
        shopAdapater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, shopLocationList);
        autoedittext.setAdapter(shopAdapater);
    }


    //Fetch shop list from database to initialize auto text
    public void fetchShopList()
    {
        data3 = new ArrayList<>();

        //retrieve information from database
        DataBaseHelper databasehelper = new DataBaseHelper(AddShoppingListPage.this);
        data3 = databasehelper.getAllShoppingCentres();

        //populate the list from the values taken from the database
        for(int i = 0; i< data3.size(); i++)
        {
            String shoppingdetails = data3.get(i).shopName + " " + data3.get(i).location;
            shopLocationList.add(shoppingdetails);
        }
    }

    //add shopping list to the database
    public void addShoppingList(View view)
    {
        DataBaseHelper databasehelper = new DataBaseHelper(AddShoppingListPage.this);
        DataShoppingList shoppinglist = new DataShoppingList();

        //validate items
        boolean validateshop = validateShop(shopID);
        boolean validateid = validateIdentification(identification.getText().toString());
        boolean validatedate = validateDate(datetime3);

        //if validation is correct add the shopping centre to the datbase
        if(validatedate && validateshop && validateid)
        {
            shoppinglist.shoppinglistid = identification.getText().toString();
            shoppinglist.shopid = shopID;
            shoppinglist.dateTime = datetime3;
            if(databasehelper.addShoppingList(shoppinglist))
            {
                startActivity(new Intent(AddShoppingListPage.this, AddShoppingListPage.class));
                Toast.makeText(getApplicationContext(),"Added to the database.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //if it could not be added display toast
                Toast.makeText(getApplicationContext(),"Could not add to the database.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //Navigation item link selections to move to page needed
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.action_shopping_list:
                startActivity(new Intent(AddShoppingListPage.this, HomePage.class));
                break;
            case R.id.action_grocery_list:
                startActivity(new Intent(AddShoppingListPage.this, GroceryListPage.class));
                break;
            case R.id.action_shopping_centre:
                startActivity(new Intent(AddShoppingListPage.this, ShoppingCentrePage.class));
                break;
            case R.id.action_add_shopping:
                startActivity(new Intent(AddShoppingListPage.this, AddShoppingListPage.class));
                break;
            case R.id.action_add_grocery:
                startActivity(new Intent(AddShoppingListPage.this, AddGroceryItem.class));
                break;
            case R.id.add_shopping_centre:
                startActivity(new Intent(AddShoppingListPage.this, AddShoppingCentrePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AddShoppingListPage.this, HomePage.class));
                break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    //show date and time dialog for user to select date and time
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

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                        datetime3 = simpleDateFormat.format(calendar.getTime());
                    }
                };

                new TimePickerDialog(AddShoppingListPage.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(AddShoppingListPage.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    //validate date time string and set input errors if needed
    private boolean validateDate(String s)
    {

        //if is empty set focus
        if(s.isEmpty())
        {
            date_time_in.requestFocus();
            date_time_in.setError("Date cannot be empty.");
            return false;
        }
        else
        {
            date_time_in.requestFocus();
            date_time_in.setError(null);
            return true;
        }
    }

    //validate shop string and place input errors if needed
    private boolean validateShop(String s)
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,50}+";

        //if it is empty or the value does not match regex then set focus on input
        if(s.isEmpty())
        {
            autoedittext.requestFocus();
            autoedittext.setError("Field cannot be empty.");
            return false;
        }
        else if (s.equals(" "))
        {

            autoedittext.setText("");
            autoedittext.requestFocus();
            autoedittext.setError("Pick a valid shop from the list.");
            return false;
        }
        else if(!s.matches(regex ))
        {
            autoedittext.requestFocus();
            autoedittext.setError("Shop name of length 1-50.");
            return false;
        }
        else
        {
            autoedittext.setError(null);
            return true;
        }
    }

    //validate identification and set input errors if needed
    private boolean validateIdentification(String s)
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,50}+";

        //if it is empty or the value does not match regex then set focus on input
        if(s.isEmpty())
        {
            identification.requestFocus();
            identification.setError("Field cannot be empty.");
            return false;
        }
        else if(!s.matches(regex ))
        {
            identification.requestFocus();
            identification.setError("Description of length 1-50.");
            return false;
        }
        else
        {
            identification.setError(null);
            return true;
        }
    }

}