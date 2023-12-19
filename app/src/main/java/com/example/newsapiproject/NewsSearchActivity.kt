package com.example.newsapiproject

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import java.time.LocalDate

class NewsSearchActivity : AppCompatActivity() {

    private lateinit var article: Article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_search)
        
        updateArticles()

    }


    private fun updateArticles() {
        val url = "https://newsapi.org/v2/everything?q=bitcoin&searchIn=title&language=en&apiKey=710119f4520a4c25b4ab12e46322e7db"

        val jsonObjectRequest = @RequiresApi(Build.VERSION_CODES.O)
        object : JsonObjectRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                Log.d("API_RESPONSE", response.toString())

                val articlesArray = response.optJSONArray("articles")
                val articlesList = ArrayList<Article>()
                if (articlesArray != null && articlesArray.length() > 0) {
                    for (i in 0 until articlesArray.length()) {
                        val articleJson = articlesArray.getJSONObject(i)
                        val publishedDate = LocalDate.parse(articleJson.getString("publishedAt").split("T")[0])
                        val article = Article(
                            title = articleJson.optString("title", "No available title"),
                            author = articleJson.optString("author", "No available author"),
                            publishedDate = publishedDate,
                            urlSource = articleJson.optString("url", "No available source")
                        )
                        articlesList.add(article)
                    }
                    // Now you have all articles in articlesList
                    val recyclerView = findViewById<RecyclerView>(R.id.articlesRecyclerView)
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.adapter = ArticleAdapter(articlesList)
                } else {
                    Log.d("API_RESPONSE", "No articles found")
                }
            },
            Response.ErrorListener { error ->
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    val errorResp = String(error.networkResponse.data)
                    Log.e("API_ERROR", "Error response: $errorResp")
                } else {
                    Log.e("API_ERROR", "Error message: ${error.message}")
                }
            }
        ) {
            override fun getHeaders(): Map<String, String>? {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }
}