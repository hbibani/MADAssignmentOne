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
        shopname = findViewById(R.id.shop_name);
        location = findViewById(R.id.location_des);
        Intent intent = getIntent();
        id = intent.getStringExtra("shopid");
        date_time_in=findViewById(R.id.date_time_input);
        date_time_in.setInputType(InputType.TYPE_NULL);
        datetime3 = "";
        getItemInformation();

        toolbar  = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
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
    }


    //get item information to display on screen
    private void getItemInformation()
    {
        datashopping = new DataShoppingCentre();
        DataBaseHelper databasehelper = new DataBaseHelper(ViewShoppingCentre.this);
        datashopping = databasehelper.getShoppingCentreById(id);
        if(datashopping != null)
        {
            shopname.setText(datashopping.shopName);
            location.setText(datashopping.location);
            date_time_in.setText(datashopping.dateTime);

        }
        else
        {
            Toast.makeText(ViewShoppingCentre.this,"Could not retrieve information.", Toast.LENGTH_SHORT).show();
        }

    }

    //modify shopping centre details in database
    public void modifyShoppingCentre(View view)
    {
        DataBaseHelper databasehelper = new DataBaseHelper(ViewShoppingCentre.this);
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

    public void deleteShoppingCentre(View view)
    {
        DataBaseHelper databasehelper = new DataBaseHelper(ViewShoppingCentre.this);
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
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(ViewShoppingCentre.this,  MainActivity.class));
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

                new TimePickerDialog(ViewShoppingCentre.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(ViewShoppingCentre.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}