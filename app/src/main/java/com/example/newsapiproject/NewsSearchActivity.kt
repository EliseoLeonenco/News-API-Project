package com.example.newsapiproject

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class NewsSearchActivity : AppCompatActivity() {

    private lateinit var data: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_search)

        data = findViewById(R.id.data)

        updateArticles()

    }


    private fun updateArticles() {
        val url = "https://newsapi.org/v2/everything?q=bitcoin&searchIn=title&language=en&apiKey=710119f4520a4c25b4ab12e46322e7db"

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET, url, null,
            Response.Listener { response ->
                // Log response to Logcat for debugging
                Log.d("API_RESPONSE", response.toString())

                val articlesArray = response.optJSONArray("articles")
                if (articlesArray != null && articlesArray.length() > 0) {
                    val firstArticle = articlesArray.getJSONObject(0)
                    val title = firstArticle.optString("title", "No available title")
                    data.text = title
                } else {
                    data.text = "No articles found"
                }
            },
            Response.ErrorListener { error ->
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    val errorResp = String(error.networkResponse.data)
                    Log.e("API_ERROR", "Error response: $errorResp")
                    data.text = "Error: $errorResp"
                } else {
                    Log.e("API_ERROR", "Error message: ${error.message}")
                    data.text = "Error: ${error.message ?: "Unknown error"}"
                }
            }
        ) {
            // Add Headers
            override fun getHeaders(): Map<String, String>? {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        // Add request to the queue
        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }
}