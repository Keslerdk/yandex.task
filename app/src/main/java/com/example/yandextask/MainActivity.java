package com.example.yandextask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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

//    JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ApiRequest().execute();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                creatRecyclerView(1, 10, stockSymbols);
                buildRecyclerView(stockSymbols);
            }
        }, 25000);


    }

    private void creatRecyclerView(int indexStart, int indexEnd, List<StockSymbol> stockSymbols) {

        for (int i = indexStart; i < indexEnd; i++) {
            myCardList.add(new CardItem(stockSymbols.get(i).getSymbol()));
//            myCardList.add(new CardItem("jdv", "hvds", "jhvs", ".zjs"));
//            myCardList.add(new CardItem("", "", "", ""));
        }

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
                    }, 3000);
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

    private class ApiRequest extends AsyncTask<String, Void, List<StockSymbol>> {

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
            Call<List<Profile2>> callProfile2 = jsonPlaceHolderApi.getProfile2();

            try {
                Response<List<StockSymbol>> responseStockSymbol  = callStockSymbol.execute();
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
            stockSymbols=string;
        }
    }
}