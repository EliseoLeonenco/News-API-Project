package com.example.newsapiproject

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import java.time.LocalDate
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.SearchView
import java.util.*


class NewsSearchActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var searchView: SearchView
    private lateinit var findButton: Button
    private lateinit var spacingItemDecoration: SpacingItemDecoration
    private lateinit var dividerItemDecoration: DividerItemDecoration
    private lateinit var tts: TextToSpeech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_search)

        searchView = findViewById<SearchView>(R.id.searchView)
        findButton = findViewById<Button>(R.id.findButton)
        tts = TextToSpeech(this, this)

        findButton.setOnClickListener {
            run {
                val keyWord = searchView.query.toString()
                updateArticles(keyWord)
            }
        }
    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.ENGLISH)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported")
            } else {
                // Ready
            }
        } else {
            Log.e("TTS", "Initialization fail")
        }
    }

    fun speakOut(text: String) {
        val speechStatus = tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Speech synthesis failed")
        }
    }

    private fun updateArticles(keyWord: String) {

        val apiKey = "710119f4520a4c25b4ab12e46322e7db"
        val url = "https://newsapi.org/v2/everything?q=$keyWord&searchIn=title&language=en&apiKey=$apiKey"

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
                            title = articleJson.optString("title"),
                            author = articleJson.optString("author"),
                            publishedDate = publishedDate,
                            urlSource = articleJson.optString("url")
                        )
                        articlesList.add(article)
                    }
                    // All articles in articlesList
                    val recyclerView = findViewById<RecyclerView>(R.id.articlesRecyclerView)
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.adapter = ArticleAdapter(articlesList, this)

                    // Remove existing SpacingItemDecoration and DividerItemDecoration if already exist (to stop it from adding continuously adding spacing)
                    if (::spacingItemDecoration.isInitialized) {
                        recyclerView.removeItemDecoration(spacingItemDecoration)
                    }
                    if (::dividerItemDecoration.isInitialized) {
                        recyclerView.removeItemDecoration(dividerItemDecoration)
                    }

                    // Add SpacingItemDecoration and DividerItemDecoration
                    spacingItemDecoration = SpacingItemDecoration(30)
                    recyclerView.addItemDecoration(spacingItemDecoration)

                    dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
                    recyclerView.addItemDecoration(dividerItemDecoration)

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
            // Add headers
            override fun getHeaders(): Map<String, String>? {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }

    class SpacingItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount?.minus(1)) {
                outRect.bottom = spaceHeight
                outRect.top = spaceHeight

            }
        }
    }



}