package br.com.andersonsv.blacklotus.network;

import br.com.andersonsv.blacklotus.data.Card;
import br.com.andersonsv.blacklotus.data.Cards;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CardService {

    @GET("/cards")
    Call<Cards> getCards(@Query("page") Integer page, @Query("language") String language, @Query("api_key") String apiKey);

    @GET("/cards/{id}")
    Call<Card> getCard(@Query("id") String id);
}
