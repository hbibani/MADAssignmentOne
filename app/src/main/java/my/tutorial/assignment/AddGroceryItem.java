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
import android.database.sqlite.SQLiteDatabase;
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
import java.util.Calendar;

public class AddGroceryItem extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText date_time_in;
    EditText grocery_description;
    EditText identification;
    String datetime3;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences pref;
    // One Button
    Uri selectedImageUri;
    // One Preview Image
    ImageView IVPreviewImage;
    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grocery_item);


        //find edit text, images and others and match them with xml inputs
        date_time_in=findViewById(R.id.date_time_input);
        date_time_in.setInputType(InputType.TYPE_NULL);
        grocery_description = findViewById(R.id.patient_mrn_number);
        identification = findViewById(R.id.id_des);

        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        datetime3 = "";
        selectedImageUri = null;

        //set tool bar and drawer layout
        toolbar  = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //set item on select listener
        date_time_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(date_time_in);
            }
        });



    }

    //set navigation drawer links
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {

            case R.id.action_shopping_list:
                startActivity(new Intent(AddGroceryItem.this, HomePage.class));
                break;
            case R.id.action_grocery_list:
                startActivity(new Intent(AddGroceryItem.this,GroceryListPage.class));
                break;
            case R.id.action_shopping_centre:
                startActivity(new Intent(AddGroceryItem.this, ShoppingCentrePage.class));
                break;
            case R.id.action_add_shopping:
                startActivity(new Intent(AddGroceryItem.this, AddShoppingListPage.class));
                break;
            case R.id.action_add_grocery:
                startActivity(new Intent(AddGroceryItem.this, AddGroceryItem.class));
                break;
            case R.id.add_shopping_centre:
                startActivity(new Intent(AddGroceryItem.this, AddShoppingCentrePage.class));
                break;
            case R.id.nav_main:
                startActivity(new Intent(AddGroceryItem.this, HomePage.class));
                break;
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }


    //add grocery item to the grocery lust
    public void addGroceryItem(View view) {
        DataBaseHelper databasehelper = new DataBaseHelper(AddGroceryItem.this);
        DataGroceryList datagrocerylist = new DataGroceryList();

        //get booleans to check validation of items in the database
        boolean validatedescription =validateDescription(grocery_description.getText().toString());
        boolean validateid = validateIdentification(identification.getText().toString());
        boolean validatedate = validateDate(datetime3);


        //check if image requires to be set from the uri of the image
        datagrocerylist.img = null;
        if(selectedImageUri != null)
        {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArray);
                datagrocerylist.img = byteArray.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //check if inputs have been validated before inputting into database
        if(validatedate && validatedescription & validateid)
        {
            datagrocerylist.description = grocery_description.getText().toString();
            datagrocerylist.groceryID = identification.getText().toString();
            datagrocerylist.dateTime = datetime3;
            if(databasehelper.addGroceryItem(datagrocerylist))
            {
                Toast.makeText(getApplicationContext(),"Added to the database.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddGroceryItem.this, AddGroceryItem.class));
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Could not add to the database.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //show time and date dialog to allow user to input user and date
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

                new TimePickerDialog(AddGroceryItem.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };

        new DatePickerDialog(AddGroceryItem.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    //validate date and set input errors
    private boolean validateDate(String s)
    {
        //if it is empty or the value does not match regex then set focus on input
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

    //validate description and set input errors
    private boolean validateDescription(String s)
    {
        String regex = "[a-zA-Z0-9@+'.!#$'&quot;,:;=/\\(\\),\\-\\s]{1,50}+";

        //if it is empty or the value does not match regex then set focus on input
        if(s.isEmpty())
        {
            grocery_description.requestFocus();
            grocery_description.setError("Field cannot be empty.");
            return false;
        }
        else if(!s.matches(regex ))
        {
            grocery_description.requestFocus();
            grocery_description.setError("Description of length 1-50.");
            return false;
        }
        else
        {
            grocery_description.requestFocus();
            grocery_description.setError(null);
            return true;
        }
    }

    //validate identification and set input errors
    private boolean validateIdentification(String s)
    {
        String regex = "[0-9]{1,5}+";
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