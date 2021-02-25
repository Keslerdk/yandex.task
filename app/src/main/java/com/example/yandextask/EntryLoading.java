package com.example.yandextask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.internal.ParcelableSparseArray;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EntryLoading extends AppCompatActivity {

    private TextView loadingText;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_loading);

        loadingText = findViewById(R.id.text_info);
        mProgressBar = findViewById(R.id.entryProgressBar);
    }

    private class RequestStockSymbols extends AsyncTask<String, Void, List<StockSymbol>> {

        private JsonPlaceHolderApi jsonPlaceHolderApi;

        @Override
        protected List<StockSymbol> doInBackground(String... strings) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://finnhub.io/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

            Call<List<StockSymbol>> callStockSymbol = jsonPlaceHolderApi.getStockSymbol();

            try {
                Response<List<StockSymbol>> responseStockSymbol = callStockSymbol.execute();
                List<StockSymbol> stockSymbols = responseStockSymbol.body();
                return stockSymbols;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (RuntimeException e) {
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Void...values) {
            mProgressBar.setIndeterminate(true);
            loadingText.setText("LOADING...");
        }

        @Override
        protected void onPostExecute(List<StockSymbol> string) {
            Intent intent = new Intent(EntryLoading.this, MainActivity.class);

            intent.putExtra("List<StockSymbol>", (Parcelable) string);

        }
    }
}