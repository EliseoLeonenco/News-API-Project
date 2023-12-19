package com.example.newsapiproject

import java.time.LocalDate

data class Article(var title: String, var author: String, var publishedDate: LocalDate, var urlSource:String)
