package com.example.yandextask;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {
//    https://finnhub.io/api/v1/stock/profile2?symbol=AAPL&token=
    @GET("stock/profile2?token=c0mlc6v48v6tkq133gdg")
    Call<Profile2> getProfile2(@Query("symbol") String name);

//    https://finnhub.io/api/v1/stock/symbol?exchange=US&token=c0mlc6v48v6tkq133gdg
    @GET("stock/symbol?exchange=ME&token=c0mlc6v48v6tkq133gdg")
    Call<List<StockSymbol>> getStockSymbol();
}
