package com.example.pr22_individual_gorbatyukdenis.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pr22_individual_gorbatyukdenis.data.entity.Book
import com.example.pr22_individual_gorbatyukdenis.R
import com.google.android.material.textview.MaterialTextView

class BookAdapter(private var list: List<Book>): RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    private lateinit var dialog: Dialog

    fun setDialog(dialog: Dialog) {
        this.dialog = dialog
    }

    interface Dialog {
        fun onClick(position: Int)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: MaterialTextView
        var author: MaterialTextView
        var publish_date: MaterialTextView
        var publisher: MaterialTextView

        init {
            title = view.findViewById(R.id.title)
            author = view.findViewById(R.id.author)
            publish_date = view.findViewById(R.id.publish_date)
            publisher = view.findViewById(R.id.publisher)
            view.setOnClickListener {
                dialog.onClick(layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_book, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = list[position].title
        holder.author.text = list[position].author
        holder.publish_date.text = list[position].publish_date
        holder.publisher.text = list[position].publisher
    }
}