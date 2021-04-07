package com.example.apidata.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NewsDao {

    @Query("SELECT * FROM news_table")
    suspend fun getArticles(): List<Article>

    @Insert
    suspend fun insertAll(vararg articles: Article) : List<Long>

    @Query("SELECT * FROM news_table WHERE id = :id")
    suspend fun getNewsArticle(id: Int) : Article

    @Query("DELETE FROM news_table")
    suspend fun deleteAll()

}