package com.example.apidata

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_table")
class Article(@PrimaryKey val title:String, val description:String, val content:String, val urlToImage:String, val url: String)
