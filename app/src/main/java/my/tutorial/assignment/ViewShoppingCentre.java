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
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewShoppingCentre extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    EditText date_time_in;
    EditText shopname;
    EditText location;
    String datetime3;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences pref;

    DataShoppingCentre datashopping;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shpping_centre);


        //retrieve the id from the itent so as  to view the grocery item and set widgets with ids
        Intent intent = getIntent();
        shopname = findViewById(R.id.shop_name);
        location = findViewById(R.id.location_des);
        id = intent.getStringExtra("shopid");
        datetime3 = intent.getStringExtra("date");
        date_time_in=findViewById(R.id.date_time_input);
        date_time_in.setInputType(InputType.TYPE_NULL);

        //get the item information from the database using the value of intent id
        getItemInformation();


        //set toolbar and navitgation items
        toolbar  = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //date set to show time dialogue
        date_time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(date_time_in);
            }
        });
    }


    //get item information to display on text fields for input
    private void getItemInformation()
    {
        datashopping = new DataShoppingCentre();
        DataBaseHelper databasehelper = new DataBaseHelper(ViewShoppingCentre.this);

        //retrieve datashopping item from the database and then set the name of the details using the id
        datashopping = databasehelper.getShoppingCentreById(id);
        if(datashopping != null)
        {

            //set the name and details of the shopping centre
            shopname.setText(datashopping.shopName);
            location.setText(datashopping.location);
            date_time_in.setText(datashopping.dateTime);
            datetime3 = datashopping.dateTime;
        }
        else
        {
            Toast.makeText(ViewShoppingCentre.this,"Could not retrieve information.", Toast.LENGTH_SHORT).show();
        }

    }

    //modify shopping centre details in database
    public void modifyShoppingCentre(View view)
    {

        //create databasehelper to delete
        DataBaseHelper databasehelper = new DataBaseHelper(ViewShoppingCentre.this);

        //retreive strings from inputs in the textfields and validate them
        boolean validateshopname = validateShopName(shopname.getText().toString());
        boolean validatelocation = validateLocation(location.getText().toString());
        boolean validatedate = validateDate(datetime3);


        //check if validation is successful and then delete
        if(validateshopname &&
                validatelocation && validatedate)
        {
            String s1 = shopname.getText().toString();
            String s2 = location.getText().toString();
            String s3 = date_time_in.getText().toString();
            if(databasehelper.updateShoppingCentre(id,s1,s2,s3))
            {
                Intent intent = new Intent(ViewShoppingCentre.this, ViewShoppingCentre.class);
                intent.putExtra("shopid", id);
                Toast.makeText(ViewShoppingCentre.this,"Update successful.", Toast.LENGTH_SHORT).show();
                ViewShoppingCentre.this.startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(ViewShoppingCentre.this, ViewShoppingCentre.class);
                intent.putExtra("shopid", id);
                Toast.makeText(ViewShoppingCentre.this,"Could not update information.", Toast.LENGTH_SHORT).show();
                ViewShoppingCentre.this.startActivity(intent);
            }
        }
    }

    //delete shopping centere from database
    public void deleteShoppingCentre(View view)
    {

        //create database helper
        DataBaseHelper databasehelper = new DataBaseHelper(ViewShoppingCentre.this);

        //delete item and check if it was successful
        if(databasehelper.deleteShoppingCentre(datashopping))
        {
            Toast.makeText(ViewShoppingCentre.this,"Deleted item from database.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(ViewShoppingCentre.this,"Could not delete.", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(ViewShoppingCentre.this, ShoppingCentrePage.class);
        ViewShoppingCentre.this.startActivity(intent);
    }



    //navigation link functionality so that you can move to a page
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.action_shopping_list:
                startActivity(new Intent(ViewShoppingCentre.this, HomePage.class));
                break;
            case R.id.action_grocery_list:
                startActivity(new Intent(ViewShoppingCentre.this,GroceryListPage.class));
                break;
            case R.id.action_shopping_centre:
                startActivity(new Intent(ViewShoppingCentre.this, ShoppingCentrePage.class));
                break;
            case R.id.action_add_shopping:
                startActivity(new Intent(ViewShoppingCentre.this, AddShoppingListPage.class));
                break;
            case R.id.action_add_grocery:
                startActivity(new Intent(ViewShoppingCentre.this, AddGroceryItem.class));
                break;
            case R.id.add_shopping_centre:
                startActivity(new Intent(ViewShoppingCentre.this, AddShoppingCentrePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(ViewShoppingCentre.this, HomePage.class));
                break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    //show date and time to appear when picking a date
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

                new TimePickerDialog(ViewShoppingCentre.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(ViewShoppingCentre.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }



    //validate shop name and set input errors
    private boolean validateShopName(String s)
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,50}+";

        //check if empty or does not match regex and set focus on input
        if(s.isEmpty())
        {
            shopname.requestFocus();
            shopname.setError("Field cannot be empty.");
            return false;
        }
        else if(!s.matches(regex ))
        {
            shopname.requestFocus();
            shopname.setError("Shop name of length 1-50.");
            return false;
        }
        else
        {
            shopname.setError(null);
            return true;
        }
    }


    //validate location and set input errors
    private boolean validateLocation(String s)
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,50}+";

        //check if empty or it does not match regex and set focus
        if(s.isEmpty())
        {
            location.requestFocus();
            location.setError("Field cannot be empty.");
            return false;
        }
        else if(!s.matches(regex ))
        {
            location.requestFocus();
            location.setError("Location of length 1-50.");
            return false;
        }
        else
        {
            location.setError(null);
            return true;
        }
    }

    //Validate date and set input errors
    private boolean validateDate(String s)
    {

        //check if string is empty and if it is set focus on this plane
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
}