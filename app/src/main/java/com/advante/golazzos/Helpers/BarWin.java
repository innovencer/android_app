package com.advante.golazzos.Helpers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.advante.golazzos.R;

/**
 * Created by Ruben Flores on 5/25/2016.
 */
public class BarWin extends View {
    Paint paint = new Paint();
    int porcentaje = 10;

    public BarWin(Context context) {
        super(context);
    }

    public BarWin(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.greenApple));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(canvas.getHeight());

        int total = canvas.getWidth();
        int firstColor = (total * porcentaje) / 100;
        canvas.drawLine(0, 0, firstColor, 0, paint);
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        canvas.drawLine(firstColor, 0, canvas.getWidth(), 0, paint);

    }
}
