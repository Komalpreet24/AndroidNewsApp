package com.example.apidata

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var adapter: NewsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        getNews()

        val pullToRefresh : SwipeRefreshLayout= findViewById(R.id.pullToRefresh)
        pullToRefresh.setOnRefreshListener {
            getNews()

        }
    }

    private fun getNews() {
        val spinner : ProgressBar = findViewById(R.id.progressBar)
        val news:Call<ApiData> = ApiService.apiServiceInstance.getHeadlines("in", 1)
        news.enqueue(object : Callback<ApiData> {
            override fun onFailure(call: Call<ApiData>, t: Throwable) {
                Log.d("news","Error Occured",t)
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(this@MainActivity,"Couldn't Load News",Toast.LENGTH_SHORT).show()

                    pullToRefresh.isRefreshing = false
            }

            override fun onResponse(call: Call<ApiData>, response: Response<ApiData>) {
                val newsResponse:ApiData = response.body()
                    Log.d("news", newsResponse.toString())

                    adapter = NewsAdapter(this@MainActivity, newsResponse.articles)
                    newsList.adapter = adapter
                    newsList.layoutManager = LinearLayoutManager(this@MainActivity)

                    spinner.setVisibility(View.GONE);

                    pullToRefresh.isRefreshing = false

//                    sendDataToRoomDataBase(newsResponse)

            }

//            fun sendDataToRoomDataBase(newsResponse : ApiData)  {
//                    var database = NewsDatabase.getDatabase( this@MainActivity )
//                    database?.newsDao().insertAll(newsResponse.articles)
//            }

        })
    }
}

