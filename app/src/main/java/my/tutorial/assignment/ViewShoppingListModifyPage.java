package my.tutorial.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ViewShoppingListModifyPage extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

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
    String shopid;
    String shoppinglistid;
    EditText identification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shopping_list_modify_page);

        Intent intent = getIntent();

        //retrieve information from intent to start page activity
        shoppinglistid  = intent.getStringExtra("shoplistid");
        getShoppingListInformation();
        identification = findViewById(R.id.id_des);
        identification.setText(shoppinglistid);


        //produce tool bar and navigation functionality
        toolbar  = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //initialize auto-edit text functionality
        autoedittext = (MaterialAutoCompleteTextView) findViewById(R.id.auto_text);
        initializeEditList();

        date_time_in=findViewById(R.id.date_time_input);
        date_time_in.setInputType(InputType.TYPE_NULL);
        date_time_in.setText(datetime3);
        date_time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(date_time_in);
            }
        });

        //set the string for the search edit text
        for(int i = 0; i < data3.size();i++)
        {
            if(data3.get(i).shopID.equals(shopid))
            {
                autoedittext.setText(shopLocationList.get(i));
                break;
            }
        }

        shopID = shopid;

        //auto complete on-text listener for the auto-edittext to get the shop id
        autoedittext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id)
            {
                int positionactual = 0;
                String selected = (String) parent.getItemAtPosition(position);
                for(int i = 0; i < shopLocationList.size(); i++)
                {
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

    private void getShoppingListInformation()
    {
        DataBaseHelper databasehelper = new DataBaseHelper(ViewShoppingListModifyPage.this);
        DataShoppingList data;
        data = databasehelper.getShoppingListItemById(shoppinglistid);

        if(data != null)
        {
            //set description values of the grocery item
            shopid = data.shopid;
            datetime3 = data.dateTime;
        }
        else
        {
            Toast.makeText(ViewShoppingListModifyPage.this,"Could not retrieve information.", Toast.LENGTH_SHORT).show();
        }
    }


    //Populate the search text here
    private void initializeEditList()
    {
        shopLocationList = new ArrayList<String>();
        fetchShopList();
        shopAdapater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, shopLocationList);
        autoedittext.setAdapter(shopAdapater);
    }


    //Fetch shop list from database to initialize auto text
    public void fetchShopList()
    {
        data3 = new ArrayList<>();
        DataBaseHelper databasehelper = new DataBaseHelper(ViewShoppingListModifyPage.this);
        data3 = databasehelper.getAllShoppingCentres();

        //Produce strings to add it to the list of values
        for(int i = 0; i< data3.size(); i++)
        {
            String shoppingdetails = data3.get(i).shopName + " " + data3.get(i).location;
            shopLocationList.add(shoppingdetails);
        }
    }


    //Modify data in database to update shop if needed
    public void modifyShoppingList(View view)
    {
        DataBaseHelper databasehelper = new DataBaseHelper(ViewShoppingListModifyPage.this);
        DataShoppingList shoppinglist = new DataShoppingList();


        boolean validatedate = validateDate(datetime3);
        boolean validateshopid = validateShopID(shopID);
        boolean validateid = validateIdentification(identification.getText().toString());

        //test validate to input into database
        if(validatedate && validateshopid && validateid)
        {
            shoppinglist.shopid = shopID;
            shoppinglist.dateTime = datetime3;
            shoppinglist.shoppinglistid =  identification.getText().toString();

            if(databasehelper.modifyShopInShoppingList(shoppinglist, shoppinglistid))
            {
                Intent intent = new Intent(ViewShoppingListModifyPage.this, ViewShoppingListModifyPage.class);
                //get id and place it in intent to view shopping list
                intent.putExtra("shoplistid", shoppinglist.shoppinglistid);
                ViewShoppingListModifyPage.this.startActivity(intent);
                Toast.makeText(getApplicationContext(),"Updated successfully.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent intent = new Intent(ViewShoppingListModifyPage.this, ViewShoppingListModifyPage.class);
                //get id and place it in intent to view shopping list
                intent.putExtra("shoplistid", shoppinglistid);
                ViewShoppingListModifyPage.this.startActivity(intent);
                Toast.makeText(getApplicationContext(),"Could not update.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Navigation item selector for Navigation box
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.action_shopping_list:
                startActivity(new Intent(ViewShoppingListModifyPage.this, HomePage.class));
                break;
            case R.id.action_grocery_list:
                startActivity(new Intent(ViewShoppingListModifyPage.this, GroceryListPage.class));
                break;
            case R.id.action_shopping_centre:
                startActivity(new Intent(ViewShoppingListModifyPage.this, ShoppingCentrePage.class));
                break;
            case R.id.action_add_shopping:
                startActivity(new Intent(ViewShoppingListModifyPage.this, AddShoppingListPage.class));
                break;
            case R.id.action_add_grocery:
                startActivity(new Intent(ViewShoppingListModifyPage.this, AddGroceryItem.class));
                break;
            case R.id.add_shopping_centre:
                startActivity(new Intent(ViewShoppingListModifyPage.this, AddShoppingCentrePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(ViewShoppingListModifyPage.this, HomePage.class));
                break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    //Date and time string setter from the Dialog box
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

                new TimePickerDialog(ViewShoppingListModifyPage.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(ViewShoppingListModifyPage.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    //validate date and set errors if neeeded
    private boolean validateDate(String s)
    {
        //check if empty and if it is set request to input value
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

    //validate shopping id
    private boolean validateShopID(String s)
    {
        String regex = "[0-9]{1,5}+";

        //test if empty or has a line
        if(s.equals(" "))
        {
            autoedittext.setText("");
            autoedittext.requestFocus();
            autoedittext.setError("Pick a shop from the list.");
            return false;
        }
        else if(s.isEmpty())
        {
            autoedittext.setText("");
            autoedittext.requestFocus();
            autoedittext.setError("Pick a shop from the list.");
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
        String regex = "[0-9]{1,5}+";

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
            identification.setError("Integer of length 1-5.");
            return false;
        }
        else
        {
            identification.setError(null);
            return true;
        }
    }
}