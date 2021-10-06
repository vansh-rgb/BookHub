package com.jikzkis.bookhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jikzkis.bookhub.R
import com.jikzkis.bookhub.database.BookEntity
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context: Context, val bookList: List<BookEntity>): RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {

    class FavouriteViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txtBookName: TextView = view.findViewById(R.id.txtRecyclerRowItem)
        val txtBookAuthor : TextView = view.findViewById(R.id.authorRecyclerRowItem)
        val txtBookRating : TextView = view.findViewById(R.id.ratingRecyclerRowItem)
        val txtBookPrice: TextView = view.findViewById(R.id.priceRecyclerRowItem)
        val txtBookImage: ImageView = view.findViewById(R.id.imgRecyclerRowItem)
        val llContent: LinearLayout = view.findViewById(R.id.bookItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourite_single_row,parent,false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val book = bookList[position]

        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.ic_launcher_foreground).into(holder.txtBookImage)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}