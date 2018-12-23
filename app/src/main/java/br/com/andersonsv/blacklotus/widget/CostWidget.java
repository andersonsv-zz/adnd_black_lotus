package br.com.andersonsv.blacklotus.widget;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.model.CardColor;

public class CostWidget extends android.support.v7.widget.AppCompatImageView {

    private String item;

    public CostWidget(Context context) {
        super(context);
    }

    public CostWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CostImageView);
        item = a.getString(R.styleable.CostImageView_item);
    }

    public CostWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (item.matches("[A-Z][a-z]+")) {

            CardColor cardColor = CardColor.getById(item);
            Drawable image = getContext().getDrawable(cardColor.getImage());

            Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), cardColor.getImage());
            Paint paint =new Paint();
            canvas.drawColor(Color.BLACK);
            canvas.drawBitmap(mBitmap, 0, 0, paint);
        }

        if (item.matches("[0-9]+")) {
            canvas.drawColor(Color.WHITE);

            //1
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.GRAY);
            RectF oval1 = new RectF(0, 0, 250,250);

            Paint p1 = new Paint();
            p1.setColor(Color.BLACK);

            canvas.drawText(item, 30, 50, p1);
            canvas.drawOval(oval1, paint);
        }

    }
}
