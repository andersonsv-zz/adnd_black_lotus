package br.com.andersonsv.blacklotus.util;

import android.content.res.Resources;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.firebase.CardModel;

public class CsvWriter {

    private static final String SEPARATOR = ";";
    private static final String CSV_DECK = "csv_deck_";
    private static final String CSV_EXTENSION = ".csv";
    private static final String NEW_LINE = "\n";
    public static final String TYPE_CSV = "text/csv";

    public static File generateCsvFile(File target, String file, List<CardModel> cardModelList, Resources resources){
        try {
            File mFile = new File(target, CSV_DECK + file + CSV_EXTENSION);
            mFile.createNewFile();

            BufferedWriter br = new BufferedWriter(new FileWriter(mFile));
            StringBuilder sb = new StringBuilder();

            sb.append(resources.getString(R.string.csv_card_name));
            sb.append(CsvWriter.SEPARATOR);
            sb.append(resources.getString(R.string.csv_quantity));
            sb.append(CsvWriter.SEPARATOR);
            sb.append(resources.getString(R.string.csv_rarity));
            sb.append(NEW_LINE);

            for (CardModel cardModel : cardModelList) {
                sb.append(cardModel.getName());
                sb.append(CsvWriter.SEPARATOR);
                sb.append(cardModel.getQuantity());
                sb.append(CsvWriter.SEPARATOR);
                sb.append(cardModel.getRarity());
                sb.append(NEW_LINE);
            }
            br.write(sb.toString());
            br.close();

            return mFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
