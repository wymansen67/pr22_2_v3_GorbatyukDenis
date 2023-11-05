package com.example.book

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.book.Book
import com.example.book.R;

class BookAdapter(var list: List<Book>) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    private lateinit var dialog: Dialog

    fun setDialog(dialog: Dialog) {
        this.dialog = dialog
    }

    interface Dialog {
        fun onClick(position: Int)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var Title: TextView
        var Author: TextView
        var PublishDate: TextView
        var Publisher: TextView

        init {
            Title = view.findViewById(R.id.title)
            Author = view.findViewById(R.id.author)
            PublishDate = view.findViewById(R.id.publish_date)
            Publisher = view.findViewById(R.id.publisher)
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
        holder.Title.text = list[position].title
        holder.Author.text = list[position].author
        holder.PublishDate.text = list[position].publish_date
        holder.Publisher.text = list[position].publisher
    }
}