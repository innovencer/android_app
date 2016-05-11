package com.advante.golazzos.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.advante.golazzos.ConfirmarActivity;
import com.advante.golazzos.EstadisticasActivity;
import com.advante.golazzos.Helpers.General;
import com.advante.golazzos.Helpers.GraphicsUtil;
import com.advante.golazzos.Model.Partido;
import com.advante.golazzos.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ruben Flores on 4/22/2016.
 */
public class List_Partidos extends ArrayAdapter<Partido> {
    ArrayList<Partido> _items;
    Context context;
    int editLocal[],editVisitante[];
    int viewLayout;
    public List_Partidos(Context context, ArrayList<Partido> items,int resourse) {
        super(context, R.layout.item_partido_1,items);
        this._items = items;
        this.context = context;
        editLocal = new int[items.size()];
        editVisitante = new int[items.size()];
        this.viewLayout = resourse;
    }

    @Override
    public int getCount() {
        return _items.size();
    }

    @Override
    public Partido getItem(int position) {
        return this._items.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Partido item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(viewLayout, parent, false);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textDia = (TextView) convertView.findViewById(R.id.textDia);
            viewHolder.textHora = (TextView) convertView.findViewById(R.id.textHora);
            viewHolder.textMins = (TextView) convertView.findViewById(R.id.textMins);
            viewHolder.textTorneo = (TextView) convertView.findViewById(R.id.textTorneo);
            viewHolder.textFecha = (TextView) convertView.findViewById(R.id.textFecha);
            viewHolder.imageEquipo1 = (ImageView) convertView.findViewById(R.id.imageEquipo1);
            viewHolder.imageEquipo2 = (ImageView) convertView.findViewById(R.id.imageEquipo2);
            viewHolder.editLocal = (TextView) convertView.findViewById(R.id.editLocal);
            viewHolder.editVisitante = (TextView) convertView.findViewById(R.id.editVisitante);
            viewHolder.buttonEstadisticas = (LinearLayout) convertView.findViewById(R.id.buttonEstadisticas);
            viewHolder.buttonJugar = (LinearLayout) convertView.findViewById(R.id.buttonJugar);
            viewHolder.textPuntosJuegas = (TextView) convertView.findViewById(R.id.textPuntosJuegas);
            viewHolder.textPuntosGanas = (TextView) convertView.findViewById(R.id.textPuntosGanas);
            viewHolder.buttonUpLocal = (LinearLayout) convertView.findViewById(R.id.buttonUpLocal);
            viewHolder.buttonUpVisitante = (LinearLayout) convertView.findViewById(R.id.buttonUpVisitante);
            viewHolder.buttonDownLocal = (LinearLayout) convertView.findViewById(R.id.buttonDownLocal);
            viewHolder.buttonDownVisitante = (LinearLayout) convertView.findViewById(R.id.buttonDownVisitante);
            viewHolder.textEquipoLocal = (TextView) convertView.findViewById(R.id.textEquipoLocal);
            viewHolder.textEquipoVisitante = (TextView) convertView.findViewById(R.id.textEquipoVisitante);
            viewHolder.buttonEstadisticas = (LinearLayout) convertView.findViewById(R.id.buttonEstadisticas);
            viewHolder.edit = (TextView) convertView.findViewById(R.id.edit);
            viewHolder.buttonDown = (LinearLayout) convertView.findViewById(R.id.buttonDown);
            viewHolder.buttonUp = (LinearLayout) convertView.findViewById(R.id.buttonUp);

            convertView.setTag(viewHolder);

        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        long diff[] = getDifference(item.getStart_time_utc());
        holder.textDia.setText(""+diff[0]);
        holder.textHora.setText(""+diff[1]);
        holder.textMins.setText(""+diff[2]);
        holder.textTorneo.setText(item.getTournament().getName());
        SimpleDateFormat format = new SimpleDateFormat("MMM dd hh:mm aaa");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = format2.parse(item.getStart_time_utc().replace("T", " ").replace("Z", ""));
            holder.textFecha.setText(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String imagePath = item.getLocal().getImage_path();
        final String tempPathV,tempPathL;
        imagePath = imagePath.substring(imagePath.lastIndexOf("/")+1,imagePath.lastIndexOf("-"));
        tempPathL = imagePath;
        File file = new File(General.local_dir_images + "equipos/" + imagePath + ".gif");

        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            GraphicsUtil graphicUtil = new GraphicsUtil();
            holder.imageEquipo1.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
        } else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.rect_white);
            GraphicsUtil graphicUtil = new GraphicsUtil();
            holder.imageEquipo1.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
        }

        imagePath = item.getVisitante().getImage_path();
        imagePath = imagePath.substring(imagePath.lastIndexOf("/") + 1, imagePath.lastIndexOf("-"));
        tempPathV = imagePath;
        file = new File(General.local_dir_images + "equipos/" + imagePath + ".gif");

        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            GraphicsUtil graphicUtil = new GraphicsUtil();
            holder.imageEquipo2.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
        } else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.rect_white);
            GraphicsUtil graphicUtil = new GraphicsUtil();
            holder.imageEquipo2.setImageBitmap(graphicUtil.getCircleBitmap(
                    bm, 16));
        }

        if(viewLayout == R.layout.item_partido_1){
            holder.editLocal.setText("" + editLocal[position]);
            holder.editVisitante.setText("" + editVisitante[position]);
        }else{
            holder.editLocal.setText("" + item.getLocal_score());
            holder.editVisitante.setText("" + item.getVisitant_score());
        }


        holder.textPuntosJuegas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Puntos a Jugar")
                        .setSingleChoiceItems(General.pointsToBet, 0, null)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                if (selectedPosition >= 0) {
                                    holder.textPuntosJuegas.setText(General.pointsToBet[selectedPosition]);
                                    holder.textPuntosGanas.setText(General.pointsToWin[selectedPosition]);
                                }
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        holder.buttonUpLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int val = Integer.parseInt(holder.editLocal.getText().toString()) + 1;
                holder.editLocal.setText("" + val);
                editLocal[position] = val;
            }
        });
        holder.buttonDownLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(holder.editLocal.getText().toString())>0){
                    int val = Integer.parseInt(holder.editLocal.getText().toString()) - 1;
                    holder.editLocal.setText(""+val);
                    editLocal[position] = val;
                }
            }
        });
        holder.edit.setText(General.resultTypes[0]);
        holder.buttonUpVisitante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int val = Integer.parseInt(holder.editVisitante.getText().toString()) + 1;
                holder.editVisitante.setText(""+val);
                editVisitante[position]=val;
            }
        });
        holder.buttonDownVisitante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(holder.editLocal.getText().toString()) > 0) {
                    int val = Integer.parseInt(holder.editVisitante.getText().toString()) - 1;
                    holder.editVisitante.setText("" + val);
                    editVisitante[position] = val;
                }
            }
        });

        holder.textEquipoLocal.setText(item.getLocal().getName());
        holder.textEquipoVisitante.setText(item.getVisitante().getName());

        holder.buttonEstadisticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EstadisticasActivity.class);
                intent.putExtra("html_center_url", item.getHtml_center_url());
                context.startActivity(intent);
            }
        });
        final String finalImagePath = imagePath;
        holder.buttonJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ConfirmarActivity.class);
                int local = Integer.parseInt(holder.editLocal.getText().toString());
                int visitante = Integer.parseInt(holder.editVisitante.getText().toString());

                intent.putExtra("match_id", item.getId());
                intent.putExtra("amount_centavos", Integer.parseInt(holder.textPuntosJuegas.getText().toString()));
                intent.putExtra("local_score", local);
                intent.putExtra("visitant_score", visitante);
                intent.putExtra("nombreLocal",item.getLocal().getName());
                intent.putExtra("nombreVisitante",item.getVisitante().getName());
                if(viewLayout == R.layout.item_partido_1){
                    intent.putExtra("bet_option_id", 0);
                    if(local > visitante){
                        intent.putExtra("image", tempPathL);
                    }else if(visitante > local){
                        intent.putExtra("image", tempPathV);
                    }else{
                        intent.putExtra("image", "-1");
                    }
                }else if(holder.edit.getText().equals(General.resultTypes[0])){
                    intent.putExtra("bet_option_id", 1);
                    intent.putExtra("local_score", 1);
                    intent.putExtra("visitant_score", 0);
                    intent.putExtra("image", tempPathL);
                }else if(holder.edit.getText().equals(General.resultTypes[1])){
                    intent.putExtra("bet_option_id", 1);
                    intent.putExtra("local_score", 0);
                    intent.putExtra("visitant_score", 1);
                    intent.putExtra("image", tempPathV);
                }else{
                    intent.putExtra("bet_option_id", 1);
                    intent.putExtra("local_score", 0);
                    intent.putExtra("visitant_score", 0);
                    intent.putExtra("image", "-1");
                }


                context.startActivity(intent);
            }
        });
        holder.buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.edit.getText().equals(General.resultTypes[0])){
                    holder.edit.setText(General.resultTypes[1]);
                }else if(holder.edit.getText().equals(General.resultTypes[1])){
                    holder.edit.setText(General.resultTypes[2]);
                }else{
                    holder.edit.setText(General.resultTypes[0]);
                }
            }
        });
        holder.buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.edit.getText().equals(General.resultTypes[0])){
                    holder.edit.setText(General.resultTypes[2]);
                }else if(holder.edit.getText().equals(General.resultTypes[1])){
                    holder.edit.setText(General.resultTypes[0]);
                }else{
                    holder.edit.setText(General.resultTypes[1]);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView textDia;
        TextView textHora;
        TextView textMins;
        TextView textTorneo;
        TextView textFecha;
        TextView editLocal;
        TextView editVisitante;
        TextView textPuntosJuegas;
        TextView textPuntosGanas;
        ImageView imageEquipo1;
        ImageView imageEquipo2;
        LinearLayout buttonEstadisticas;
        LinearLayout buttonJugar;
        LinearLayout buttonUpLocal;
        LinearLayout buttonUpVisitante;
        LinearLayout buttonDownLocal;
        LinearLayout buttonDownVisitante;
        TextView textEquipoLocal;
        TextView textEquipoVisitante;
        TextView edit;
        LinearLayout buttonUp;
        LinearLayout buttonDown;
    }


    public long[] getDifference(String date){
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date endDate = c.getTime();
            Date startDate = format.parse(date.replace("T", " ").replace("Z", ""));
            long different = startDate.getTime() - endDate.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

        /*System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);*/
            long time[] = new long[]{elapsedDays,elapsedHours,elapsedMinutes};
            return  time;
        }catch(Exception e){
            e.printStackTrace();
            long time[] = new long[]{0,0,0};
            return time;
        }
    }

}
