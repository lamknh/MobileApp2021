package com.example.what_food


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton

class end : Activity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.no_food)

        var back_init : ImageButton = findViewById<ImageButton>(R.id.back_init)

        back_init.setOnClickListener{
            var gotostart = Intent(applicationContext, MainActivity::class.java)
            startActivity(gotostart)
            finish()
        }

    }
}