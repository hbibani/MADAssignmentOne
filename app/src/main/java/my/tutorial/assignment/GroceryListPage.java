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

import java.util.ArrayList;
import java.util.List;

public class GroceryListPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences pref;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private RecyclerView mRVFriends;
    private AdapterGroceryList mAdapter;
    List<DataGroceryList> data3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list_page);

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

        //fetch grocery details to implement them in recycler view
        fetchGroceryDetails();
    }



    //implement navigation drawer links to move from page to page
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.action_shopping_list:
                startActivity(new Intent(GroceryListPage.this, HomePage.class));
                break;
            case R.id.action_grocery_list:
                startActivity(new Intent(GroceryListPage.this, GroceryListPage.class));
                break;
            case R.id.action_shopping_centre:
                startActivity(new Intent(GroceryListPage.this, ShoppingCentrePage.class));
                break;
            case R.id.action_add_shopping:
                startActivity(new Intent(GroceryListPage.this, AddShoppingListPage.class));
                break;
            case R.id.action_add_grocery:
                startActivity(new Intent(GroceryListPage.this, AddGroceryItem.class));
                break;
            case R.id.add_shopping_centre:
                startActivity(new Intent(GroceryListPage.this, AddShoppingCentrePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(GroceryListPage.this, HomePage.class));
                break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }


    //fetch grocery details and then place them in the recycler view
    public void fetchGroceryDetails() {
        data3 = new ArrayList<>();
        DataBaseHelper databasehelper = new DataBaseHelper(GroceryListPage.this);
        data3 = databasehelper.getAllGroceryItems();

        // Setup and Handover data to recyclerview
        mRVFriends = (RecyclerView) findViewById(R.id.admissionList);
        mAdapter = new AdapterGroceryList(GroceryListPage.this, data3);
        mRVFriends.setAdapter(mAdapter);
        mRVFriends.setLayoutManager(new LinearLayoutManager(GroceryListPage.this));

    }

    //provide alert dialog before deleting item
    public void deleteGroceryItem(View view)
    {

        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(view.getContext())
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_baseline_delete_forever_24)

                .setPositiveButton("Delete", (dialog, whichButton) -> {
                    deleteGrocery();
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

    //delete grocery items which have been selected
    private void deleteGrocery() {
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
                    DataGroceryList groceryitem = data3.get(i);
                    DataBaseHelper databasehelper = new DataBaseHelper(GroceryListPage.this);
                    databasehelper.deleteGroceryItem(groceryitem);
                }
            }
            Intent intent = new Intent(GroceryListPage.this, GroceryListPage.class);
            GroceryListPage.this.startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"There are no items to delete.", Toast.LENGTH_SHORT).show();
        }
    }
}