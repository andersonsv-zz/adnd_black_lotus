package br.com.andersonsv.blacklotus.util;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import br.com.andersonsv.blacklotus.firebase.CardModel;

public class CsvWriter {
    public static void generateCsvFile(List<CardModel> cards, File csv){
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(csv));

            StatefulBeanToCsv<CardModel> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .build();

            beanToCsv.write(cards);
        }catch (Exception exception) {
            exception.printStackTrace();
        } finally {

        }

    }
}
