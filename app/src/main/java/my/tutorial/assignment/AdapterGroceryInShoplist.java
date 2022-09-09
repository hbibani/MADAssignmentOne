package my.tutorial.assignment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;


public class AdapterGroceryInShoplist extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Context context;
    private LayoutInflater inflater;
    List<DataGroceryInShopList> data = Collections.emptyList();
    DataGroceryInShopList current;
    int currentPos=0;
    String test;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterGroceryInShoplist(Context context, List<DataGroceryInShopList> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_grocery_item_in_shop, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataGroceryInShopList current=data.get(position);
        myHolder.shoplistid.setText("listID: " + current.shopListID);
        myHolder.groceryid.setText("groceryID: " + current.groceryID);
        myHolder.description.setText("Desc: " + current.description);
        myHolder.amount.setText("Amount: " + current.amount);

        if(current.img != null)
        {
            Bitmap imagebit = BitmapFactory.decodeByteArray(current.img, 0 , current.img.length);
            myHolder.image.setImageBitmap(imagebit);
        }

        if(current.grocerychecked == 1)
        {
            myHolder.checkbox.setChecked(true);
        }
        else
        {
            myHolder.checkbox.setChecked(false);
        }
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //create variables which will be assigned to the views with id in myholder, this will match the adapater xml files in the layout
        TextView shoplistid;
        TextView groceryid;
        TextView description;
        TextView amount;
        ImageView image;
        Button deletebutton;
        CheckBox checkbox;
        CheckBox checkboxdelete;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            //set all the views with associated id
            shoplistid = (TextView) itemView.findViewById(R.id.shoplistid_des);
            groceryid = (TextView) itemView.findViewById(R.id.groceryid_des);
            amount = (TextView) itemView.findViewById(R.id.amount_des);
            description = (TextView) itemView.findViewById(R.id.description_des);
            image = (ImageView) itemView.findViewById(R.id.image_des);
            deletebutton = (Button) itemView.findViewById(R.id.delete_button_des);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkBox);
            checkboxdelete = (CheckBox) itemView.findViewById(R.id.checkBox1);


            //check box to indicate that the item has been picked up
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkButton();
                }
            });

            //check box schedule for deletion
            checkboxdelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkButtonDelete(isChecked);
                }
            });

        }


        //check to see if item has been picked up, if it has been picked up, we check the item and update in database
        public void checkButton()
        {
            int position = this.getAdapterPosition();
            DataGroceryInShopList groceryItem = data.get(position);
            DataBaseHelper databasehelper = new DataBaseHelper(context);

            //check item to see if it has been placed in the grocery list
            if(checkbox.isChecked())
            {

                //update item details in the database to say it is checked
                if(databasehelper.checkTheGroceryIteam(groceryItem.id,1))
                {
                    groceryItem.grocerychecked = 1;
                    //Toast.makeText(context,"Updated succesfully.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context,"Could not update.", Toast.LENGTH_SHORT).show();
                }


            }
            else
            {

                //set to checked item to false if it has been changed to not checked in the database
                if(databasehelper.checkTheGroceryIteam(groceryItem.id,0))
                {
                    groceryItem.grocerychecked = 0;
                    //Toast.makeText(context,"Updated succesfully.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context,"Could not update.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onClick(View v)
        {
        }

        //check item to schedule for deletion from check box
        private void checkButtonDelete(boolean isChecked)
        {
            int position = this.getAdapterPosition();
            DataGroceryInShopList listItem = data.get(position);

            if(isChecked)
            {
                listItem.deleteitemchecked = true;
            }
            else
            {
                listItem.deleteitemchecked = false;
            }
        }

    }

}
