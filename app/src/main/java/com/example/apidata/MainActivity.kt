package com.example.apidata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.apidata.model.ApiService
import com.example.apidata.model.Article
import com.example.apidata.model.NewsDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NumberFormatException
import kotlin.coroutines.CoroutineContext
import com.example.apidata.SharedPreferencesHelper

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    // after job finished join main thread

    lateinit var adapter: NewsAdapter
    lateinit var spinner: ProgressBar

    lateinit var prefHelper : SharedPreferencesHelper
    var refreshTime = 1 * 60 * 1000 * 1000 * 1000L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefHelper = SharedPreferencesHelper.getShared(application)

        spinner = findViewById(R.id.progressBar)
        checkCacheDuration()

        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            getNews()
        }



        val pullToRefresh: SwipeRefreshLayout = findViewById(R.id.pullToRefresh)
        pullToRefresh.setOnRefreshListener {
            getNews()
//            fetchFromDatabase()
        }
    }




    private  fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()

        try{

            val cachePreferenceInt = cachePreference?.toInt() ?: 5 * 60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)

        }catch (e: NumberFormatException) {
            e.printStackTrace()
        }

    }


    private fun getNews() {

        val news: Call<ApiData> = ApiService.apiServiceInstance.getHeadlines("in", 1)
        news.enqueue(object : Callback<ApiData> {
            override fun onFailure(call: Call<ApiData>, t: Throwable) {
                Log.d("news", "Error Occured", t)
                spinner.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Couldn't Load News", Toast.LENGTH_SHORT).show()

                pullToRefresh.isRefreshing = false
            }

            override fun onResponse(call: Call<ApiData>, response: Response<ApiData>) {

                val newsResponse: ApiData = response.body()

                saveDataLocally(newsResponse.articles)

                Log.d("news", newsResponse.toString())

                Toast.makeText(application, "Data retrieved from endpoint", Toast.LENGTH_SHORT)
                    .show()



            }



        })


    }

    private fun saveDataLocally(newsResponse: List<Article>) {
        launch {
            val dao = NewsDatabase.getDatabase(application).newsDao()

            dao.deleteAll()

            val result = dao.insertAll(*newsResponse.toTypedArray())

            var i = 0
            while (i < newsResponse.size) {
                newsResponse[i].id = result[i].toInt()
                ++i
            }

            dataRetrieved(newsResponse)

        }


        prefHelper.saveUpdateTime(System.nanoTime())

    }

    private fun dataRetrieved(newsResponse: List<Article>) {

        adapter = NewsAdapter(this@MainActivity, newsResponse)
        newsList.adapter = adapter
        newsList.layoutManager = LinearLayoutManager(this@MainActivity)

        spinner.visibility = View.GONE

        pullToRefresh.isRefreshing = false
    }

    private fun fetchFromDatabase() {
        spinner.visibility = View.VISIBLE
        launch {
            val dao = NewsDatabase.getDatabase(application).newsDao()
            val allArticles = dao.getArticles()
            dataRetrieved(allArticles)
            Toast.makeText(application,"Data retrieved from database", Toast.LENGTH_SHORT).show()
        }
    }


}

