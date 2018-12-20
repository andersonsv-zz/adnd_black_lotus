package br.com.andersonsv.blacklotus.util;

import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.widget.ImageView;

import java.util.List;

public class ColorDeckUtil {

    public static void addColorIfNonNull(final String color, final List<Integer> colors){
        if (color != null) {
            int colorValue = Color.parseColor(color);
            colors.add(colorValue);
        }
    }

    public static void setOneColor(int color, final ImageView imagemView){
        imagemView.setImageResource(0);
        imagemView.invalidate();
        ShapeDrawable sd = new ShapeDrawable(new OvalShape());
        sd.setIntrinsicHeight(100);
        sd.setIntrinsicWidth(100);
        sd.getPaint().setColor(color);
        imagemView.setBackground(sd);
    }
}
