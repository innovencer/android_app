package com.advante.golazzos.Fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GeneralFragment;
import com.advante.golazzos.Helpers.VolleySingleton;
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

/**
 * Created by Ruben Flores on 5/25/2016.
 */
public class Notificaciones_Fragment extends GeneralFragment {
    JsonObjectRequest jsArrayRequest;
    TextView textInfo,textNotificaciones,textCuenta;
    ImageView imageProfile;
    LinearLayout linear1, linear2, linear3, linearGuardar;
    ToggleButton toggle1, toggle2, toggle3, toggle4, toggle5;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificaciones, container, false);

        textInfo = (TextView) view.findViewById(R.id.textInfo);
        textNotificaciones = (TextView) view.findViewById(R.id.textNotificaciones);
        textCuenta = (TextView) view.findViewById(R.id.textCuenta);

        linear1 = (LinearLayout) view.findViewById(R.id.linear1);
        linear2 = (LinearLayout) view.findViewById(R.id.linear2);
        linear3 = (LinearLayout) view.findViewById(R.id.linear3);
        linearGuardar = (LinearLayout) view.findViewById(R.id.linearGuardar);

        toggle1 = (ToggleButton) view.findViewById(R.id.toggle1);
        toggle2 = (ToggleButton) view.findViewById(R.id.toggle2);
        toggle3 = (ToggleButton) view.findViewById(R.id.toggle3);
        toggle4 = (ToggleButton) view.findViewById(R.id.toggle4);
        toggle5 = (ToggleButton) view.findViewById(R.id.toggle5);

        toggle1.setChecked(gnr.getLoggedUser().getUserSettings().isNew_bet_notification());
        toggle2.setChecked(gnr.getLoggedUser().getUserSettings().isClosed_match_notification());
        toggle3.setChecked(gnr.getLoggedUser().getUserSettings().isWon_notification());
        toggle4.setChecked(gnr.getLoggedUser().getUserSettings().isLose_notification());
        toggle5.setChecked(gnr.getLoggedUser().getUserSettings().isFriendship_notification());


        textInfo.setOnClickListener(onClickInfo);
        linear1.setOnClickListener(onClickInfo);
        linearGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        //textNotificaciones.setOnClickListener(onClickNotificaciones);
        //linear2.setOnClickListener(onClickNotificaciones);

        textCuenta.setOnClickListener(onClickCuenta);
        linear3.setOnClickListener(onClickCuenta);

        return view;
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

    private void save() {
        JSONObject user = new JSONObject();
        JSONObject values = new JSONObject();
        try {
            values.put("won_notification", toggle3.isChecked());
            values.put("lose_notification", toggle4.isChecked());
            values.put("new_bet_notification", toggle1.isChecked());
            values.put("closed_match_notification", toggle2.isChecked());
            values.put("friendship_notification", toggle5.isChecked());
            user.put("user",values);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsArrayRequest = new JsonObjectRequest(
                Request.Method.PUT,
                General.endpoint_users+"/me",
                user,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        gnr.getLoggedUser().getUserSettings().setClosed_match_notification(toggle2.isChecked());
                        gnr.getLoggedUser().getUserSettings().setLose_notification(toggle4.isChecked());
                        gnr.getLoggedUser().getUserSettings().setNew_bet_notification(toggle1.isChecked());
                        gnr.getLoggedUser().getUserSettings().setWon_notification(toggle3.isChecked());
                        gnr.getLoggedUser().getUserSettings().setFriendship_notification(toggle5.isChecked());
                        Toast.makeText(getContext(),"Notificaciones Actualizadas ...",Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Error en al tratar de conectar con el servicio web. Intente mas tarde",Toast.LENGTH_SHORT).show();
                        //dialog.dismiss();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + General.getToken());
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        jsArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsArrayRequest);
    }
}
