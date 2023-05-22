package com.example.mealmovers_merchant.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmovers_merchant.R;
import com.example.mealmovers_merchant.main.use_cases.dialogs.DialogOrder;

import java.util.ArrayList;
import java.util.List;

public class ConfirmationTimeAdapter extends RecyclerView.Adapter<ConfirmationTimeAdapter.MyViewHolder> {



    private int[] items = {30, 40,60,80,100};
    private Context context;
    private DialogOrder dialog;

    public ConfirmationTimeAdapter(Context context, DialogOrder dialog) {
        this.context = context;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
