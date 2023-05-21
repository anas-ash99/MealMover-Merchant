package com.example.mealmovers_merchant.main.use_cases;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mealmovers_merchant.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class FireBaseCase {

    private final Context context;
    private final MediaPlayer mediaPlayer;
    FirebaseFirestore db  = FirebaseFirestore.getInstance();
    CollectionReference collectionRef = db.collection("orders");
    CollectionReference orderStatusCollectionRef = db.collection("order status");
    public FireBaseCase(Context context) {
        this.context = context;

        mediaPlayer = MediaPlayer.create(context,R.raw.mewe);
    }



    public void listingToNewOrders() {
        try {
            collectionRef.addSnapshotListener((Executor) this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot orders, @Nullable FirebaseFirestoreException error) {

                    if (error != null){
                        Log.e("fireStore", error.getMessage() );
                        Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    if (orders != null) {
                        for (QueryDocumentSnapshot value : orders) {
                            try {
                                if (value.get("restaurant_id").equals("641a35f33f50168a64ca2f68") && value.get("status").equals("new")) {
//                                    order = value.toObject(OrderModel.class);
//                                    mainViewModel.getNotConfirmedOrders().add(0, order);
//                                    initNotConfirmedOrders();
//                                    orderId = value.getId();
//                                    db.collection("orders").document(orderId).delete();
//                                    ordersRepo.updateOrderStatus(getApplication(), order.get_id(), "received");
//                                    db.collection("orders").document(orderId).update("status", "received");
////                                    createNotification();
                                    mediaPlayer.start();
                                    break;
                                }

                            }catch (Exception e){
                                Log.e("order line 332", e.toString());

                            }
                        }
                    }
                }
            });
        }catch (Exception e){
            Log.e("new order", e.toString());

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
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        });


    }






}
