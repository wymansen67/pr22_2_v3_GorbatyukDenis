package com.example.book

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class Editor : AppCompatActivity() {

    private lateinit var Title: EditText
    private lateinit var Author: EditText
    private lateinit var PublishDate: EditText
    private lateinit var Publisher: EditText
    private lateinit var bttnSave: Button
    private lateinit var database: AppDatabase
    private var list = mutableListOf<Book>()
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        Title = findViewById(R.id.title)
        Author = findViewById(R.id.author)
        PublishDate = findViewById(R.id.publishDate)
        Publisher = findViewById(R.id.publisher)
        bttnSave = findViewById(R.id.btn_save)
        database = AppDatabase.getInstance(applicationContext)

        val intent = intent.extras
        if (intent != null) {
            val id = intent.getInt("id", 0)
            val book = database.bookDao().get(id)

            Title.setText(book.title)
            Title.isEnabled = false
            Author.setText(book.author)
            PublishDate.setText(book.publish_date)
            Publisher.setText(book.publisher)
        } else {
            Title.isEnabled = true
        }

        bttnSave.setOnClickListener {
            if (Title.text.isNotEmpty() && Author.text.isNotEmpty() && PublishDate.text.isNotEmpty() && Publisher.text.isNotEmpty()) {
                //if ()
                if (intent != null) {
                    database.bookDao().update(
                        Book(
                            intent.getInt("id", 0),
                            Title.text.toString(),
                            Author.text.toString(),
                            PublishDate.text.toString(),
                            Publisher.text.toString()
                        )
                    )
                } else {
                    list.clear()
                    list.addAll(
                        database.bookDao()
                            .search(Title.text.toString())
                    )
                    adapter = BookAdapter(list)

                    if (adapter.itemCount == 0) {
                        database.bookDao().insertAll(
                            Book(
                                null,
                                Title.text.toString(),
                                Author.text.toString(),
                                PublishDate.text.toString(),
                                Publisher.text.toString(),
                            )
                        )
                    } else {
                        database.bookDao().delete(list[0])
                        database.bookDao().insertAll(
                            Book(
                                null,
                                Title.text.toString(),
                                Author.text.toString(),
                                PublishDate.text.toString(),
                                Publisher.text.toString(),
                            )
                        )
                    }
                }
                finish()
            }
        }
    }
}