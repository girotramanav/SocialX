package com.example.socialx

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialx.databinding.ActivityHomeBinding
import com.example.socialx.network.Api
import com.example.socialx.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() , INewsRVAdaptor {

    private lateinit var _binding : ActivityHomeBinding
    private lateinit var adapter : NewsRVAdaptor
    private lateinit var newsList : ArrayList<News>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        supportActionBar?.hide()
        adapter = NewsRVAdaptor(this , this)
        _binding.newsRecyclerView.layoutManager = LinearLayoutManager(this)
        _binding.newsRecyclerView.adapter = adapter
        _binding.progressLayout.visibility = View.VISIBLE
        getTopNews()

        _binding.searchBar.setOnEditorActionListener { _, i, _ ->
            if(i == EditorInfo.IME_ACTION_SEARCH){
                _binding.progressLayout.visibility = View.VISIBLE
                Log.d("MyInfo", "Clicked")
                getTopicNews()
            }
            false
        }
    }


    private fun getTopNews(){
        val service = RetrofitInstance.retrofitInstance?.create(Api::class.java)
        val call = service?.getNews("in" , getString(R.string.api_key))
        call?.enqueue(object : Callback<NewsResponse>{
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if(response.isSuccessful){
                    Log.d("MyInfo" , "Success :" + response.body())
                    newsList = ((response.body()?.articles as ArrayList<News>?)!!)
                    adapter.updateNewsList(newsList)
                    _binding.progressLayout.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.d("MyInfo", "Failed Call : ${t.message}")
                _binding.progressLayout.visibility = View.GONE
            }
        })
    }

    private fun getTopicNews(){
        val service = RetrofitInstance.retrofitInstance?.create(Api::class.java)
        val call = service?.getTopicNews(_binding.searchBar.text.toString() , getString(R.string.api_key))
        call?.enqueue(object : Callback<NewsResponse>{
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if(response.isSuccessful){
                    Log.d("MyInfo" , "Success :" + response.body())
                    newsList = ((response.body()?.articles as ArrayList<News>?)!!)
                    adapter.updateNewsList(newsList)
                    _binding.progressLayout.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.d("MyInfo", "Failed Call : ${t.message}")
                _binding.progressLayout.visibility = View.GONE
            }
        })
    }

    override fun onNewsClicked(position: Int) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(newsList[position].url)
        startActivity(intent)
    }
}