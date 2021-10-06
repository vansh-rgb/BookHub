package com.jikzkis.bookhub.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jikzkis.bookhub.R
import com.jikzkis.bookhub.database.BookDatabase
import com.jikzkis.bookhub.database.BookEntity
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject


class DescriptionActivity : AppCompatActivity() {
    lateinit var txtBookName: TextView
    lateinit var txtBookAuthor: TextView
    lateinit var txtBookPrice: TextView
    lateinit var txtBookRating: TextView
    lateinit var imgBookImage: ImageView
    lateinit var txtBookDesc: TextView
    private lateinit var btnAddToFav: Button
    private lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout

    lateinit var toolBar: Toolbar

    private var bookId : String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        imgBookImage = findViewById(R.id.imgBookImage)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        btnAddToFav = findViewById(R.id.btnAddToFav)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

        toolBar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)

        if(intent!=null) {
            bookId = intent.getStringExtra("bookId").toString()
        } else {
            finish()
            Toast.makeText(this,"Some Error Occurred",Toast.LENGTH_SHORT).show()
        }

        if(bookId == "100") {
            finish()
            Toast.makeText(this,"Some Error Occurred",Toast.LENGTH_SHORT).show()
        }

        val queue = Volley.newRequestQueue(this)

        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()

        jsonParams.put("book_id", bookId)

        val jsonRequest = @SuppressLint("SetTextI18n")
        object : JsonObjectRequest(Method.POST,url,jsonParams, Response.Listener {

            try {
                val success = it.getBoolean("success")
                if(success) {
                    val bookJsonObject = it.getJSONObject("book_data")
                    progressLayout.visibility = View.GONE

                    supportActionBar?.title = bookJsonObject.getString("name")

                    txtBookName.text = bookJsonObject.getString("name")
                    txtBookAuthor.text = bookJsonObject.getString("author")
                    txtBookDesc.text = bookJsonObject.getString("description")
                    txtBookPrice.text = bookJsonObject.getString("price")
                    txtBookRating.text = bookJsonObject.getString("rating")
                    Picasso.get().load(bookJsonObject.getString("image")).error(R.drawable.ic_launcher_foreground).into(imgBookImage)
                    val bookImageUrl: String = bookJsonObject.getString("image")

                    val bookEntity = BookEntity(
                        bookId?.toInt() as Int,
                        txtBookName.text.toString(),
                        txtBookAuthor.text.toString(),
                        txtBookPrice.text.toString(),
                        txtBookPrice.text.toString(),
                        txtBookDesc.text.toString(),
                        bookImageUrl
                    )
                    val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
                    val isFav = checkFav.get()

                    if(isFav) {
                        btnAddToFav.text = "Remove from Favourites"
                        val favColor = ContextCompat.getColor(applicationContext, R.color.red)
                        btnAddToFav.setBackgroundColor(favColor)
                    } else {
                        btnAddToFav.text = "Add to Favourites"
                        val noFavColor = ContextCompat.getColor(applicationContext, R.color.purple_200)
                        btnAddToFav.setBackgroundColor(noFavColor)
                    }

                    btnAddToFav.setOnClickListener {
                        if(!DBAsyncTask(applicationContext, bookEntity, 1).execute().get()) {
                            val async = DBAsyncTask(applicationContext, bookEntity, 2).execute()
                            val result = async.get()
                            if(result) {
                                Toast.makeText(
                                    this@DescriptionActivity,
                                    "Book Added to Favourites",
                                    Toast.LENGTH_SHORT
                                ).show()
                                btnAddToFav.text = "Remove from Favourites"
                                val favColor = ContextCompat.getColor(applicationContext, R.color.red)
                                btnAddToFav.setBackgroundColor(favColor)
                            } else {
                                Toast.makeText(
                                    this@DescriptionActivity,
                                    "Some error occurred",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            val async = DBAsyncTask(applicationContext, bookEntity, 3).execute()
                            val result = async.get()
                            if(result) {
                                Toast.makeText(
                                    this@DescriptionActivity,
                                    "Book Removed from Favourites",
                                    Toast.LENGTH_SHORT
                                ).show()
                                btnAddToFav.text = "Add to Favourites"
                                val noFavColor = ContextCompat.getColor(applicationContext, R.color.purple_200)
                                btnAddToFav.setBackgroundColor(noFavColor)
                            } else {
                                Toast.makeText(
                                    this@DescriptionActivity,
                                    "Some error occurred",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                } else {
                    Toast.makeText(this,"Something went Wrong",Toast.LENGTH_SHORT).show()
                }
            } catch (e:JSONException) {
                Toast.makeText(this,"Json Parsing Error",Toast.LENGTH_SHORT).show()
            }

        },Response.ErrorListener {
            Toast.makeText(this,"Some Error Occurred",Toast.LENGTH_SHORT).show()
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "token_here"
                return headers
            }
        }

        queue.add(jsonRequest)

    }


    /*class FavManager(val context: Context, private val bookEntity: BookEntity, private val mode: Int) {

        private val db = Room.databaseBuilder(context, BookDatabase::class.java,"books_db").build()
        fun executeAsync(): Boolean {
            thread {

            }

            return false
        }
    }*/
    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) : AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, BookDatabase::class.java,"books_db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {

            when(mode) {
                1->{
                    val book: BookEntity = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return true
                }
                2->{
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3->{
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }
}