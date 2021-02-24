package com.example.yandextask;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {
//    https://finnhub.io/api/v1/stock/profile2?symbol=AAPL&token=
    @GET("stock/profile2?symbol=YNDX.ME&token=c0mlc6v48v6tkq133gdg")
    Call<List<Profile2>> getProfile2();

//    https://finnhub.io/api/v1/stock/symbol?exchange=US&token=c0mlc6v48v6tkq133gdg
    @GET("stock/symbol?exchange=ME&token=c0mlc6v48v6tkq133gdg")
    Call<List<StockSymbol>> getStockSymbol();
}
