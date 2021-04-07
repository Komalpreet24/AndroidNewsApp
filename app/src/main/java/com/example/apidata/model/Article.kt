package com.example.apidata.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_table")
data class Article(
    val title: String?,
    val description: String?,
    val content: String?,
    val urlToImage: String?,
    val url: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}
