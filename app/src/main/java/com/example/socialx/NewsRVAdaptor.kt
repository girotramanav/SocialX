package com.example.socialx

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsRVAdaptor(private val context : Context , private val listener : INewsRVAdaptor) : RecyclerView.Adapter<NewsRVAdaptor.NewsViewHolder>() {

    private var allNews = ArrayList<News>()

    inner class NewsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val title : TextView = itemView.findViewById(R.id.news_title)
        val description : TextView = itemView.findViewById(R.id.news_desc)
        val thumbnail : ImageView = itemView.findViewById(R.id.news_thumbnail)
        val card : CardView = itemView.findViewById(R.id.news_card)
        val source : TextView = itemView.findViewById(R.id.source)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val viewHolder = NewsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news_rv , parent , false))
        viewHolder.card.setOnClickListener {
            listener.onNewsClicked(viewHolder.adapterPosition)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currNews = allNews[position]
        holder.title.text = currNews.title
        holder.description.text = currNews.description
        Glide.with(context).load(currNews.urlToImage).into(holder.thumbnail)
        holder.source.text = "2 hours ago   ${currNews.source.name}"
    }

    override fun getItemCount(): Int {
        return allNews.size
    }

    fun updateNewsList(newList : ArrayList<News>){
        allNews = newList
        notifyDataSetChanged()
    }

}

interface INewsRVAdaptor{
    fun onNewsClicked(position : Int)
}