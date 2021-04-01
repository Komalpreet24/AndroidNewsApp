package com.example.apidata

import android.app.Application


class NewsApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { NewsDatabase.getDatabase(this) }
    val repository by lazy { NewsRespository(database.newsDao()) }
}