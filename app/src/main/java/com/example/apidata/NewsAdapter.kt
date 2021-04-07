package com.example.apidata

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.apidata.model.Article

class NewsAdapter(val context: Context, val articles: List<Article>) :
    RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var newsImage = itemView.findViewById<ImageView>(R.id.newsImage)
        var newsHeadline = itemView.findViewById<TextView>(R.id.newsHeadline)
        var newsBody = itemView.findViewById<TextView>(R.id.newsBody)
        val imageSpinner = itemView.findViewById<ProgressBar>(R.id.imageSpinner)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false)
        return ArticleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        var article = articles[position]

        holder.newsHeadline.text = article.title
        holder.newsBody.text = article.description
        Glide.with(context).load(article.urlToImage)
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.imageSpinner.setVisibility(View.GONE);
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.imageSpinner.setVisibility(View.GONE);
                    return false
                }
            })
            .into(holder.newsImage)
        holder.itemView.setOnClickListener{

            val intent = Intent(context, FullNewsArticle::class.java)
            intent.putExtra("title", article.title)
            intent.putExtra("description", article.description)
            intent.putExtra("source",article.url)
            intent.putExtra("content",article.content)
            intent.putExtra("image",article.urlToImage)
            context.startActivity(intent)

        }
    }
}