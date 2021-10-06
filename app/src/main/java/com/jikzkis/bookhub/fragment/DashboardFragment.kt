package com.jikzkis.bookhub.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jikzkis.bookhub.R
import com.jikzkis.bookhub.adapter.DashboardRecyclerAdapter
import com.jikzkis.bookhub.model.Book
import org.json.JSONException

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DashboardFragment : Fragment() {

    private lateinit var recyclerDashboard: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager

    private lateinit var recyclerAdapter: DashboardRecyclerAdapter

    private lateinit var progressLayout : RelativeLayout
    private lateinit var progressBar: ProgressBar

    var bookInfoList = arrayListOf<Book>()

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_dashboard,
                                container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerDashboard = view.findViewById(R.id.recylcerDashboard)

        layoutManager = LinearLayoutManager(activity)

        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        progressLayout.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books/"

        val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null, Response.Listener {

            try {
                progressLayout.visibility = View.GONE
                val success = it.getBoolean("success")

                if (success) {
                    val data = it.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val bookJsonObject = data.getJSONObject(i)
                        val bookObject = Book(
                            bookJsonObject.getString("book_id"),
                            bookJsonObject.getString("name"),
                            bookJsonObject.getString("author"),
                            bookJsonObject.getString("rating"),
                            bookJsonObject.getString("price"),
                            bookJsonObject.getString("image")
                        )
                        bookInfoList.add(bookObject)

                        recyclerAdapter = DashboardRecyclerAdapter(activity as Context, bookInfoList)

                        recyclerDashboard.adapter = recyclerAdapter

                        recyclerDashboard.layoutManager = layoutManager
                    }
                } else {
                    Toast.makeText(context, "Error has Occurred", Toast.LENGTH_SHORT).show()
                }
            } catch(e: JSONException) {
                Toast.makeText(activity as Context, "Json Parsing Error",Toast.LENGTH_LONG).show()
            }

        }, Response.ErrorListener {
            Toast.makeText(activity as Context,"Volley Error Occurred",Toast.LENGTH_SHORT).show()
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "e829c3b782b731"
                return headers
            }
        }

        queue.add(jsonObjectRequest)
    }
}