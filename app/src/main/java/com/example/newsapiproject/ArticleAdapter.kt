package com.example.newsapiproject

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView

class ArticleAdapter(private val articles: List<Article>, private val context: Context) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.article_title)
        val author: TextView = itemView.findViewById(R.id.article_author)
        val publishedDate: TextView = itemView.findViewById(R.id.article_published_date)
        val speechIcon: ImageButton = itemView.findViewById(R.id.speech_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.title.text = article.title
        holder.publishedDate.text = article.publishedDate.toString()
        if(article.author != "null"){
            holder.author.text = article.author
        } else {
            holder.author.text = "No available author"
        }

        // Set OnClickListener for each item
        holder.itemView.setOnClickListener {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("URL", article.urlSource)
            context.startActivity(intent)
        }

        // Set OnClickListener for speech icon
        holder.speechIcon.setOnClickListener {
            var author = article.author
            if (author == "null"){
                author = "Unknown author"
            }
            var speechText = article.title + ", published by, " + author
            (context as NewsSearchActivity).speakOut(speechText)
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }
}
