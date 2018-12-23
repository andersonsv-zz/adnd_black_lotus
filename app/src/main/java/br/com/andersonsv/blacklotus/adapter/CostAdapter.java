package br.com.andersonsv.blacklotus.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        Canvas canvas = new Canvas();

        if (data.matches("\\{[a-zA-Z]\\}")) {

            data = data.replaceAll("\\{", "");
            data = data.replaceAll("\\}", "");

            CardColor cardColor = CardColor.getById(data);
            Drawable image = mContext.getDrawable(cardColor.getImage());

            holder.getmCostImage().setImageDrawable(image);
        }

        if (data.matches("\\{[0-9]\\}")) {
            canvas.drawColor(Color.WHITE);

            //1
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.GRAY);
            RectF oval1 = new RectF(0, 0, 250,250);

            Paint p1 = new Paint();
            p1.setColor(Color.BLACK);

            canvas.drawText(data, 30, 50, p1);
            canvas.drawOval(oval1, paint);
        }


        //holder.getmCostImage().setImageDrawable(canvas.);
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }
}
