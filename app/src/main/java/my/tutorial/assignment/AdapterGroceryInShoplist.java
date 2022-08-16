package my.tutorial.assignment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView shoplistid;
        TextView groceryid;
        TextView description;
        TextView amount;
        ImageView image;
        Button deletebutton;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            shoplistid = (TextView) itemView.findViewById(R.id.shoplistid_des);
            groceryid = (TextView) itemView.findViewById(R.id.groceryid_des);
            amount = (TextView) itemView.findViewById(R.id.amount_des);
            description = (TextView) itemView.findViewById(R.id.description_des);
            image = (ImageView) itemView.findViewById(R.id.image_des);
            deletebutton = (Button) itemView.findViewById(R.id.delete_button_des);

            deletebutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog myQuittingDialogBox = new AlertDialog.Builder(v.getContext())
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
            });

        }

        @Override
        public void onClick(View v)
        {
            int position = this.getAdapterPosition();
            DataGroceryInShopList groceryItem = data.get(position);
            String id = groceryItem.getId();
            String description = groceryItem.getDescription();
            String amount = groceryItem.getAmount();
            Intent intent = new Intent(context, HomePage.class);
            intent.putExtra("id", id);
            intent.putExtra("description", description);
            intent.putExtra("amount", amount);
            context.startActivity(intent);
        }

        private void deleteGrocery()
        {
            //Delete grocery item from shopping list in database
            int position = this.getAdapterPosition();
            DataGroceryInShopList groceryitems = data.get(position);
            DataBaseHelper databasehelper = new DataBaseHelper(context);
            if(databasehelper.deleteGroceryItemInShoppingList(groceryitems))
            {
                Toast.makeText(context,"Deleted item from database.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context,"Could not delete.", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(context, ViewShoppingListPage.class);
            intent.putExtra("shopid", groceryitems.id );
            context.startActivity(intent);
        }
    }

}
