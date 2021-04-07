package com.example.apidata

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.apidata.model.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FullNewsArticle : AppCompatActivity() {

    lateinit var adapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_news_article)

        getArticle()

    }

    private fun getArticle() {

        val title = intent.extras!!.getString("title")
        val description = intent.extras!!.getString("description")
        val source = intent.extras!!.getString("source")
        val content = intent.extras!!.getString("content")
        val image = intent.extras!!.getString("image")


        val spinner : ProgressBar = findViewById(R.id.imageSpinner)
        val newsImage : ImageView = findViewById(R.id.newsImage)
        val newsHeadline : TextView = findViewById(R.id.newsHeadline)
        val newsdescription : TextView = findViewById(R.id.newsdescription)
        val newsSource : TextView = findViewById(R.id.newsSource)
        val newscontent : TextView = findViewById(R.id.newscontent)



        val news: Call<ApiData> = ApiService.apiServiceInstance.getHeadlines("in", 1)
        news.enqueue(object : Callback<ApiData> {
            override fun onFailure(call: Call<ApiData>, t: Throwable) {
                Log.d("news","Error Occured",t)
                spinner.setVisibility(View.GONE);
                Toast.makeText(this@FullNewsArticle,"Couldn't Load News", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ApiData>, response: Response<ApiData>) {
                val newsResponse:ApiData = response.body()
                Log.d("news", newsResponse.toString())

                newsHeadline.setText(title)
                newsSource.setText(source)
                newsdescription.setText(description)
                newscontent.setText(content)


                Glide.with(this@FullNewsArticle).load(image)
                    .listener(object : RequestListener<Drawable> {
                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            spinner.setVisibility(View.GONE);
                            return false
                        }

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            spinner.setVisibility(View.GONE);
                            return false
                        }
                    })
                    .into(newsImage)

            }

        })
    }

}
