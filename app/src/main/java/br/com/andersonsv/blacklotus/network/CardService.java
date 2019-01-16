package br.com.andersonsv.blacklotus.network;

import br.com.andersonsv.blacklotus.data.Card;
import br.com.andersonsv.blacklotus.data.Cards;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CardService {

    @GET("/v1/cards")
    Call<Cards> getCardsByLanguage(@Query("name") String name, @Query("pageSize") Integer pageSize, @Query("language") String language);

    @GET("/v1/cards")
    Call<Cards> getCards(@Query("name") String name, @Query("pageSize") Integer pageSize);


    @GET("/v1/cards/{id}")
    Call<Card> getCard(@Query("id") String id);
}
