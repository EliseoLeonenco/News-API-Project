package com.example.newsapiproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var quit_btn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        quit_btn = findViewById(R.id.quit_btn)

        //quit button exits the application
        quit_btn.setOnClickListener {
            finish()
        }
    }
}