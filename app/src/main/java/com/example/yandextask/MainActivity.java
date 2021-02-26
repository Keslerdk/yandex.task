package com.example.yandextask;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ArrayList<CardItem> myCardList = new ArrayList<>();
    RecyclerView myRecyclerView;

    //    RecyclerView.Adapter myAdapter;
    MyAdapter myAdapter;

    RecyclerView.LayoutManager myLayoutManager;

    ArrayList<String> stockSymbolNames = new ArrayList<>();
    List<Profile2> profile2List = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stockSymbolNames = getIntent().getStringArrayListExtra("List<StockSymbol>");
        Log.d("MainActicity", String.valueOf(stockSymbolNames));

        creatRecyclerView(0, 10, stockSymbolNames);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                Log.d("my CardList111", String.valueOf(myCardList));
                buildRecyclerView(stockSymbolNames);
            }
        }, 9000);
    }

    private void creatRecyclerView(int indexStart, int indexEnd, List<String> names) {

        for (int i = indexStart; i < indexEnd; i++) {
            new RequestProfile2().execute(names.get(i));
        }
    }

    private void buildRecyclerView(List<String> names) {
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

                    int index = myCardList.size();
                    int end = index + 10;
                    creatRecyclerView(index, end, names);


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            myCardList.remove(myCardList.size() - 11);
                            myAdapter.notifyItemRemoved(myCardList.size());

                            //random more data
//                            int index = myCardList.size();
//                            int end = index + 10;
//                            creatRecyclerView(index, end, names);
                            myAdapter.notifyDataSetChanged();
                            myAdapter.setLoaded();


                        }
                    }, 9000);

                } else {
                    Toast.makeText(MainActivity.this, "Load data completed !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /*
    public void changeItem(int position, String text) {
//        myCardList.get(position).changeTicker(text);
//        myAdapter.notifyItemChanged(position);
        myCardList.add(new CardItem("aaaa", "aaaa", "aaaa", "aaaa"));
        myAdapter.notifyDataSetChanged();
    } */


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
            myCardList.add(new CardItem(string.getTicker(), string.getName(), "bbbb", "bbbb"));
            profile2List.add(string);
            Log.d("profile2 list Inside", String.valueOf(profile2List) + 111);
            Log.d("ProfileInside", String.valueOf(string) + 111);
        }
    }
}