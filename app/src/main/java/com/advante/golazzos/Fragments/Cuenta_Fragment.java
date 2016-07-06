package com.advante.golazzos.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.VolleySingleton;
import com.advante.golazzos.Interface.IGetUser_Listener;
import com.advante.golazzos.MainActivity;
import com.advante.golazzos.Model.User;
import com.advante.golazzos.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.npay.activity.NPay;
import io.npay.hub_cancel_subscription.CancelResponse;
import io.npay.hub_cancel_subscription.OnSubscriptionCancelledListener;
import io.npay.hub_subscriptions.OnSubscriptionCreatedListener;
import io.npay.hub_subscriptions.SubscriptionResponse;

/**
 * Created by Ruben Flores on 5/25/2016.
 */
public class Cuenta_Fragment extends GeneralFragment {
    JsonObjectRequest request;
    TextView textInfo,textNotificaciones,textCuenta, textNombre, textTipoUsuario;
    ImageView imageTipoUsua;
    LinearLayout linear1, linear2, linear3, linearTitular, linearSuplente,linearButton1;

    private OnSubscriptionCreatedListener listenCreate;
    private OnSubscriptionCancelledListener listenCancel;
    private NPay npay;

    String idService;
    Boolean flag = true;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            flag = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cuenta, container, false);

        npay = new NPay(getActivity());

        textInfo = (TextView) view.findViewById(R.id.textInfo);
        textNotificaciones = (TextView) view.findViewById(R.id.textNotificaciones);
        textCuenta = (TextView) view.findViewById(R.id.textCuenta);
        textNombre = (TextView) view.findViewById(R.id.textNombre);
        textTipoUsuario = (TextView) view.findViewById(R.id.textTipoUsuario);

        imageTipoUsua = (ImageView) view.findViewById(R.id.imageTipoUsuario);

        linear1 = (LinearLayout) view.findViewById(R.id.linear1);
        linear2 = (LinearLayout) view.findViewById(R.id.linear2);
        linear3 = (LinearLayout) view.findViewById(R.id.linear3);
        linearTitular = (LinearLayout) view.findViewById(R.id.linearTitular);
        linearSuplente = (LinearLayout) view.findViewById(R.id.linearSuplente);
        linearButton1 = (LinearLayout) view.findViewById(R.id.linearButton1);


        gnr.getUser(new IGetUser_Listener() {
            @Override
            public void onComplete(Boolean complete, User user) {
                if(complete){
                    init();
                }
            }
        });

        linearButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                npay.CancelSubscription().CancelSubscription(gnr.getLoggedUser().getSubscription_id(), idService);
            }
        });

        linearSuplente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                npay.CreateSubscription(idService, General.KEYWORD, General.MEDIA);
            }
        });

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
                    Log.v("Subscription", result.getIdSubscription());
                    createSuscrip(result.getIdSubscription());
                    //Querys.addSubscription(ServiceDetailActivity.this, result.getIdSubscription(), result.getIDService());

                    //Toast toast = Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG);
                } catch(Exception e){
                    e.printStackTrace();
                    Toast toast = Toast.makeText(getContext(), "Toast del lado de la app - Object is empty", Toast.LENGTH_LONG);
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
                    cancelSuscrip();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        npay.setOnSubscriptionCreatedListener(listenCreate);
        npay.setOnSubscriptionCancelledListener(listenCancel);
        //textCuenta.setOnClickListener(onClickCuenta);
        //linear3.setOnClickListener(onClickCuenta);

        return view;
    }

    private void init(){
        String nombre = "";
        String name[] = gnr.getLoggedUser().getName().split(" ");
        switch (name.length){
            case 1:
                nombre = name[0];
                break;
            case 2:
                nombre = name[0];
                break;
            case 3:
                nombre = name[0]+ " "+name[1];
                break;
            case 4:
                nombre = name[0] +" "+ name[1];
                break;
            default:
                if (name.length>4){
                    nombre = name[0] +" "+ name[1];
                }
                break;
        }

        textNombre.setText(nombre+",");
        textInfo.setOnClickListener(onClickInfo);
        linear1.setOnClickListener(onClickInfo);

        textNotificaciones.setOnClickListener(onClickNotificaciones);
        linear2.setOnClickListener(onClickNotificaciones);

        if(!gnr.getLoggedUser().getPaid_subscription()){
            linearSuplente.setVisibility(View.VISIBLE);
            linearTitular.setVisibility(View.GONE);
            textTipoUsuario.setText("Suplente");
            imageTipoUsua.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.suplente_icono));
        }else{
            linearTitular.setVisibility(View.VISIBLE);
            linearSuplente.setVisibility(View.GONE);
            textTipoUsuario.setText("Titular");
            imageTipoUsua.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.titular_icono));
        }

        switch (gnr.locale){
            case "COL":
                idService = getString(R.string.npay_co);
                break;
            case "MEX":
                idService = getString(R.string.npay_mx);
                break;
            default:
                idService = getString(R.string.npay_co);
                break;
        }

    }

    View.OnClickListener onClickInfo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, new Perfil_Fragment(), "");
            ft.addToBackStack(null);
            ft.commit();
        }
    };
    View.OnClickListener onClickCuenta = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, new Cuenta_Fragment(), "");
            ft.addToBackStack(null);
            ft.commit();
        }
    };
    View.OnClickListener onClickNotificaciones = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, new Notificaciones_Fragment(), "");
            ft.addToBackStack(null);
            ft.commit();
        }
    };

    private void createSuscrip(final String id){
        dialog.show();
        JSONObject subscription =  new JSONObject();
        JSONObject id_sub = new JSONObject();
        try {
            id_sub.put("id", id);
            subscription.put("subscription", id_sub);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request = new JsonObjectRequest(
                Request.Method.POST,
                General.endpoint_subscription,
                subscription,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        linearTitular.setVisibility(View.VISIBLE);
                        linearSuplente.setVisibility(View.GONE);

                        textTipoUsuario.setText("Titular");
                        imageTipoUsua.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.titular_icono));

                        gnr.getLoggedUser().setSubscription_id(id);
                        gnr.getLoggedUser().setPaid_subscription(true);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        dialog.dismiss();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + General.getToken());
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }
    private void cancelSuscrip(){
        dialog.show();
        request = new JsonObjectRequest(
                Request.Method.DELETE,
                General.endpoint_subscription,
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        linearTitular.setVisibility(View.GONE);
                        linearSuplente.setVisibility(View.VISIBLE);

                        textTipoUsuario.setText("Suplente");
                        imageTipoUsua.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.suplente_icono));

                        gnr.getLoggedUser().setSubscription_id("");
                        gnr.getLoggedUser().setPaid_subscription(false);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                    }
                })
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", "Token " + General.getToken());
                        params.put("Content-Type", "application/json");
                        return params;
                    }
                };
        request.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
    }
}
