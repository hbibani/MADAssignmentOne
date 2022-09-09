package my.tutorial.assignment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


        if(current.img != null)
        {
            Bitmap imagebit = BitmapFactory.decodeByteArray(current.img, 0 , current.img.length);
            myHolder.image.setImageBitmap(imagebit);
        }
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

        //create variables for each of the views to be assigned in the myholder, this will match the adapater xml files in the layout
        TextView id;
        TextView desc;
        TextView date;
        ImageView image;
        Button viewButton;
        CheckBox checkbox;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //set all the views and assign them using the ids
            id = (TextView) itemView.findViewById(R.id.id_des);
            desc = (TextView) itemView.findViewById(R.id.description_des);
            date = (TextView) itemView.findViewById(R.id.date_des);
            image = (ImageView) itemView.findViewById(R.id.image_des);
            //deleteButton = (Button) itemView.findViewById(R.id.delete_button_des);
            viewButton = (Button) itemView.findViewById(R.id.view_button_des);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkBox);

            //set check box item listener to see if item needs to be deleted
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkButton(isChecked);
                }
            });

            //go to modification page and view grocery item details
            viewButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    viewButton();
                }
            });


        }


        //check if item is scheduled for deletion
        public void checkButton(boolean ischecked)
        {
            int position = this.getAdapterPosition();
            DataGroceryList groceryItem = data.get(position);

            //set position to deleted if is checked
            if(ischecked)
            {
                groceryItem.deletecheck = true;
            }
            else
            {
                groceryItem.deletecheck = false;
            }
        }


        //view grocery item for modification
        public void viewButton()
        {
            int position = this.getAdapterPosition();
            DataGroceryList grocerylist = data.get(position);
            String shoppingID = grocerylist.getGroceryID();
            String description = grocerylist.getDescription();
            String date = grocerylist.getDateTime();

            //go to viewgroceryitem page for modification if needed
            Intent intent = new Intent(context, ViewGroceryItem.class);
            intent.putExtra("id", shoppingID);
            intent.putExtra("description", description);
            intent.putExtra("date", date);
            context.startActivity(intent);
        }

        @Override
        public void onClick(View v)
        {
        }
    }
}
