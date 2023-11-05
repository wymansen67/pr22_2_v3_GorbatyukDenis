package com.example.book

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.book.databinding.ActivitySearchBookBinding
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject

class SearchBook : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBookBinding
    private lateinit var Title: TextView
    private lateinit var Author: TextView
    private lateinit var PublishDate: TextView
    private lateinit var tvPublisher: TextView
    private lateinit var database: AppDatabase
    private var list = mutableListOf<Book>()
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Title = binding.title
        Author = binding.author
        PublishDate = binding.publishDate
        tvPublisher = binding.publisher
        database = AppDatabase.getInstance(applicationContext)

        binding.getBookInfoButton.setOnClickListener {

            if (binding.bookISBN.text.toString().trim().isNotEmpty()) {
                getBook(binding.bookISBN.text.toString().trim())
            } else {
                binding.saveBookInfoButton.visibility = GONE
                Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show()
            }

            if (Title.text.toString().isNotEmpty()) {
                binding.getBookInfoButton.visibility = GONE
                binding.saveBookInfoButton.visibility = VISIBLE
            } else {
                binding.saveBookInfoButton.visibility = GONE
                Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.saveBookInfoButton.setOnClickListener {
                list.clear()
                list.addAll(database.bookDao().search(Title.text.toString()))
                adapter = BookAdapter(list)

                if (adapter.itemCount == 0) {
                    database.bookDao().insertAll(
                        Book(
                            null,
                            Title.text.toString(),
                            Author.text.toString(),
                            PublishDate.text.toString(),
                            tvPublisher.text.toString()
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
                            tvPublisher.text.toString(),
                        )
                    )
                }

                startActivity(Intent(this@SearchBook, MainActivity::class.java))
        }
    }

    fun ValidateISBN(ISBN: String): String {
        var err: Int = 0
        if (ISBN.length == 10 || ISBN.length == 13) {
            for (char in ISBN) if (!char.isDigit()) err++
            if (err > 0) return "ISBN should contain only digits"
            else return "0"
        } else return "Invalid ISBN length"
    }

    private fun getBook(ISBN: String) {
        var url = "https://openlibrary.org/isbn/" + ISBN + ".json"
        if (!ISBN.isNullOrEmpty()) {
            var validRes = ValidateISBN(ISBN)
            if (validRes != "0") {
                var snackBar = Snackbar.make(
                    findViewById(android.R.id.content),
                    validRes,
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                val queue = Volley.newRequestQueue(this)
                val stringRequest = StringRequest(
                    Request.Method.GET,
                    url,
                    { response ->
                        val jsonObject = JSONObject(response)
                        val title = jsonObject.getString("title").toString()
                        var publisher = jsonObject.getString("publishers")
                        publisher = publisher.replace("\"", "")
                        publisher = publisher.replace("[", "")
                        publisher = publisher.replace("]", "")
                        val publish_date = jsonObject.getString("publish_date")
                        var author = jsonObject.getString("authors")
                        author = author.subSequence(10, (author.length - 3)).toString()
                        author = author.replace("\\", "")
                        var url_author = "https://openlibrary.org" + author + ".json"
                        val queue1 = Volley.newRequestQueue(this)
                        val stringRequest1 = StringRequest(
                            Request.Method.GET,
                            url_author,
                            { response ->
                                val jsonObject1 = JSONObject(response)
                                val name = jsonObject1.getString("personal_name")
                                binding.title.text = "$title"
                                binding.author.text = "$name"
                                binding.publishDate.text = "$publish_date"
                                binding.publisher.text = "$publisher"

                            }, {
                                Log.d("MyLog", "Volley error: $it")
                            }
                        )
                        queue1.add(stringRequest1)
                    }, {
                        Log.d("MyLog", "Volley error: $it")
                    }
                )
                queue.add(stringRequest)
            }
        }
    }
}