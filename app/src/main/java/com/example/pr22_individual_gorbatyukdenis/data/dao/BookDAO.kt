package com.example.pr22_individual_gorbatyukdenis.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pr22_individual_gorbatyukdenis.data.entity.Book

@Dao
interface BookDAO {
    @Query ("select * from book")
    fun getAll(): List<Book>

    @Query("select * from book where bid in (:weatherIds)")
    fun loadAllByIds(weatherIds: IntArray): List<Book>

    @Query("select * from book where bid = :bid")
    fun get(bid: Int): Book

    @Query("select bid from book where title like :search")
    fun search(search: String): List<Book>

    @Insert
    fun insertAll(vararg books: Book)

    @Update
    fun update(book: Book)

    @Delete
    fun delete(book: Book)
}