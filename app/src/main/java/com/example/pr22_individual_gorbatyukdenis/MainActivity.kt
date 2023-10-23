package com.example.pr22_individual_gorbatyukdenis

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pr22_individual_gorbatyukdenis.adapter.BookAdapter
import com.example.pr22_individual_gorbatyukdenis.data.AppDB
import com.example.pr22_individual_gorbatyukdenis.data.entity.Book
import com.example.pr22_individual_gorbatyukdenis.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import org.json.JSONObject
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var bindingClass: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private var list = mutableListOf<Book>()
    private lateinit var adapter: BookAdapter
    private lateinit var db: AppDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        recyclerView = findViewById(R.id.recycler_view)
        fab = findViewById(R.id.fab)

        db = AppDB.getInstance(applicationContext)
        adapter = BookAdapter(list)
        adapter.setDialog(object : BookAdapter.Dialog {
            override fun onClick(position: Int) {
                val dialog = AlertDialog.Builder(this@MainActivity)
                dialog.setTitle(list[position].title)
                dialog.setItems(
                    R.array.itemsoption,
                    DialogInterface.OnClickListener { dialog, which ->
                        when (which) {
                            0 -> {
                                val intent = Intent(this@MainActivity, EditorActivity::class.java)
                                intent.putExtra("id", list[position].bid)
                                startActivity(intent)
                            }
                            1 -> {
                                db.bookDao().delete(list[position])
                                getData()
                            }
                            else -> {
                                dialog.dismiss()
                            }
                        }
                    })
                val dialogView = dialog.create()
                dialogView.show()
            }
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(applicationContext, RecyclerView.VERTICAL))

        fab.setOnClickListener {
            startActivity(Intent(this, EditorActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getData() {
        list.clear()
        list.addAll(db.bookDao().getAll())
        adapter.notifyDataSetChanged()
    }
}