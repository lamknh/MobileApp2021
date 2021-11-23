package com.example.ui

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

class selectfinal : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selectfinal)

        var newName = kotlin.arrayOfNulls<String>(2)
        var newFoodImage =Array(2,{0})
        var intent = intent
        var voteResult = intent.getIntArrayExtra("VoteCount1")
        var FoodName = intent.getStringArrayExtra("ImageName1")
        var FoodImage = arrayOf(R.drawable.five, R.drawable.six, R.drawable.tteokbokki, R.drawable.ramen)
        //var FoodImage = intent.getIntArrayExtra("WinFoodImage")

        var select = 0
        var x = 0
        for(i in voteResult!!.indices) {
            if (voteResult[i] == 1)
            {
                newName[x] = FoodName!![i]
                newFoodImage[x] = FoodImage!![i]
                x++
            }
        }

        var left = findViewById<TextView>(R.id.leftFood2)
        var right = findViewById<TextView>(R.id.rightFood2)
        var leftName = findViewById<TextView>(R.id.leftFoodName2)
        var rightName = findViewById<TextView>(R.id.rightFoodName2)

        left.setBackgroundResource(newFoodImage[select])
        right.setBackgroundResource(newFoodImage[select+1])
        leftName.setText(newName[select])
        rightName.setText(newName[select+1])

        left.setOnClickListener {
            Toast.makeText(applicationContext, "1우승", Toast.LENGTH_SHORT).show()
        }
        right.setOnClickListener {
            Toast.makeText(applicationContext, "2우승", Toast.LENGTH_SHORT).show()
        }


    }


}