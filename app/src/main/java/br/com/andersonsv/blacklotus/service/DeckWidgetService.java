package br.com.andersonsv.blacklotus.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.firebase.CardModel;
import br.com.andersonsv.blacklotus.provider.DeckWidgetProvider;
import br.com.andersonsv.blacklotus.util.ImageHtmlUtil;

import static br.com.andersonsv.blacklotus.util.StringUtils.replaceTypetImgSrc;

public class DeckWidgetService extends RemoteViewsService  {
    private List<CardModel> cards;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteListViewsFactory(getApplicationContext());
    }

    class RemoteListViewsFactory implements DeckWidgetService.RemoteViewsFactory {

        final Context mContext;

        RemoteListViewsFactory(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            cards = DeckWidgetProvider.cards;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (cards == null) return 0;
            return cards.size();
        }

        @Override
        public RemoteViews getViewAt(int index) {
            final RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_card_item);
            CardModel card = cards.get(index);
            views.setTextViewText(R.id.textViewCardName, card.getName());
            views.setTextViewText(R.id.textViewQuantity, card.getQuantity().toString());

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
