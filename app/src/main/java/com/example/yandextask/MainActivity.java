package com.example.yandextask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ArrayList<CardItem> myCardList = new ArrayList<>();
    RecyclerView myRecyclerView;

    //    RecyclerView.Adapter myAdapter;
    MyAdapter myAdapter;

    RecyclerView.LayoutManager myLayoutManager;
    List<StockSymbol> stockSymbols;
    Profile2 profile2;
    List<Profile2> profile2List = new ArrayList<>();

//    JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        stockSymbols = getIntent().getParcelableExtra("List<StockSymbol>");
//        Log.d("StockSymbols", String.valueOf(stockSymbols.get(0))+1212);
        new RequestStockSymbols().execute();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                creatRecyclerView(0, 10, stockSymbols);

//                Log.d("my CardList111", String.valueOf(myCardList));
//                buildRecyclerView(stockSymbols);
            }
        }, 25000);

        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("my CardList111", String.valueOf(myCardList)+111);
                buildRecyclerView(stockSymbols);
            }
        }, 20000);


    }

    private void creatRecyclerView(int indexStart, int indexEnd, List<StockSymbol> stockSymbols) {

//        profile2List = new ArrayList<Profile2>();
        for (int i = indexStart; i < indexEnd; i++) {
            new RequestProfile2().execute(stockSymbols.get(i).getSymbol());
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("Profile2List", String.valueOf(profile2List));
                for (int i = indexStart; i < indexEnd; i++) {
                    myCardList.add(new CardItem(profile2List.get(i).getTicker(), profile2List.get(i).getName(), "bbb", "bbb"));
                }
                Log.d("my Card List", String.valueOf(myCardList)+111);
            }
        }, 20000);


    }

    private void buildRecyclerView(List<StockSymbol> stockSymbols) {
//        myRecyclerView = findViewById(R.id.recyclerView);
//        myRecyclerView.setHasFixedSize(true);
//        myLayoutManager = new LinearLayoutManager(this);

//        myAdapter = new MyAdapter(myCardList);

        myRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(myRecyclerView, this, myCardList);

//        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setAdapter(myAdapter);


        myAdapter.setLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                if (myCardList.size() <= 100) {
                    myCardList.add(null);
                    myAdapter.notifyItemInserted(myCardList.size() - 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            myCardList.remove(myCardList.size() - 1);
                            myAdapter.notifyItemRemoved(myCardList.size());

                            //random more data
                            int index = myCardList.size();
                            int end = index + 10;
                            creatRecyclerView(index, end, stockSymbols);
                            myAdapter.notifyDataSetChanged();
                            myAdapter.setLoaded();

                        }
                    }, 7000);
                } else {
                    Toast.makeText(MainActivity.this, "Load data completed !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /*
    public void getStockSymbol() {
        Call<List<StockSymbol>> call = jsonPlaceHolderApi.getStockSymbol();

        call.enqueue(new Callback<List<StockSymbol>>() {
            @Override
            public void onResponse(Call<List<StockSymbol>> call, Response<List<StockSymbol>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                List<StockSymbol> stockSymbols = response.body();
                creatRecyclerView(0, 10, stockSymbols);

                buildRecyclerView(stockSymbols);
            }

            @Override
            public void onFailure(Call<List<StockSymbol>> call, Throwable t) {

            }
        });
    } */

    /*
    public void getProfile2(int position) {
        Call<List<Profile2>> call = jsonPlaceHolderApi.getProfile2();
        call.enqueue(new Callback<List<Profile2>>() {
            @Override
            public void onResponse(@NotNull Call<List<Profile2>> call, Response<List<Profile2>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                List<Profile2> profile2 = response.body();
                changeItem(position, profile2.get(0).getName());
            }

            @Override
            public void onFailure(Call<List<Profile2>> call, Throwable t) {
            }
        });
    } */


    public void changeItem(int position, String text) {
//        myCardList.get(position).changeTicker(text);
//        myAdapter.notifyItemChanged(position);
        myCardList.add(new CardItem("aaaa", "aaaa", "aaaa", "aaaa"));
        myAdapter.notifyDataSetChanged();
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
        protected void onPostExecute(List<StockSymbol> string) {
            stockSymbols = string;
        }
    }


    private class RequestProfile2 extends AsyncTask<String, Void, Profile2> {

        private JsonPlaceHolderApi jsonPlaceHolderApi;


        @Override
        protected Profile2 doInBackground(String... strings) {
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

            Call<Profile2> callProfile2 = jsonPlaceHolderApi.getProfile2(strings[0]);
            try {
                Response<Profile2> responseProfile2 = callProfile2.execute();
                Profile2 profile2 = responseProfile2.body();
                return profile2;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (RuntimeException e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(Profile2 string) {
//            profile2 = string;
//            Log.d("ProfileStringInside", String.valueOf(profile2));
            profile2List.add(string);
            Log.d("profile2 list Inside", String.valueOf(profile2List) + 111);
            Log.d("ProfileInside", String.valueOf(string) + 111);
        }
    }
}