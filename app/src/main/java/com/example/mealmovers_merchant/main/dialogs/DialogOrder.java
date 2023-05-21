package com.example.mealmovers_merchant.main.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmovers_merchant.R;
import com.example.mealmovers_merchant.main.MainActivity;
import com.example.mealmovers_merchant.main.models.OrderModel;

import java.time.LocalDateTime;

public class DialogOrder  {
    private MainActivity activity;
    public Dialog dialog;
    MutableLiveData<Integer> selectedEstimationTime = new MutableLiveData<>(0);
    LocalDateTime deliverTime = LocalDateTime.now();
    CardView thirtyMinCard, fortyMinCard, sixtyMinCard, eightyMinCard, hundredMinCard, confirmButton;
    TextView thirtyMinTV, fortyMinTV, sixtyMinTV, eightyMinTV,hundredMinTV;
    TextView deliveryTime, paymentStatus, totalPrice, phoneNumber, deliveryType, addressLine2, addressLine1, customerName;
    RecyclerView orderMenuItemsRV;


    public DialogOrder(MainActivity activity) {
        this.activity = activity;
        initDialog();
    }


    private void showNewOrderDialog(OrderModel order) {

        if (dialog.isShowing()){
            dialog.hide();
        }
        activity.initOrderItemsRV();
        selectEstimationTime();
        activity.handleConfirmOrderClick();

        if (order.getScheduled().equals(true)){
            dialog.findViewById(R.id.timeHeaderTv).setVisibility(View.GONE);
            dialog.findViewById(R.id.timeList).setVisibility(View.GONE);
            confirmButton.setCardBackgroundColor(activity.getColor(R.color.teal_200));
        }else{
            dialog.findViewById(R.id.timeHeaderTv).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.timeList).setVisibility(View.VISIBLE);
            confirmButton.setCardBackgroundColor(activity.getColor(R.color.secondary_background));
        }

        dialog.show();

    }




    private void selectEstimationTime() {
        selectedEstimationTime.observe(activity, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (selectedEstimationTime.getValue()){
                    case 0:
                        thirtyMinCard.setCardBackgroundColor(activity.getColor(R.color.white));
                        fortyMinCard.setCardBackgroundColor(Color.WHITE);
                        sixtyMinCard.setCardBackgroundColor(Color.WHITE);
                        eightyMinCard.setCardBackgroundColor(Color.WHITE);
                        hundredMinCard.setCardBackgroundColor(Color.WHITE);
                        confirmButton.setCardBackgroundColor(activity.getColor(R.color.secondary_background));
                        break;
                    case 30:
//                        thirtyMinTV.setTextColor(activity.getColor(R.color.white));
                        thirtyMinCard.setCardBackgroundColor(activity.getColor(R.color.teal_200));
                        fortyMinCard.setCardBackgroundColor(Color.WHITE);
                        sixtyMinCard.setCardBackgroundColor(Color.WHITE);
                        eightyMinCard.setCardBackgroundColor(Color.WHITE);
                        hundredMinCard.setCardBackgroundColor(Color.WHITE);
                        confirmButton.setCardBackgroundColor(activity.getColor(R.color.teal_200));
                        break;
                    case 40:
                        fortyMinCard.setCardBackgroundColor(activity.getColor(R.color.teal_200));
                        thirtyMinCard.setCardBackgroundColor(Color.WHITE);
                        sixtyMinCard.setCardBackgroundColor(Color.WHITE);
                        eightyMinCard.setCardBackgroundColor(Color.WHITE);
                        hundredMinCard.setCardBackgroundColor(Color.WHITE);
                        confirmButton.setCardBackgroundColor(activity.getColor(R.color.teal_200));
                        break;
                    case 60:
                        sixtyMinCard.setCardBackgroundColor(activity.getColor(R.color.teal_200));
                        thirtyMinCard.setCardBackgroundColor(Color.WHITE);
                        fortyMinCard.setCardBackgroundColor(Color.WHITE);
                        eightyMinCard.setCardBackgroundColor(Color.WHITE);
                        hundredMinCard.setCardBackgroundColor(Color.WHITE);
                        confirmButton.setCardBackgroundColor(activity.getColor(R.color.teal_200));
                        break;

                    case 80:
                        eightyMinCard.setCardBackgroundColor(activity.getColor(R.color.teal_200));
                        thirtyMinCard.setCardBackgroundColor(Color.WHITE);
                        fortyMinCard.setCardBackgroundColor(Color.WHITE);
                        sixtyMinCard.setCardBackgroundColor(Color.WHITE);
                        hundredMinCard.setCardBackgroundColor(Color.WHITE);
                        confirmButton.setCardBackgroundColor(activity.getColor(R.color.teal_200));
                        break;

                    case 100:
                        hundredMinCard.setCardBackgroundColor(activity.getColor(R.color.teal_200));
                        thirtyMinCard.setCardBackgroundColor(Color.WHITE);
                        fortyMinCard.setCardBackgroundColor(Color.WHITE);
                        sixtyMinCard.setCardBackgroundColor(Color.WHITE);
                        eightyMinCard.setCardBackgroundColor(Color.WHITE);
                        confirmButton.setCardBackgroundColor(activity.getColor(R.color.teal_200));
                        break;

                }

            }
        });


        thirtyMinCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliverTime = LocalDateTime.now().plusMinutes(30);
                selectedEstimationTime.setValue(30);
            }
        });

        fortyMinCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedEstimationTime.setValue(40);
                deliverTime = LocalDateTime.now().plusMinutes(40);
            }
        });

        sixtyMinCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedEstimationTime.setValue(60);
                deliverTime = LocalDateTime.now().plusMinutes(60);
            }
        });
        eightyMinCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedEstimationTime.setValue(80);
                deliverTime = LocalDateTime.now().plusMinutes(80);
            }
        });
        hundredMinCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliverTime = LocalDateTime.now().plusMinutes(100);
                selectedEstimationTime.setValue(100);
            }
        });


    }
    private void initDialog(){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.order_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
        confirmButton = dialog.findViewById(R.id.confirm_button);
        thirtyMinCard = dialog.findViewById(R.id.thirtyMinCard);
        fortyMinCard = dialog.findViewById(R.id.fortyMinCard);
        sixtyMinCard = dialog.findViewById(R.id.sixtyMinCard);
        eightyMinCard = dialog.findViewById(R.id.eightyMinCard);
        hundredMinCard = dialog.findViewById(R.id.hundredMinCard);
        orderMenuItemsRV = dialog.findViewById(R.id.itemsRecyclerView);
        deliveryTime = dialog.findViewById(R.id.deliveryTime);
        paymentStatus = dialog.findViewById(R.id.paymentStatus);
        totalPrice = dialog.findViewById(R.id.totalPrice);
        deliveryType = dialog.findViewById(R.id.deliveryType);
        phoneNumber = dialog.findViewById(R.id.phoneNumber);
        addressLine2 = dialog.findViewById(R.id.addressLine2);
        addressLine1 = dialog.findViewById(R.id.addressLine1);
        customerName = dialog.findViewById(R.id.customerName);
        thirtyMinTV = dialog.findViewById(R.id.thirtyMinTV);
    }
}
