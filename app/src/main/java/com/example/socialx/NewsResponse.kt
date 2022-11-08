package com.example.socialx

data class NewsResponse(
    val articles : List<News>
)

data class News(
    val source : Source,
    val title: String,
    val description : String,
    val url : String,
    val urlToImage : String
)

data class Source(
    val name : String
)
