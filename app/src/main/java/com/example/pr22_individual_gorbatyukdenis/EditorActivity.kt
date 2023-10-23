package com.example.pr22_individual_gorbatyukdenis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.example.pr22_individual_gorbatyukdenis.adapter.BookAdapter
import com.example.pr22_individual_gorbatyukdenis.data.AppDB
import com.example.pr22_individual_gorbatyukdenis.data.entity.Book
import com.example.pr22_individual_gorbatyukdenis.databinding.ActivityEditorBinding

class EditorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditorBinding
    private lateinit var title: EditText
    private lateinit var author: EditText
    private lateinit var publish_date: EditText
    private lateinit var publisher: EditText
    private lateinit var btn: Button
    private lateinit var db: AppDB
    private var list = mutableListOf<Book>()
    private lateinit var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = binding.title
        author = binding.author
        publish_date = binding.publishDate
        publisher = binding.publisher
        btn = binding.btnSave

        db = AppDB.getInstance(applicationContext)

        val intent = intent.extras
        if (intent != null){
            val id = intent.getInt("id", 0)
            val book = db.bookDao().get(id)

            title.setText(book.title)
            title.isEnabled = false
            author.setText(book.author)
            publish_date.setText(book.publish_date)
            publisher.setText(book.publisher)
        } else {
            title.isEnabled = true
        }

        btn.setOnClickListener{
            if (title.text.isNotEmpty() && author.text.isNotEmpty() && publish_date.text.isNotEmpty() && publisher.text.isNotEmpty())
            try {
                if (title.text.contains("-_?/\\|*&^;=+%$#@!<>'\"[]{}~`")) {
                    Toast.makeText(
                        applicationContext,
                        "Наименование книги имеет недопустимые символы",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (author.text.contains("-_?/\\|*&^:;=+%$#@!<>'\"[]{}~`")) {
                    Toast.makeText(
                        applicationContext,
                        "Автор имеет недопустимые символы",
                        Toast.LENGTH_SHORT
                    ).show()
                }else if (publish_date.text.contains("-_?/\\|*&^:;=+%$#@!<>'\"[]{}~`")){
                    Toast.makeText(
                        applicationContext,
                        "Дата публикации имеет недопустимые символы",
                        Toast.LENGTH_SHORT
                    ).show()
                }else if (publisher.text.contains("-_?/\\|*&^:;=+%$#@!<>'\"[]{}~`")){
                    Toast.makeText(
                        applicationContext,
                        "Издатель имеет недопустимые символы",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (intent != null) {
                        db.bookDao().update(
                            Book(
                                intent.getInt("id", 0),
                                title.text.toString(),
                                author.text.toString(),
                                publish_date.text.toString(),
                                publisher.text.toString(),
                            )
                        )
                    } else {
                        list.clear()
                        list.addAll(
                            db.bookDao()
                                .search(title.text.toString())
                        )
                        adapter = BookAdapter(list)

                        if (adapter.itemCount == 0) {
                            db.bookDao().insertAll(
                                Book(
                                    null,
                                    title.text.toString(),
                                    author.text.toString(),
                                    publish_date.text.toString(),
                                    publisher.text.toString(),
                                )
                            )
                        } else {
                            db.bookDao().delete(list[0])
                            db.bookDao().insertAll(
                                Book(
                                    null,
                                    title.text.toString(),
                                    author.text.toString(),
                                    publish_date.text.toString(),
                                    publisher.text.toString(),
                                )
                            )
                        }
                    }
                    finish()
                }
            } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                "",
                Toast.LENGTH_SHORT
            ).show()
            } else {
                Toast.makeText(applicationContext, "Поля не могут быть пустыми", Toast.LENGTH_SHORT).show()
            }
        }
    }
}