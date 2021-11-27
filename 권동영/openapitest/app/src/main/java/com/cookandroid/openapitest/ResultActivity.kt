package com.cookandroid.openapitest

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    lateinit var txtName : TextView
    lateinit var txtAddress : TextView
    lateinit var txtDistance : TextView
    lateinit var btnNext : Button
    lateinit var btnPrev : Button
    lateinit var btnExit : Button

    lateinit var imgTest : ImageView

    var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result)

        txtName = findViewById(R.id.txtName)
        txtAddress = findViewById(R.id.txtAddress)
        txtDistance = findViewById(R.id.txtDistance)
        btnNext = findViewById(R.id.btnNext)
        btnPrev = findViewById(R.id.btnPrev)
        btnExit = findViewById(R.id.btnExit)
        imgTest = findViewById(R.id.imgTest)

        var intent = intent

        val list: ArrayList<ResultDTO>? = getIntent().getSerializableExtra("result") as ArrayList<ResultDTO>?

        txtName.setText(list!!.get(0).name.toString())
        txtAddress.setText(list!!.get(0).x.toString() + list!!.get(0).y.toString())
        txtDistance.setText(list!!.get(0).distance.toString())

        // imgTest.setImageResource(test1!!.get(0))
        val mHandler = Handler(Looper.getMainLooper())
        mHandler.post {

        }

        btnExit.setOnClickListener {
            finish()
        }
    }

}