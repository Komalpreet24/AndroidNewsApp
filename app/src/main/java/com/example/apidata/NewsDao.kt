package com.example.apidata

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {

    @Query("SELECT * FROM news_table")
    fun getArticles(): List<Article>

    @Insert()
    fun insertAll(article: List<Article>)

    @Query("DELETE FROM news_table")
    fun deleteAll()

}