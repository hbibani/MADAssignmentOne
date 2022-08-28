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

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

        setContentView(R.layout.activity_home_page);

        //set tool bar and navigation drawer for page movement and links
        toolbar  = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        try {
            fetchShoppingDetails();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    //set navigation input values
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
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    //fetch shopping list for recycler view
    public void fetchShoppingDetails() throws ParseException {
        data3 = new ArrayList<>();
        DataBaseHelper databasehelper = new DataBaseHelper(HomePage.this);
        data3 = databasehelper.getAllShoppingLists();
        List<DataShoppingList> past = new ArrayList<>(); ;
        List<DataShoppingList> present = new ArrayList<>();;

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());


        for(int i = 0 ; i < data3.size(); i++)
        {
            SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = sdformat.parse(data3.get(i).dateTime);
            Date d2 = sdformat.parse(timeStamp);
            if(d2.compareTo(d1) < 0 || d2.compareTo(d1) == 0)
            {
                data3.get(i).present = true;
                present.add(data3.get(i));
            }
            else
            {
                past.add(data3.get(i));
            }
        }

        for(int i = 0; i < past.size(); i++)
        {
            present.add(past.get(i));
        }



        // Setup and Handover data to recyclerview
        mRVShoppingList = (RecyclerView) findViewById(R.id.admissionList);
        mAdapter = new AdapterShoppingList(HomePage.this, present);
        mRVShoppingList.setAdapter(mAdapter);
        mRVShoppingList.setLayoutManager(new LinearLayoutManager(HomePage.this));
    }

    //provide a dialogue before deleting item
    public void deleteShoppingListItem(View view) {

        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(view.getContext())
        // set message, title, and icon
        .setTitle("Delete")
        .setMessage("Do you want to Delete")
        .setIcon(R.drawable.ic_baseline_delete_forever_24)

        .setPositiveButton("Delete", (dialog, whichButton) -> {
            deleteShoppingList();
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

    //delete shopping list items
    private void deleteShoppingList() {

        boolean test = false;
        for(int i = 0; i < data3.size(); i++)
        {
            if(data3.get(i).deletecheck)
            {
                test = true;
                break;
            }
        }

        if(test)
        {
            for(int i = 0; i < data3.size(); i++)
            {
                if(data3.get(i).deletecheck == true)
                {
                    DataShoppingList shoppinglist = data3.get(i);
                    DataBaseHelper databasehelper = new DataBaseHelper(HomePage.this);
                    databasehelper.deleteShoppingList(shoppinglist);
                }
            }
            startActivity(new Intent(HomePage.this, HomePage.class));
        }
        else
        {
            Toast.makeText(getApplicationContext(),"There are no items to delete.", Toast.LENGTH_SHORT).show();
        }

    }
}