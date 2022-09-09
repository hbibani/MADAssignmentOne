package my.tutorial.assignment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;


public class AdapterShoppingList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    List<DataShoppingList> data = Collections.emptyList();
    DataShoppingList current;
    int currentPos=0;
    String test;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterShoppingList(Context context, List<DataShoppingList> data)
    {
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_shopping_list, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataShoppingList current=data.get(position);

        myHolder.id.setText("ID: " + current.shoppinglistid);
        myHolder.shopid.setText("ShopID: " + current.shopid);
        myHolder.shopname.setText("Shop: " + current.shopName);
        myHolder.location.setText("Loc: " + current.location);
        myHolder.date.setText("Date: " + current.dateTime);

        if (current.present)
        {
            myHolder.image.setImageURI(Uri.parse("android.resource://my.tutorial.assignment/drawable/icon_shop_adapter"));
        }
        else
        {
            myHolder.image.setImageURI(Uri.parse("android.resource://my.tutorial.assignment/drawable/ic_closed"));
        }
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //Set all the views to be accesesd later in the myholder, this will match the adapter xml files in the layout
        TextView id;
        TextView location;
        TextView date;
        TextView shopid;
        TextView shopname;
        ImageView image;
        Button modifyButton;
        Button mapButton;
        Button addButton;
        CheckBox checkbox;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //assign the views with the corresponding ids
            id = (TextView) itemView.findViewById(R.id.id_des);
            shopid = (TextView) itemView.findViewById(R.id.shopid_des);
            shopname = (TextView) itemView.findViewById(R.id.shopname_des);
            location = (TextView) itemView.findViewById(R.id.location_des);
            date = (TextView) itemView.findViewById(R.id.date_des);
            image = (ImageView) itemView.findViewById(R.id.image_des);
            modifyButton = (Button) itemView.findViewById(R.id.view_button_des);
            mapButton = (Button) itemView.findViewById(R.id.map_button_des);
            addButton = (Button) itemView.findViewById(R.id.add_button_des);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkBox);

            //check box to see if item needs deleting
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkButton(isChecked);
                }
            });

            //go to modify page if needed
            modifyButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    modifyButton();
                }
            });

            //find location on the map
            mapButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mapButton();
                }
            });

            //view the shopping list to add grocery items to the shopping list
            addButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addButton();
                }
            });


        }

        //state that item has been checked if it needs to be deleted
        public void checkButton(boolean ischecked)
        {
            int position = this.getAdapterPosition();
            DataShoppingList listItem = data.get(position);

            if(ischecked)
            {
                listItem.deletecheck = true;
            }
            else
            {
                listItem.deletecheck = false;
            }
        }


        //go to shopping centre modication page
        public void modifyButton()
        {
            int position = this.getAdapterPosition();
            DataShoppingList shoplist = data.get(position);
            String shoppinglistid = shoplist.shoppinglistid;
            String shopid = shoplist.getShopid();
            String date = shoplist.getDateTime();

            //put all values to identify shopping centre
            Intent intent = new Intent(context, ViewShoppingListModifyPage.class);
            intent.putExtra("shoplistid", shoppinglistid);
            intent.putExtra("shopid", shopid);
            intent.putExtra("date", date);
            context.startActivity(intent);
        }


        //get the address from location and place it in intent to locate shopping centre location
        public void mapButton()
        {
            int position = this.getAdapterPosition();
            DataShoppingList shoplist = data.get(position);
            String location = shoplist.getLocation();

            //start maps activity and use location to find shopping centre associated with the shopping list
            Intent intent = new Intent(context, MapsActivity.class);
            intent.putExtra("location", location);
            context.startActivity(intent);
        }


        //go to the shopping list page to add the items
        public void addButton()
        {

            int position = this.getAdapterPosition();
            DataShoppingList shoplist = data.get(position);
            String shoppingID = shoplist.getShoppinglistid();
            Intent intent = new Intent(context, ViewShoppingListPage.class);

            //get id and place it in intent to view shopping list
            intent.putExtra("shopinglistid", shoppingID);
            context.startActivity(intent);
        }

        @Override
        public void onClick(View v)
        {

        }

    }

}
