package com.example.what_food

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class FailActivity : Activity() {
    lateinit var btnExit : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fail)

        btnExit = findViewById(R.id.btnExit)

        btnExit.setOnClickListener {
            finish()
        }
    }
}