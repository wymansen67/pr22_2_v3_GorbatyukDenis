package com.example.pr22_individual_gorbatyukdenis.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pr22_individual_gorbatyukdenis.data.dao.BookDAO
import com.example.pr22_individual_gorbatyukdenis.data.entity.Book

@Database(entities = [Book::class], version = 1)
abstract class AppDB: RoomDatabase() {
    abstract fun bookDao(): BookDAO

    companion object {
        private var instance: AppDB? = null

        fun getInstance(context: Context): AppDB{
            if (instance == null){
                instance = Room.databaseBuilder(context, AppDB::class.java, "app-db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}