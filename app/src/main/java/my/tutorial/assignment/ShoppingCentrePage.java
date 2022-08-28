package my.tutorial.assignment;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ShoppingCentrePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences pref;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private RecyclerView mRVFriends;
    private AdapterShoppingCentre mAdapter;
    List<DataShoppingCentre> data3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_centre_page);

        //set toolbar and navigation drawer
        toolbar  = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        fetchCentreDetails();

    }

    //navigation item interface to move from page to page using the navigation drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.action_shopping_list:
                startActivity(new Intent(ShoppingCentrePage.this, HomePage.class));
                break;
            case R.id.action_grocery_list:
                startActivity(new Intent(ShoppingCentrePage.this, GroceryListPage.class));
                break;
            case R.id.action_shopping_centre:
                startActivity(new Intent(ShoppingCentrePage.this, ShoppingCentrePage.class));
                break;
            case R.id.action_add_shopping:
                startActivity(new Intent(ShoppingCentrePage.this, AddShoppingListPage.class));
                break;
            case R.id.action_add_grocery:
                startActivity(new Intent(ShoppingCentrePage.this, AddGroceryItem.class));
                break;
            case R.id.add_shopping_centre:
                startActivity(new Intent(ShoppingCentrePage.this, AddShoppingCentrePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(ShoppingCentrePage.this, HomePage.class));
                break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    //fetch centre detials to produce list in recycler view
    private void fetchCentreDetails() {
        data3 = new ArrayList<>();
        DataBaseHelper databasehelper = new DataBaseHelper(ShoppingCentrePage.this);
        data3 = databasehelper.getAllShoppingCentres();

        // Setup and Handover data to recyclerview
        mRVFriends = (RecyclerView) findViewById(R.id.admissionList);
        mAdapter = new AdapterShoppingCentre(ShoppingCentrePage.this, data3);
        mRVFriends.setAdapter(mAdapter);
        mRVFriends.setLayoutManager(new LinearLayoutManager(ShoppingCentrePage.this));
    }

}