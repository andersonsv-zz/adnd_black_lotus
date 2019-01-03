package br.com.andersonsv.blacklotus.provider;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.firebase.CardModel;
import br.com.andersonsv.blacklotus.firebase.DeckModel;
import br.com.andersonsv.blacklotus.service.DeckWidgetService;

import static br.com.andersonsv.blacklotus.util.Constants.CARD_LIST;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_PARCELABLE;

public class DeckWidgetProvider extends AppWidgetProvider {

    public static List<CardModel> cards = new ArrayList<>();
    private static String text;

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_card_provider);
        views.setTextViewText(R.id.textViewDeckName, text);

        Intent intent = new Intent(context, DeckWidgetService.class);
        views.setRemoteAdapter(R.id.listViewCard, intent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null ) {
            if (intent.hasExtra(DECK_PARCELABLE)) {
                DeckModel deck = intent.getParcelableExtra(DECK_PARCELABLE);
                text = deck.getName();
            }
            if (intent.hasExtra(CARD_LIST)) {
                cards = intent.getParcelableArrayListExtra(CARD_LIST);
            }
        } else {
            //text = context.getString(R.string.widget_no_recipe_selected);
            text = "No cards";
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName widget = new ComponentName(context.getApplicationContext(), DeckWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(widget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewCard);
        if (appWidgetIds != null && appWidgetIds.length > 0) {
            onUpdate(context, appWidgetManager, appWidgetIds);
        }

        super.onReceive(context, intent);
    }
}
