package com.example.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView

class SelectFood : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selectfood)

        var newName = kotlin.arrayOfNulls<String>(4)
        var newFoodImage =Array(4,{0})
        var intent = intent
        var voteResult = intent.getIntArrayExtra("VoteCount")
        var FoodName = intent.getStringArrayExtra("ImageName")
        var FoodImage = arrayOf(R.drawable.one , R.drawable.two, R.drawable.three,
           R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.tteokbokki, R.drawable.ramen)
        //var FoodImage = intent.getIntegerArrayListExtra("WinFoodImage")
        var cupmaintext = arrayOf("2강 1/2","2강 2/2" )
        var round = 4
        var now = 1
        var select = 0
        var next = 2
        var x = 0
        for(i in voteResult!!.indices) {
            if (voteResult[i] == 1)
            {
                newName[x] = FoodName!![i]
                newFoodImage[x] = FoodImage!![i]
                x++
            }
        }

        var newVote = IntArray(4)
        for(i in 0..3)
            newVote[i] = 0
        var left = findViewById<TextView>(R.id.leftFood1)
        var right = findViewById<TextView>(R.id.rightFood1)
        var leftName = findViewById<TextView>(R.id.leftFoodName1)
        var rightName = findViewById<TextView>(R.id.rightFoodName1)
        var cupmain = findViewById<TextView>(R.id.cup)

        left.setBackgroundResource(newFoodImage[select])
        right.setBackgroundResource(newFoodImage[select+1])
        leftName.setText(newName[select])
        rightName.setText(newName[select+1])

        left.setOnClickListener {
            if(round <= next)
            {
                newVote[select + 1]++
                var nextintent1 = Intent(this, selectfinal::class.java)
                nextintent1.putExtra("VoteCount1", newVote)
                nextintent1.putExtra("ImageName1", newName)
                nextintent1.putExtra("WinFoodImage",newFoodImage)
                startActivity(nextintent1)

            }
            else {
                newVote[select + 1]++
                select += 2
                cupmain.setText(cupmaintext[now])
                now++
                left.setBackgroundResource(newFoodImage[next])
                leftName.setText(newName[next])
                right.setBackgroundResource(newFoodImage[next + 1])
                rightName.setText(newName[next + 1])
                next += 2
            }
        }
        right.setOnClickListener {
            if(round <= next)
            {
                newVote[select + 1]++
                var nextintent1 = Intent(this, selectfinal::class.java)
                nextintent1.putExtra("VoteCount1", newVote)
                nextintent1.putExtra("ImageName1", newName)
                nextintent1.putExtra("WinFoodImage", newFoodImage)
                startActivity(nextintent1)
            }
            else {
                newVote[select + 1]++
                select += 2
                cupmain.setText(cupmaintext[now])
                now++
                left.setBackgroundResource(newFoodImage[next])
                leftName.setText(newName[next])
                right.setBackgroundResource(newFoodImage[next + 1])
                rightName.setText(newName[next + 1])
                next += 2
            }
        }
    }
}