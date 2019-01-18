package br.com.andersonsv.blacklotus.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.LevelListDrawable;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.model.CardColor;
import br.com.andersonsv.blacklotus.widget.TextDrawable;

public class ImageHtmlUtil {

    private final Context mContext;

    public ImageHtmlUtil(Context context){
        mContext = context;
    }

    public Drawable generate(String source) {
        CardColor cardColor = CardColor.getById(source);
        int size = mContext.getResources().getInteger(R.integer.card_cost_list);

        if (cardColor != null) {
            LevelListDrawable d = new LevelListDrawable();

            Drawable empty =  mContext.getResources().getDrawable(cardColor.getImage());
            d.addLevel(0, 0, empty);
            d.setBounds(0, 0, size, size);

            return d;

        } else {
            //Copied by - https://github.com/devunwired/textdrawable
            TextDrawable textDrawable = new TextDrawable(mContext);

            textDrawable.setText(source);
            textDrawable.setTextColor(Color.BLACK);

            GradientDrawable gD = new GradientDrawable();
            gD.setColor(Color.GRAY);
            gD.setShape(GradientDrawable.OVAL);
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{gD, textDrawable});

            layerDrawable.setBounds(0, 0, size, size);

            return layerDrawable;
        }
    }
}
