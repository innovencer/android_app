package com.advante.golazzos.Helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Ruben Flores on 5/25/2016.
 */
public class Bar extends View {
    Paint paint = new Paint();
    int porcentaje = 10;
    int colors[] = new int[2];

    public Bar(Context context) {
        super(context);
    }

    public Bar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(colors[0]);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(canvas.getHeight());

        int total = canvas.getWidth();
        int firstColor = (total * porcentaje) / 100;
        canvas.drawLine(0, 0, firstColor, 0, paint);
        paint.setColor(colors[1]);
        canvas.drawLine(firstColor, 0, canvas.getWidth(), 0, paint);

    }
}
