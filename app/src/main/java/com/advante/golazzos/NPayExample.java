package com.advante.golazzos;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.advante.golazzos.Helpers.General;

import java.util.List;

import io.npay.activity.NPay;
import io.npay.hub.service_detail.OnServiceDetailReceivedListener;
import io.npay.hub.service_detail.ServiceDetailItem;
import io.npay.hub.services.OnServiceItemReceivedListener;
import io.npay.hub.services.ServiceItem;
import io.npay.hub_cancel_subscription.CancelResponse;
import io.npay.hub_cancel_subscription.OnSubscriptionCancelledListener;
import io.npay.hub_subscriptions.OnSubscriptionCreatedListener;
import io.npay.hub_subscriptions.SubscriptionResponse;

/**
 * Created by Ruben Flores on 5/11/2016.
 */
public class NPayExample extends Activity {
    //global variables for Demo
    public String sku;
    public int items_qty = 0;
    LinearLayout linearSerTitular, linearSerTitular2;
    ImageView image1;
    NPay npay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_npay);


        //Initialize NPay Object
        npay = new NPay(this);

        linearSerTitular = (LinearLayout) findViewById(R.id.linearSerTitular);
        linearSerTitular2 = (LinearLayout) findViewById(R.id.linearSerTitular2);

        npay.setOnServiceDetailReceivedListener(onServiceDetailReceivedListener);

        npay.setOnSubscriptionCreatedListener(onCreateListener);
        npay.setOnSubscriptionCancelledListener(onCancelListener);

        linearSerTitular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                npay.CreateSubscription("dcc65262bcd170c684d98cf084fc9a4e", General.KEYWORD, General.MEDIA);
            }
        });
        linearSerTitular2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                npay.CancelSubscription().CancelSubscription("dcc65262bcd170c684d98cf084fc9a4e", General.KEYWORD);
            }
        });

    }

    OnSubscriptionCreatedListener onCreateListener = new OnSubscriptionCreatedListener(){

        @Override
        public void onSubscriptionCreatedListener(SubscriptionResponse result) {
            try {
                Log.e("---CREATE---", "*--------------------*");
                Log.v("Object", result.getObject());
                Log.v("Created", result.getCreated());
                Log.v("First Charge", result.getFirstCharge());
                Log.v("Last Charge", result.getLastCharge());
                Log.v("Next Charge", result.getNextCharge());
                Log.v("Customer ID", result.getIdCustomer());
                Log.v("Service ID", result.getIDService());
                Log.v("Cancelled", result.getCancelled());
                Log.v("Status", result.getStatus());
                //Querys.addSubscription(ServiceDetailActivity.this, result.getIdSubscription(), result.getIDService());
                Toast toast = Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG);
            } catch(Exception e){
                e.printStackTrace();
                Toast toast = Toast.makeText(getApplicationContext(), "Toast del lado de la app - Object is empty", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        @Override
        public void onCancel() {

        }
    };
    OnSubscriptionCancelledListener onCancelListener = new OnSubscriptionCancelledListener() {
        @Override
        public void onSubscriptionCancelledListener(CancelResponse resultItem) {
            try {
                Log.e("---CANCEL---", "*--------------------*");
                Log.v("Object", resultItem.getObject());
                Log.v("Created", resultItem.getCreated());
                Log.v("First Charge", resultItem.getFirstCharge());
                Log.v("Last Charge", resultItem.getLastCharge());
                Log.v("Next Charge", resultItem.getNextCharge());
                Log.v("Customer ID", resultItem.getIdCustomer());
                Log.v("Service ID", resultItem.getIDService());
                Log.v("Cancelled", resultItem.getCancelled());
                Log.v("Status", resultItem.getStatus());
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    };

    OnServiceItemReceivedListener onServiceItemReceivedListener = new OnServiceItemReceivedListener(){
        @Override
        public void onServiceItemReceivedListener(List<ServiceItem> result) {
            for(ServiceItem serviceItem:result){
                Log.e("Golazzos", serviceItem.getIdService() +" "+serviceItem.getName());
            }
        }
    };
    OnServiceDetailReceivedListener onServiceDetailReceivedListener = new OnServiceDetailReceivedListener(){
        @Override
        public void onServiceDetailReceivedListener(ServiceDetailItem result) {
            Log.e("Golazzos", ""+result.getSubTotal());
        }
    };

}
