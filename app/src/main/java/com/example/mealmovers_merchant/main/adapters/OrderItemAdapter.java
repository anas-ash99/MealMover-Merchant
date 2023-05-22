package com.example.mealmovers_merchant.main.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmovers_merchant.R;
import com.example.mealmovers_merchant.main.ExtensionMethods;
import com.example.mealmovers_merchant.main.models.MenuItemModel;
import com.example.mealmovers_merchant.main.models.OrderModel;
import com.example.mealmovers_merchant.main.use_cases.dialogs.DialogOrder;
import com.example.mealmovers_merchant.main.viewModels.MainViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderItemAdapter  extends RecyclerView.Adapter<OrderItemAdapter.MyViewHolder> {


    Context context;
    List<OrderModel> orders;
    private DialogOrder orderDialog;

    private MainViewModel viewModel;
    public void setOrders(List<OrderModel> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    public OrderItemAdapter(Context context, List<OrderModel> orders,MainViewModel viewModel) {
        this.context = context;
        this.orders = orders;
        this.viewModel = viewModel;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        orderDialog = new DialogOrder(context, null);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_item, parent, false);
        return new OrderItemAdapter.MyViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        OrderModel order = orders.get(position);

        try {

            String hourMinute = ExtensionMethods.toHourAndMinute(order.getCreated_at());
            int productsQuantity = 0;

            holder.street_name.setText(order.getAddress().getStreetName() + " " + order.getAddress().getHouseNumber());
            holder.customer_name.setText(order.getAddress().getName());
            for (MenuItemModel item: order.getItems()) {
                productsQuantity = productsQuantity + item.getQuantity();
            }
            holder.ordered_at.setText(hourMinute);
            holder.quantityAndPrice.setText(productsQuantity + " Products" + " (" + order.getOrderPrice() + ")");

            if (order.getStatus().equals("delivered")){holder.moveOrderIcon.setVisibility(View.GONE);}
            holder.moveOrderIcon.setOnClickListener(v -> {viewModel.handleMoveOrderClick(order);});
            holder.itemView.setOnClickListener(v -> orderDialog.showAlreadyExistOrderDialog(order));
        }catch (Exception e){

            Log.e("order item", e.getMessage(), e);
            Toast.makeText(context, "Something went wrong with order item adapter", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ordered_at, street_name, customer_name, quantityAndPrice;
        CardView moveOrderIcon;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ordered_at = itemView.findViewById(R.id.ordered_at);
            street_name = itemView.findViewById(R.id.street_name);
            customer_name = itemView.findViewById(R.id.customer_name);
            quantityAndPrice  = itemView.findViewById(R.id.productsAndPrice);
            ordered_at = itemView.findViewById(R.id.ordered_at);
            moveOrderIcon = itemView.findViewById(R.id.moveOrderIcon);






        }
    }

    private void showNewOrderDialog(OrderModel order) {

        Dialog dialog;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.order_layout);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
        TextView deliveryTime, paymentStatus, totalPrice, phoneNumber, deliveryType, addressLine2, addressLine1, customerName, xButton;
        LinearLayout setTimeLayout, newLayout;
        RecyclerView orderMenuItemsRV;
        orderMenuItemsRV = dialog.findViewById(R.id.itemsRecyclerView);
        deliveryTime = dialog.findViewById(R.id.deliveryTime);
        xButton = dialog.findViewById(R.id.xButton);
        paymentStatus = dialog.findViewById(R.id.paymentStatus);
        totalPrice = dialog.findViewById(R.id.totalPrice);
        deliveryType = dialog.findViewById(R.id.deliveryType);
        phoneNumber = dialog.findViewById(R.id.phoneNumber);
        addressLine2 = dialog.findViewById(R.id.addressLine2);
        addressLine1 = dialog.findViewById(R.id.addressLine1);
        customerName = dialog.findViewById(R.id.customerName);
        newLayout = dialog.findViewById(R.id.newLayout);
        setTimeLayout = dialog.findViewById(R.id.setTimeLayout);
        setTimeLayout.setVisibility(View.GONE);
        newLayout.setVisibility(View.GONE);
        customerName.setText(order.getAddress().getName());
        addressLine1.setText(order.getAddress().getStreetName()+ " " + order.getAddress().getHouseNumber() );
        addressLine2.setText(order.getAddress().getZipCode() + " " + order.getAddress().getCity());
        phoneNumber.setText(order.getAddress().getPhoneNumber());
        deliveryType.setText(order.getType());
        totalPrice.setText(order.getOrderPrice() + "€");
        paymentStatus.setText(order.getPaymentStatus());
        deliveryTime.setText(order.getDeliveryTime());
        xButton.setVisibility(View.VISIBLE);
        NewOrderItem newOrderItem = new NewOrderItem(context, order.getItems());
        orderMenuItemsRV.setAdapter(newOrderItem);
        orderMenuItemsRV.setLayoutManager(new LinearLayoutManager(context));

        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });


    }


}
