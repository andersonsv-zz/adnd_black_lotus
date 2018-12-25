package br.com.andersonsv.blacklotus.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.holder.CostImageViewHolder;
import br.com.andersonsv.blacklotus.model.CardColor;

public class CostAdapter extends RecyclerView.Adapter<CostImageViewHolder> {
    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<String> mData;

    public CostAdapter(Context context, List<String> data) {
        mContext = context;
        mData = data;
        this.mInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public CostImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_cost_image, parent, false);

        return new CostImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CostImageViewHolder holder, int i) {

        String data = mData.get(i);

        if (data.matches("[a-zA-Z]")) {

            CardColor cardColor = CardColor.getById(data);
            Drawable image = mContext.getDrawable(cardColor.getImage());

            holder.getmCostImage().setImageDrawable(image);
        }

        if (data.matches("[0-9]")) {

            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
            shape.setColor(Color.GRAY);

            TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setDither(true);
            Canvas canvas = new Canvas();
            canvas.drawColor(0xff0000aa);
            canvas.drawText(data, 20 / 2, 20 / 2,
                    mTextPaint);

            shape.draw(canvas);


            holder.getmCostImage().setImageDrawable(shape);
        }

    }



    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }
}
