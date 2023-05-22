package com.example.mealmovers_merchant.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmovers_merchant.R;
import com.example.mealmovers_merchant.main.models.MenuItemModel;

import java.util.List;

public class NewOrderItem extends RecyclerView.Adapter<NewOrderItem.MyViewHolder> {

    Context context;
    List<MenuItemModel> items;


    public NewOrderItem(Context context, List<MenuItemModel> items) {
        this.context = context;
        this.items = items;

    }

    @NonNull
    @Override
    public NewOrderItem.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_item_new_order, parent,false);
        return new NewOrderItem.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewOrderItem.MyViewHolder holder, int position) {
        MenuItemModel item = items.get(position);
        System.out.println(item.getName());
//        String date = String.valueOf(item.getCreatedAt()).substring(3, 10);
        holder.itemPrice.setText(item.getPrice() + "€");
        holder.itemName.setText(item.getName());
        holder.itemQuantity.setText(item.getQuantity() + "x");

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
       TextView itemQuantity, itemPrice, itemName, ordered_at;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);


        }
    }
}
