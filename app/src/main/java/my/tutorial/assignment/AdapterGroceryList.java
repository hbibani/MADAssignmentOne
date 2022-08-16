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


public class AdapterGroceryList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private LayoutInflater inflater;
    List<DataGroceryList> data = Collections.emptyList();
    DataGroceryList current;
    int currentPos=0;
    String test;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterGroceryList(Context context, List<DataGroceryList> data)
    {
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_grocery_list, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataGroceryList current=data.get(position);

        myHolder.id.setText("ID: " + current.groceryID );
        myHolder.desc.setText("Desc:" + current.description );
        myHolder.date.setText("Date: " + current.dateTime);
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView id;
        TextView desc;
        TextView date;
        ImageView image;
        Button deleteButton;
        Button viewButton;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            id = (TextView) itemView.findViewById(R.id.id_des);
            desc = (TextView) itemView.findViewById(R.id.description_des);
            date = (TextView) itemView.findViewById(R.id.date_des);
            image = (ImageView) itemView.findViewById(R.id.image_des);
            deleteButton = (Button) itemView.findViewById(R.id.delete_button_des);
            viewButton = (Button) itemView.findViewById(R.id.view_button_des);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    AlertDialog myQuittingDialogBox = new AlertDialog.Builder(v.getContext())
                            // set message, title, and icon
                            .setTitle("Delete")
                            .setMessage("Do you want to Delete")
                            .setIcon(R.drawable.ic_baseline_delete_forever_24)

                            .setPositiveButton("Delete", (dialog, whichButton) -> {
                                deleteItem();
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


            viewButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    viewButton();
                }
            });


        }



        public void viewButton()
        {
            int position = this.getAdapterPosition();
            DataGroceryList grocerylist = data.get(position);
            String shoppingID = grocerylist.getGroceryID();
            String description = grocerylist.getDescription();
            String date = grocerylist.getDateTime();

            Intent intent = new Intent(context, ViewGroceryItem.class);
            intent.putExtra("id", shoppingID);
            intent.putExtra("description", description);
            intent.putExtra("date", date);
            context.startActivity(intent);
        }

        @Override
        public void onClick(View v)
        {
            int position = this.getAdapterPosition();
            DataGroceryList grocerylist = data.get(position);
            String shoppingID = grocerylist.getGroceryID();
            String description = grocerylist.getDescription();
            String date = grocerylist.getDateTime();

            Intent intent = new Intent(context, HomePage.class);
            intent.putExtra("id", shoppingID);
            intent.putExtra("description", description);
            intent.putExtra("date", date);
            context.startActivity(intent);
        }


        //Delete grocery item from database
        private void deleteItem()
        {
            int position = this.getAdapterPosition();
            DataGroceryList groceryitem = data.get(position);
            DataBaseHelper databasehelper = new DataBaseHelper(context);
            if(databasehelper.deleteGroceryItem(groceryitem ))
            {
                Toast.makeText(context,"Deleted item from database..", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context,"Could not delete.", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(context, GroceryListPage.class);
            context.startActivity(intent);
        }

    }





}
