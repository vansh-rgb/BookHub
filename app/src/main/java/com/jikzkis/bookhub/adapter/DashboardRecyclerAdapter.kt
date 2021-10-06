package com.jikzkis.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.jikzkis.bookhub.R
import com.jikzkis.bookhub.activity.DescriptionActivity
import com.jikzkis.bookhub.model.Book
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter (private val context: Context, private val itemList: ArrayList<Book>) : RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>(){

    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtBookName: TextView = view.findViewById(R.id.txtRecyclerRowItem)
        val txtBookAuthor : TextView = view.findViewById(R.id.authorRecyclerRowItem)
        val txtBookRating : TextView = view.findViewById(R.id.ratingRecyclerRowItem)
        val txtBookPrice: TextView = view.findViewById(R.id.priceRecyclerRowItem)
        val txtBookImage: ImageView = view.findViewById(R.id.imgRecyclerRowItem)
        val bookItem: RelativeLayout = view.findViewById(R.id.bookItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row, parent, false)

        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = itemList[position]
        holder.txtBookAuthor.text = book.authName
        holder.txtBookName.text = book.bookName
        holder.txtBookPrice.text = book.price
        holder.txtBookRating.text = book.rating
        Picasso.get().load(book.bookImage).error(R.drawable.ic_launcher_foreground).into(holder.txtBookImage)

        holder.bookItem.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("bookId",book.bookId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}