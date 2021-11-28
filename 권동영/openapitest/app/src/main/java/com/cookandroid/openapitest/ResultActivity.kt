package com.cookandroid.openapitest

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    lateinit var txtPosition : TextView
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

        txtPosition = findViewById(R.id.txtPosition)
        txtName = findViewById(R.id.txtName)
        txtAddress = findViewById(R.id.txtAddress)
        txtDistance = findViewById(R.id.txtDistance)
        btnNext = findViewById(R.id.btnNext)
        btnPrev = findViewById(R.id.btnPrev)
        btnExit = findViewById(R.id.btnExit)
        imgTest = findViewById(R.id.imgTest)

        var intent = intent

        val list: ArrayList<ResultDTO>? = getIntent().getSerializableExtra("result") as ArrayList<ResultDTO>?
        val getLongitude = intent.getDoubleExtra("getLongitude", 0.0)
        val getLatitude = getIntent().getDoubleExtra("getLatitude", 0.0)

        txtPosition.setText("현재 경도 : " + getLongitude + ", 현재 위도 : " + getLatitude)
        txtName.setText(list!!.get(0).name)
        txtAddress.setText("경도 : " + list!!.get(0).x + ", 위도 : " + list!!.get(0).y)
        txtDistance.setText(list!!.get(0).distance)

        // imgTest.setImageResource(test1!!.get(0))
        val mHandler = Handler(Looper.getMainLooper())
        mHandler.post {

        }

        btnNext.setOnClickListener {
            index++
            if(index >= list.size)
                index = 0
            txtName.setText(list!!.get(index).name)
            txtAddress.setText("경도 : " + list!!.get(index).x + ", 위도 : " + list!!.get(index).y)
            txtDistance.setText(list!!.get(index).distance)
        }

        btnPrev.setOnClickListener {
            index--
            if(index < 0)
                index = list.size - 1
            txtName.setText(list!!.get(index).name)
            txtAddress.setText("경도 : " + list!!.get(index).x + ", 위도 : " + list!!.get(index).y)
            txtDistance.setText(list!!.get(index).distance)
        }

        btnExit.setOnClickListener {
            finish()
        }
    }

}