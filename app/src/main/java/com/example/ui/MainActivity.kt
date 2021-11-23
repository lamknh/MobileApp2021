package com.example.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var S_btn : ImageButton = findViewById<ImageButton>(R.id.Start_btn)

        S_btn.setOnClickListener {
            var second = Intent(applicationContext, second::class.java)
            startActivity(second)
            finish()
        }
    }
}