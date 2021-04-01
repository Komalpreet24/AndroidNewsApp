package com.example.apidata

class NewsRespository(private val newsDao: NewsDao) {
    val allArticle: List<Article> = newsDao.getArticles()
}