package com.advante.golazzos;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.advante.golazzos.Helpers.General;
import com.android.volley.toolbox.JsonArrayRequest;

import java.text.DecimalFormat;
import java.util.List;

import io.npay.activity.NPay;
import io.npay.check_subscription.OnCheckSubscriptionListener;
import io.npay.hub.service_detail.OnServiceDetailReceivedListener;
import io.npay.hub.service_detail.ServiceDetailItem;
import io.npay.hub.services.OnServiceItemReceivedListener;
import io.npay.hub.services.ServiceItem;
import io.npay.hub_cancel_subscription.CancelResponse;
import io.npay.hub_cancel_subscription.OnSubscriptionCancelledListener;
import io.npay.hub_subscriptions.OnSubscriptionCreatedListener;
import io.npay.hub_subscriptions.SubscriptionResponse;

/**
 * Created by Naranya on 31/01/2016.
 */
public class ServiceDetailActivity extends Activity implements OnServiceDetailReceivedListener {
    JsonArrayRequest jsArrayRequest;
    private Toolbar toolbar;
    private NPay npay;
    private String id_service;

    private OnSubscriptionCreatedListener listenCreate;
    private OnSubscriptionCancelledListener listenCancel;
    private OnCheckSubscriptionListener listenSub;

    LinearLayout linearSerTitular, linearSerTitular2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_npay);

        id_service = "dcc65262bcd170c684d98cf084fc9a4e";
        Log.i("ID_SERVICE", id_service);

        npay = new NPay(this);

        listenCreate = new OnSubscriptionCreatedListener() {
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

        listenCancel = new OnSubscriptionCancelledListener() {
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

        OnServiceItemReceivedListener listen = new OnServiceItemReceivedListener() {
            @Override
            public void onServiceItemReceivedListener(List<ServiceItem> result) {
                DecimalFormat f = new DecimalFormat("#.00");
                try {
                    for (ServiceItem service : result) {
                        System.out.println(String.valueOf(service.getName()));
                        System.out.println(String.valueOf(service.getDescription()));
                        System.out.println(String.valueOf(service.getHubDetails().getCountry()));
                        System.out.println(String.valueOf(service.getHubDetails().getCarrier()));
                        System.out.println(f.format( service.getHubDetails().getAmount()));
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        };

        listenSub = new OnCheckSubscriptionListener() {
            @Override
            public void onFinishCheckResponse(String s) {
                Log.d("golazz",s);
            }
        };

        linearSerTitular = (LinearLayout) findViewById(R.id.linearSerTitular);
        linearSerTitular2 = (LinearLayout) findViewById(R.id.linearSerTitular2);

        linearSerTitular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                npay.CreateSubscription(id_service, General.KEYWORD, General.MEDIA);
            }
        });
        linearSerTitular2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                npay.CancelSubscription().CancelSubscription("e78ae033116118775bf03c955657e861", id_service);
            }
        });

        npay.setOnServiceDetailReceivedListener(this);

        npay.setOnSubscriptionCreatedListener(listenCreate);
        npay.setOnSubscriptionCancelledListener(listenCancel);
        npay.setOnCheckSubscriptionListener(listenSub);
        npay.setOnServiceItemReceivedListener(listen);

        npay.getServices().getServices();
        npay.checkSubscription(id_service);
        npay.getServiceDetails().getServiceDetails(id_service);
    }

    @Override
    public void onServiceDetailReceivedListener(ServiceDetailItem result) {
        try {
            String information =
                    "Información detallada" + "\n" + "\n" +
                    "Nombre: " + result.getName() + "\n" +
                            "Descripción: " + result.getDescription() + "\n" +
                            "Pais: " + result.getCountry() + "\n" +
                            "Carrier: " + result.getCarrier() + "\n" +
                            "Cantidad: " + result.getAmount() + "\n" +
                            "SubTotal: " + result.getSubTotal() + "\n" +
                            "Impuesto: " + result.getTax() + "\n" +
                            "Intervalo: " + result.getInterval() + "\n" +
                            "Intervalo contador: " + result.getIntervalCount() + "\n" +
                            "Intervalo caducado: " + result.getIntervalExpire() + "\n" +
                            "Moneda: " + result.getCurrency() + "\n" +
                            "Codigo referencia: " + result.getReferenceCode() + "\n" +
                            "Doble Opt In: " + result.getDoubleOptIn() + "\n" +
                            "Tipo: " + result.getType();
            Toast.makeText(this,information,Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}