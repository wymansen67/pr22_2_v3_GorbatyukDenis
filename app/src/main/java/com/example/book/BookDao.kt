package com.example.book

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BookDao {
    @Query("select * from book")
    fun getAll(): List<Book>

    @Query("select * from book where bid in (:bookIds)")
    fun loadAllByIds(bookIds: IntArray): List<Book>

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