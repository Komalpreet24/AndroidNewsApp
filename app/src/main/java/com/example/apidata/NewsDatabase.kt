package com.example.apidata

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = arrayOf(Article::class), version = 1, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {


    abstract fun newsDao(): NewsDao



    private class NewsDatabaseCallback() : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                    var newsDao = database.newsDao()

                    // Delete all content here.
                    newsDao.deleteAll()

//                    // Add sample words.
//                    var word = Article("Hello")
//                    wordDao.insert(word)
//                    word = Word("World!")
//                    wordDao.insert(word)

            }
        }
    }




    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        private var INSTANCE: NewsDatabase? = null

        fun getDatabase(context: Context): NewsDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java,
                    "news_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}