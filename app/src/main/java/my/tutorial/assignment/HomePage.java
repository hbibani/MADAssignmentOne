package my.tutorial.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SharedPreferences pref;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private RecyclerView mRVShoppingList;
    private AdapterShoppingList mAdapter;
    List<DataShoppingList> data3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        {
            setContentView(R.layout.activity_home_page);
            toolbar  = findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.bringToFront();
            navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            fetchShoppingDetails();
        }
        //else
        {
            //Toast.makeText(HomePage.this, "Need to be logged in.", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(HomePage.this,  MainActivity.class));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        Log.d("Item Selector", String.valueOf(item.getItemId()));
        switch (item.getItemId())
        {

            case R.id.action_shopping_list:
                startActivity(new Intent(HomePage.this, HomePage.class));
                break;
            case R.id.action_grocery_list:
                startActivity(new Intent(HomePage.this, GroceryListPage.class));
                break;
            case R.id.action_shopping_centre:
                startActivity(new Intent(HomePage.this, ShoppingCentrePage.class));
                break;
            case R.id.action_add_shopping:
                startActivity(new Intent(HomePage.this, AddShoppingListPage.class));
                break;
            case R.id.action_add_grocery:
                startActivity(new Intent(HomePage.this, AddGroceryItem.class));
                break;
            case R.id.add_shopping_centre:
                startActivity(new Intent(HomePage.this, AddShoppingCentrePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(HomePage.this, HomePage.class));
                break;
            case R.id.action_logout_admin2:
            {
                pref=this.getSharedPreferences("NewsTweetSettings", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(HomePage.this,  MainActivity.class));
            }
            break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    //fetch shopping list for recycler view
    public void fetchShoppingDetails() {
        data3 = new ArrayList<>();
        DataBaseHelper databasehelper = new DataBaseHelper(HomePage.this);
        data3 = databasehelper.getAllShoppingLists();

        // Setup and Handover data to recyclerview
        mRVShoppingList = (RecyclerView) findViewById(R.id.admissionList);
        mAdapter = new AdapterShoppingList(HomePage.this, data3);
        mRVShoppingList.setAdapter(mAdapter);
        mRVShoppingList.setLayoutManager(new LinearLayoutManager(HomePage.this));
    }


}