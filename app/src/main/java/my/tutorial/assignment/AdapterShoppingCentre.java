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
import java.util.Collections;
import java.util.List;


public class AdapterShoppingCentre extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    List<DataShoppingCentre> data = Collections.emptyList();
    DataGroceryList current;
    int currentPos=0;
    String test;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterShoppingCentre(Context context, List<DataShoppingCentre> data)
    {
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_shopping_centres, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataShoppingCentre current=data.get(position);
        myHolder.id.setText("ID: " + current.shopID );
        myHolder.shopname.setText("Name:" + current.shopName);
        myHolder.location.setText("Loc:" + current.location );
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //Get all the variables for the views that will be assigned in the my holder, this will match the adapater xml files in the layout
        TextView id;
        TextView shopname;
        TextView location;
        ImageView image;
        Button deleteButton;
        Button modifyButton;
        Button mapButton;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            //assign the views with the corresponding ids
            id = (TextView) itemView.findViewById(R.id.id_des);
            shopname = (TextView) itemView.findViewById(R.id.shop_des);
            location = (TextView) itemView.findViewById(R.id.location_des);
            image = (ImageView) itemView.findViewById(R.id.image_des);
            deleteButton = (Button) itemView.findViewById(R.id.delete_button_des);
            modifyButton = (Button) itemView.findViewById(R.id.view_button_des);
            mapButton = (Button) itemView.findViewById(R.id.map_button_des);

            //set delete item listener and allow for a dialog to pop up before deleting
            deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog myQuittingDialogBox = new AlertDialog.Builder(v.getContext())
                            // set message, title, and icon
                            .setTitle("Delete")
                            .setMessage("Do you want to Delete")
                            .setIcon(R.drawable.ic_baseline_delete_forever_24)

                            .setPositiveButton("Delete", (dialog, whichButton) -> {
                                deleteShoppingCentre();
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


            //modify the shopping center when modify button clicked
            modifyButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    modifyButton();
                }
            });

            //show map when map button is clicked
            mapButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mapButton();
                }
            });

        }


        //go to map using location on click
        public void mapButton()
        {
            int position = this.getAdapterPosition();
            DataShoppingCentre shopcentre = data.get(position);
            String shoppingID = shopcentre.getShoppingID();
            String shop = shopcentre.getShopName();
            String location = shopcentre.getLocation();
            String date = shopcentre.getDateTime();

            //start intent with location for the maps activity
            Intent intent = new Intent(context, MapsActivity.class);
            intent.putExtra("shopid", shoppingID);
            intent.putExtra("shopname", shop);
            intent.putExtra("location", location);
            intent.putExtra("date", date);
            context.startActivity(intent);
        }

        //modify shopping centre if needed
        public void modifyButton()
        {
            int position = this.getAdapterPosition();
            DataShoppingCentre shopcentre = data.get(position);
            String shoppingID = shopcentre.getShoppingID();
            String shop = shopcentre.getShopName();
            String location = shopcentre.getLocation();
            String date = shopcentre.getDateTime();

            //go to view shopping centre page to modify pages and produce strings to access for retrieval of information from database
            Intent intent = new Intent(context, ViewShoppingCentre.class);
            intent.putExtra("shopid", shoppingID);
            intent.putExtra("shopname", shop);
            intent.putExtra("location", location);
            intent.putExtra("date", date);
            context.startActivity(intent);
        }

        @Override
        public void onClick(View v)
        {
        }


        //Delete shopping centre from database
        private void deleteShoppingCentre()
        {
            //Delete friend from database
            int position = this.getAdapterPosition();
            DataShoppingCentre shoppingcentre = data.get(position);
            DataBaseHelper databasehelper = new DataBaseHelper(context);
            if(databasehelper.deleteShoppingCentre(shoppingcentre))
            {
                Toast.makeText(context,"Deleted item from database.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context,"Could not delete.", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(context, ShoppingCentrePage.class);
            context.startActivity(intent);
        }
    }
}
