package com.example.mealmovers_merchant.main.use_cases.firebase_case;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mealmovers_merchant.R;
import com.example.mealmovers_merchant.main.models.OrderModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Any;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import kotlinx.coroutines.flow.Flow;

public class FireBaseCase {


    private final MediaPlayer mediaPlayer;
    private final Activity activity;
    FirebaseFirestore db  = FirebaseFirestore.getInstance();
    CollectionReference collectionRef = db.collection("orders");
    CollectionReference orderStatusCollectionRef = db.collection("order status");
    public FireBaseCase(Activity activity) {
        this.activity = activity;
        mediaPlayer = MediaPlayer.create(activity,R.raw.mewe);
    }



    public void listingToNewOrders(String restaurant_id, ObserveCallBack<List<OrderModel>> observeCallBack){
        try {

            collectionRef.orderBy("created_at", Query.Direction.ASCENDING).addSnapshotListener(activity, (orders, error) -> {

             List<OrderModel> allOrders = new ArrayList<>();
                if (error != null){
                    Log.e("fireStore", error.getMessage() );
                    Toast.makeText(activity, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }

                if (orders != null) {

                    for (QueryDocumentSnapshot value : orders) {
                        if (value.get("restaurant_id").equals(restaurant_id) && value.get("status").equals("new")) {
                            OrderModel order = value.toObject(OrderModel.class);
                            order.setOrderFireStoreId(value.getId());
//                            if (!allOrders.contains(order) && allOrders.size() > 0){
//                                allOrders.add(allOrders.size() - 1, order);
//                            }else if (!allOrders.contains(order) && allOrders.size() == 0){
//
//                            }
                            allOrders.add(0, order);
                        }

                    }
                    observeCallBack.onNewOrders(allOrders);
                }

            });
        }catch (Exception e){
            Log.e("new order",e.getMessage(), e);
            Toast.makeText(activity, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }


    public void sendNotificationOrderStatus(String status, String id,String selectedEstimationTime){
        Map<String, String> notification = new HashMap<>();
        notification.put("order_id", id);
        notification.put("status", status);
        notification.put("confirmationTime", selectedEstimationTime );

        orderStatusCollectionRef.add(notification).addOnSuccessListener(documentReference -> {

        }).addOnFailureListener(e -> {
            Log.e("notification", e.toString());
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        });



    }


    public void deleteItem(String orderId){

        collectionRef.document(orderId).delete();
    }






}


