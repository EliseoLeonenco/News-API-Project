package com.example.newsapiproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var quitBtn: Button
    private lateinit var continueBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        continueBtn = findViewById(R.id.continue_btn)
        quitBtn = findViewById(R.id.quit_btn)

        // continue button moves to NewsSearchActivity
        continueBtn.setOnClickListener {
            val intent = Intent(this, NewsSearchActivity::class.java);
            startActivity(intent)
        }

        // Quit button exits the application
        quitBtn.setOnClickListener {
            finish()
        }
    }
}