package my.tutorial.assignment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView id;
        TextView location;
        TextView date;
        TextView shopid;
        TextView shopname;
        ImageView image;
        Button deleteButton;
        Button modifyButton;
        Button mapButton;
        Button addButton;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            id = (TextView) itemView.findViewById(R.id.id_des);
            shopid = (TextView) itemView.findViewById(R.id.shopid_des);
            shopname = (TextView) itemView.findViewById(R.id.shopname_des);
            location = (TextView) itemView.findViewById(R.id.location_des);
            date = (TextView) itemView.findViewById(R.id.date_des);
            image = (ImageView) itemView.findViewById(R.id.image_des);
            deleteButton = (Button) itemView.findViewById(R.id.delete_button_des);
            modifyButton = (Button) itemView.findViewById(R.id.view_button_des);
            mapButton = (Button) itemView.findViewById(R.id.map_button_des);
            addButton = (Button) itemView.findViewById(R.id.add_button_des);


            deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog myQuittingDialogBox = new AlertDialog.Builder(v.getContext())
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
            });


            modifyButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    modifyButton();
                }
            });

            mapButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mapButton();
                }
            });


            addButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addButton();
                }
            });


        }

        public void modifyButton()
        {
            int position = this.getAdapterPosition();
            DataShoppingList shoplist = data.get(position);
            String shoppinglistid = shoplist.shoppinglistid;
            String shopid = shoplist.getShopid();
            String date = shoplist.getDateTime();
            Intent intent = new Intent(context, ViewShoppingListModifyPage.class);
            intent.putExtra("shoplistid", shoppinglistid);
            intent.putExtra("shopid", shopid);
            intent.putExtra("date", date);

            context.startActivity(intent);
        }


        public void mapButton()
        {
            int position = this.getAdapterPosition();
            DataShoppingList shoplist = data.get(position);
            String location = shoplist.getLocation();
            Intent intent = new Intent(context, MapsActivity.class);
            intent.putExtra("location", location);
            context.startActivity(intent);
        }

        public void addButton()
        {
            int position = this.getAdapterPosition();
            DataShoppingList shoplist = data.get(position);
            String shoppingID = shoplist.getShoppinglistid();

            Intent intent = new Intent(context, ViewShoppingListPage.class);
            intent.putExtra("shopinglistid", shoppingID);
            context.startActivity(intent);
        }

        @Override
        public void onClick(View v)
        {
            int position = this.getAdapterPosition();
            DataShoppingList shoplist = data.get(position);
            String shoppingID = shoplist.getShoppinglistid();
            String date = shoplist.getDateTime();

            Intent intent = new Intent(context, HomePage.class);
            intent.putExtra("shopid", shoppingID);
            intent.putExtra("date", date);
            context.startActivity(intent);
        }


        private void deleteShoppingList()
        {
            //Delete friend from database
            int position = this.getAdapterPosition();
            DataShoppingList shoppinglist = data.get(position);
            DataBaseHelper databasehelper = new DataBaseHelper(context);
            if(databasehelper.deleteShoppingList(shoppinglist))
            {
                Toast.makeText(context,"Deleted item from database.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context,"Could not delete.", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(context, HomePage.class);
            context.startActivity(intent);
        }

    }




}
