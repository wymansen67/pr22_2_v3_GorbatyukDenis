package com.example.pr22_individual_gorbatyukdenis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pr22_individual_gorbatyukdenis.adapter.BookAdapter
import com.example.pr22_individual_gorbatyukdenis.data.AppDB
import com.example.pr22_individual_gorbatyukdenis.data.entity.Book
import com.example.pr22_individual_gorbatyukdenis.databinding.ActivityBookSearchBinding
import com.example.pr22_individual_gorbatyukdenis.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import org.json.JSONObject

class BookSearchActivity : AppCompatActivity() {

    lateinit var bindingClass: ActivityBookSearchBinding
    lateinit var BookISBN: TextInputEditText
    lateinit var GetInfoBTN: MaterialButton
    lateinit var SaveInfoBTN: MaterialButton
    private var list = mutableListOf<Book>()
    private lateinit var adapter: BookAdapter
    private lateinit var db: AppDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityBookSearchBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        BookISBN = bindingClass.bookISBN
        GetInfoBTN = bindingClass.getBookInfoButton
        GetInfoBTN.setOnClickListener() { GetBook(BookISBN.text.toString()) }
        SaveInfoBTN.setOnClickListener() { SaveBook()}
    }

    fun GetBook(ISBN: String) {
        var url = "https://openlibrary.org/isbn/" + ISBN + ".json"
        if (!ISBN.isNullOrEmpty()) {
            var validRes = ValidateISBN(ISBN)
            if (validRes != "0") { var snackBar = Snackbar.make(findViewById(android.R.id.content), validRes, Snackbar.LENGTH_LONG).show() }
            else {
                val queue = Volley.newRequestQueue(this)
                val stringRequest = StringRequest(
                    Request.Method.GET,
                    url,
                    {
                            response->
                        val jsonObject = JSONObject(response)
                        val title = jsonObject.getString("title").toString()
                        var publisher = jsonObject.getString("publishers")
                        publisher = publisher.replace("\"", "")
                        publisher = publisher.replace("[", "")
                        publisher = publisher.replace("]", "")
                        val publish_date = jsonObject.getString("publish_date")
                        var author = jsonObject.getString("authors")
                        author = author.subSequence(10,(author.length - 3)).toString()
                        author = author.replace("\\", "")
                        var url_author = "https://openlibrary.org" + author + ".json"
                        val queue1 = Volley.newRequestQueue(this)
                        val stringRequest1 = StringRequest(
                            Request.Method.GET,
                            url_author,
                            {
                                    response->
                                val jsonObject1 = JSONObject(response)
                                val name = jsonObject1.getString("personal_name")
                                bindingClass.title.text = "$title"
                                bindingClass.author.text = "$name"
                                bindingClass.publishDate.text = "$publish_date"
                                bindingClass.publisher.text = "$publisher"

                            }, {
                                Log.d("MyLog","Volley error: $it")
                            }
                        )
                        queue1.add(stringRequest1)
                    }, {
                        Log.d("MyLog","Volley error: $it")
                    }
                )
                queue.add(stringRequest)
            }
            bindingClass.saveBookInfoButton.visibility = VISIBLE
            bindingClass.getBookInfoButton.visibility = GONE
        }
    }

    fun SaveBook(){
            list.clear()
            list.addAll(db.bookDao().search(bindingClass.title.text.toString()))
            adapter = BookAdapter(list)

            if (adapter.itemCount == 0) {
                db.bookDao().insertAll(
                    Book(
                        null,
                        bindingClass.title.text.toString(),
                        bindingClass.author.text.toString(),
                        bindingClass.publishDate.text.toString(),
                        bindingClass.publisher.text.toString()
                    )
                )
            } else {
                db.bookDao().delete(list[0])
                db.bookDao().insertAll(
                    Book(
                        null,
                        bindingClass.title.text.toString(),
                        bindingClass.author.text.toString(),
                        bindingClass.publishDate.text.toString(),
                        bindingClass.publisher.text.toString()
                    )
                )
            }
            startActivity(Intent(this@BookSearchActivity, MainActivity::class.java))
    }

    fun ValidateISBN(ISBN: String): String {
        var err: Int = 0
        if (ISBN.length == 10 || ISBN.length == 13){
            for (char in ISBN ) if (!char.isDigit()) err++
            if (err > 0) return "ISBN should contain only digits"
            else return "0"
        }
        else return "Invalid ISBN length"
    }
}