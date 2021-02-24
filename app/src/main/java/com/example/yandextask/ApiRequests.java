package com.example.yandextask;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ApiRequests {
    private final JsonPlaceHolderApi jsonPlaceHolderApi;

    public ApiRequests(Retrofit retrofit) {
        this.jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        getStockSymbol();
    }

    public void getProfile2() {
        Call<List<Profile2>> call = jsonPlaceHolderApi.getProfile2();
        call.enqueue(new Callback<List<Profile2>>() {
            @Override
            public void onResponse(@NotNull Call<List<Profile2>> call, Response<List<Profile2>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

            }

            @Override
            public void onFailure(Call<List<Profile2>> call, Throwable t) {
            }
        });
    }

    public void getStockSymbol() {
        Call<List<StockSymbol>> call = jsonPlaceHolderApi.getStockSymbol();

        call.enqueue(new Callback<List<StockSymbol>>() {
            @Override
            public void onResponse(Call<List<StockSymbol>> call, Response<List<StockSymbol>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                List<StockSymbol>stockSymbols = response.body();
                for (int i=0; i<20; i++){
                    Log.d("StockSymbols", stockSymbols.get(i).getSymbol());
                }

            }

            @Override
            public void onFailure(Call<List<StockSymbol>> call, Throwable t) {

            }
        });
    }

}
