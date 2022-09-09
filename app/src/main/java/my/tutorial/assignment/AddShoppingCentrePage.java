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
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class AddShoppingCentrePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText date_time_in;
    String datetime3;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences pref;
    EditText shopname_text;
    EditText location_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping_centre_page);
        date_time_in=findViewById(R.id.date_time_input);
        shopname_text = findViewById(R.id.shop_nametest);
        location_text = findViewById(R.id.location_des);
        date_time_in.setInputType(InputType.TYPE_NULL);
        datetime3 = "";

        //set tool bar and navigation functionality
        toolbar  = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //set date and time dialog
        date_time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(date_time_in);
            }
        });

    }


    //validate shopname and set input errors
    private boolean validateShopName(String s)
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,50}+";
        if(s.isEmpty())
        {
            shopname_text.requestFocus();
            shopname_text.setError("Field cannot be empty.");
            return false;
        }
        else if(!s.matches(regex ))
        {
            shopname_text.requestFocus();
            shopname_text.setError("Shop name of length 1-50.");
            return false;
        }
        else
        {
            shopname_text.setError(null);
            return true;
        }
    }

    //validate location and set input errors
    private boolean validateLocation(String s)
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,50}+";
        if(s.isEmpty())
        {
            location_text.requestFocus();
            location_text.setError("Field cannot be empty.");
            return false;
        }
        else if(!s.matches(regex ))
        {
            location_text.requestFocus();
            location_text.setError("Location of length 1-50.");
            return false;
        }
        else
        {
            location_text.setError(null);
            return true;
        }
    }

    //validate date string and set input errors
    private boolean validateDate(String s)
    {
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

    //add shopping centre to the database
    public void addShoppingCentre(View view) {
        DataBaseHelper databasehelper = new DataBaseHelper(AddShoppingCentrePage.this);
        DataShoppingCentre datashoppingcentre = new DataShoppingCentre();

        //check validation from functions and retrieve booleans
        boolean validateshopname = validateShopName(shopname_text.getText().toString());
        boolean validatelocation = validateLocation(location_text.getText().toString());
        boolean validatedate = validateDate(datetime3);


        //if validation is successful, add it to the database
        if(validateshopname &&
                validatelocation && validatedate)
        {
            datashoppingcentre.shopName= shopname_text.getText().toString();
            datashoppingcentre.dateTime = datetime3;
            datashoppingcentre.location = location_text.getText().toString();

            //add items to the database
            if(databasehelper.addShoppingCentre(datashoppingcentre))
            {
                startActivity(new Intent(AddShoppingCentrePage.this, AddShoppingCentrePage.class));
                Toast.makeText(getApplicationContext(),"Added to the database.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Could not add to the database.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //show time dialog to pick the time and date for user
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

                new TimePickerDialog(AddShoppingCentrePage.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(AddShoppingCentrePage.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    //on navigation click the links needed to move to selected page
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.action_shopping_list:
                startActivity(new Intent(AddShoppingCentrePage.this, HomePage.class));
                break;
            case R.id.action_grocery_list:
                startActivity(new Intent(AddShoppingCentrePage.this,GroceryListPage.class));
                break;
            case R.id.action_shopping_centre:
                startActivity(new Intent(AddShoppingCentrePage.this, ShoppingCentrePage.class));
                break;
            case R.id.action_add_shopping:
                startActivity(new Intent(AddShoppingCentrePage.this, AddShoppingListPage.class));
                break;
            case R.id.action_add_grocery:
                startActivity(new Intent(AddShoppingCentrePage.this, AddGroceryItem.class));
                break;
            case R.id.add_shopping_centre:
                startActivity(new Intent(AddShoppingCentrePage.this, AddShoppingCentrePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AddShoppingCentrePage.this, HomePage.class));
                break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
}