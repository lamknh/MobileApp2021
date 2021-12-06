package com.cookandroid.openapitest

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class FailActivity : Activity() {
    lateinit var btnExit : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fail)

        btnExit = findViewById(R.id.btnExit)

        btnExit.setOnClickListener {
            finish()
        }
    }
}