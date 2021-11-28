package com.example.what_food
//8강
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*

class worldcup_world1 : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.worldcup_world1)
        // round 체크
        var round = 8
        var now = 1
        var select = 0
        var next = 2
        // 음식 렌덤 이미지 8개
        var FoodImage = intArrayOf(R.drawable.one , R.drawable.two, R.drawable.three,
            R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.tteokbokki, R.drawable.ramen)

        //초기버튼 정의 및 실행
        var btninit = findViewById<ImageButton>(R.id.HomeButton1)

        btninit.setOnClickListener{
            var goto_init = Intent(applicationContext, second::class.java)
            startActivity(goto_init)
            finish()
        }

        // 음식  이름
        var FoodName = arrayOf("1","2","3","4","5","6","떡볶이", "라면")
        var cupmaintext = arrayOf("8강 1/4","8강 2/4","8강 3/4","8강 4/4" )
        // left, right 버튼
        var left = findViewById<TextView>(R.id.leftFood)
        var right = findViewById<TextView>(R.id.rightFood)
        var leftName = findViewById<TextView>(R.id.leftFoodName)
        var rightName = findViewById<TextView>(R.id.rightFoodName)
        var cupmain = findViewById<TextView>(R.id.cup)
        left.setBackgroundResource(FoodImage[select])
        leftName.setText(FoodName[select])
        right.setBackgroundResource(FoodImage[select+1])
        rightName.setText(FoodName[select+1])

        //음식 투표 수 초기 카운트는 0으로
        var voteCount = IntArray(8)
        for (i in 0..7)
            voteCount[i] = 0

        left.setOnClickListener {
            if(round <= next)
            {
                voteCount[select]++
                var nextintent = Intent(this, worldcup_ch2::class.java)
                nextintent.putExtra("VoteCount", voteCount)
                nextintent.putExtra("ImageName", FoodName)
                nextintent.putExtra("WinFoodImage", FoodImage)
                startActivity(nextintent)
            }
            else {
                voteCount[select]++
                select += 2
                cupmain.setText(cupmaintext[now])
                now++
                left.setBackgroundResource(FoodImage[next])
                leftName.setText(FoodName[next])
                right.setBackgroundResource(FoodImage[next + 1])
                rightName.setText(FoodName[next + 1])
                next += 2
            }
        }
        right.setOnClickListener {
            if(round <= next)
            {
                voteCount[select + 1]++
                var nextintent = Intent(this, worldcup_world2::class.java)
                nextintent.putExtra("VoteCount", voteCount)
                nextintent.putExtra("ImageName", FoodName)
                nextintent.putExtra("WinFoodImage", FoodImage)
                startActivity(nextintent)
            }
            else {
                voteCount[select + 1]++
                select += 2
                cupmain.setText(cupmaintext[now])
                now++
                left.setBackgroundResource(FoodImage[next])
                leftName.setText(FoodName[next])
                right.setBackgroundResource(FoodImage[next + 1])
                rightName.setText(FoodName[next + 1])
                next += 2
            }
        }
    }
}