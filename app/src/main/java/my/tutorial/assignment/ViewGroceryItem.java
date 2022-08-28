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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ViewGroceryItem extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText date_time_in;
    String datetime3;
    EditText descriptionedit;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    DataGroceryList datagrocery;
    String id;
    int SELECT_PICTURE = 200;
    Uri selectedImageUri;
    // One Preview Image
    ImageView IVPreviewImage;
    byte [] img;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_grocery_item);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        datetime3 = "";
        descriptionedit = findViewById(R.id.description_des_text);
        date_time_in=findViewById(R.id.date_time_input);
        date_time_in.setInputType(InputType.TYPE_NULL);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        datagrocery = new DataGroceryList();

        //get item information to set for input text fields
        getItemInformation();


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

        //set on item click listener to show date time dialogue
        date_time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(date_time_in);
            }
        });
    }


    //get item information to input in text fields or other widgets
    private void getItemInformation()
    {
        DataBaseHelper databasehelper = new DataBaseHelper(ViewGroceryItem.this);
        datagrocery = databasehelper.getGroceryItemById(id);
        if(datagrocery != null)
        {
            descriptionedit.setText(datagrocery.description);
            date_time_in.setText(datagrocery.dateTime);
            datetime3 = datagrocery.dateTime;
            if(datagrocery.img != null)
            {
                Bitmap imagebit = BitmapFactory.decodeByteArray(datagrocery.img, 0 , datagrocery.img.length);
                IVPreviewImage.setImageBitmap(imagebit);
                IVPreviewImage.setBackground(null);
            }
        }
        else
        {
            Toast.makeText(ViewGroceryItem.this,"Could not retrieve information.", Toast.LENGTH_SHORT).show();
        }
    }

    // Modify grocery item in database
    public void modifyGroceryItem(View view)
    {
        DataBaseHelper databasehelper = new DataBaseHelper(ViewGroceryItem.this);

        boolean validatedescription =validateDescription(descriptionedit.getText().toString());
        boolean validatedate = validateDate(datetime3);

        if(selectedImageUri != null)
        {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
            datagrocery.img = byteArray.toByteArray();
        }

        if(validatedate && validatedescription)
        {
            String s1 = descriptionedit.getText().toString();
            String s2 = date_time_in.getText().toString();

            if(databasehelper.updateGroceryItem(id,s1,s2,datagrocery.img))
            {
                Intent intent = new Intent(ViewGroceryItem.this, ViewGroceryItem.class);
                intent.putExtra("id", id);
                ViewGroceryItem.this.startActivity(intent);
                Toast.makeText(ViewGroceryItem.this,"Update successful.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent intent = new Intent(ViewGroceryItem.this, ViewGroceryItem.class);
                intent.putExtra("id", id);
                ViewGroceryItem.this.startActivity(intent);
                Toast.makeText(ViewGroceryItem.this,"Could not update information.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //delete grocery item from database
    public void deleteGroceryItem(View view)
    {
        DataBaseHelper databasehelper = new DataBaseHelper(ViewGroceryItem.this);
        if(databasehelper.deleteGroceryItem(datagrocery))
        {
            Toast.makeText(ViewGroceryItem.this,"Deleted item from database.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(ViewGroceryItem.this,"Could not delete.", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(ViewGroceryItem.this, GroceryListPage.class);
        ViewGroceryItem.this.startActivity(intent);
    }

    //navigation links to move from page to page on navigation drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.action_shopping_list:
                startActivity(new Intent(ViewGroceryItem.this, HomePage.class));
                break;
            case R.id.action_grocery_list:
                startActivity(new Intent(ViewGroceryItem.this,GroceryListPage.class));
                break;
            case R.id.action_shopping_centre:
                startActivity(new Intent(ViewGroceryItem.this, ShoppingCentrePage.class));
                break;
            case R.id.action_add_shopping:
                startActivity(new Intent(ViewGroceryItem.this, AddShoppingListPage.class));
                break;
            case R.id.action_add_grocery:
                startActivity(new Intent(ViewGroceryItem.this, AddGroceryItem.class));
                break;
            case R.id.add_shopping_centre:
                startActivity(new Intent(ViewGroceryItem.this, AddShoppingCentrePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(ViewGroceryItem.this, HomePage.class));
                break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }

    //show date and time dialogue pop-up so user can pick time
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

                new TimePickerDialog(ViewGroceryItem.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(ViewGroceryItem.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    //validate date and set input errors
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

    //validate description of grocery item
    private boolean validateDescription(String s)
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,50}+";
        if(s.isEmpty())
        {
            descriptionedit.requestFocus();
            descriptionedit.setError("Field cannot be empty.");
            return false;
        }
        else if(!s.matches(regex ))
        {
            descriptionedit.requestFocus();
            descriptionedit.setError("Description of length 1-50.");
            return false;
        }
        else
        {
            descriptionedit.setError(null);
            return true;
        }
    }

    public void addImageItem(View view) {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {

                    // update the preview image in the layout
                    IVPreviewImage.setImageURI(selectedImageUri);
                    IVPreviewImage.setBackground(null);
                }
            }
        }
    }
}