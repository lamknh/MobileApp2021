package com.example.what_food
//4강
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class worldcup_des2 : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.worldcup_des2)
//초기버튼 정의 및 실행
        var btninit = findViewById<ImageButton>(R.id.HomeButton2)

        btninit.setOnClickListener{
            var goto_init = Intent(applicationContext, second::class.java)
            startActivity(goto_init)
            finish()
        }
        var newName = kotlin.arrayOfNulls<String>(4)
        var newFoodImage =IntArray(4)
        var intent = intent
        var voteResult = intent.getIntArrayExtra("VoteCount")
        var FoodName = intent.getStringArrayExtra("ImageName")
        var FoodImage = intent.getIntArrayExtra("WinFoodImage")
        var cupmaintext = arrayOf("4강 1/2","4강 2/2" )
        var round = 4
        var now = 1
        var select = 0
        var next = 2
        var x = 0
        for(i in voteResult!!.indices) {
            if (voteResult[i] == 1)
            {
                newName[x] = FoodName!![i]
                newFoodImage!!.set(x,FoodImage!!.get(i))
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

        left.setBackgroundResource(newFoodImage!!.get(select))
        right.setBackgroundResource(newFoodImage.get(select+1))
        leftName.setText(newName[select])
        rightName.setText(newName[select+1])

        left.setOnClickListener {
            if(round <= next)
            {
                newVote[select]++
                var nextintent1 = Intent(this, worldcup_desf::class.java)
                nextintent1.putExtra("VoteCount1", newVote)
                nextintent1.putExtra("ImageName1", newName)
                nextintent1.putExtra("WinFoodImage",newFoodImage)
                startActivity(nextintent1)

            }
            else {
                newVote[select]++
                select += 2
                cupmain.setText(cupmaintext[now])
                now++
                left.setBackgroundResource(newFoodImage.get(next))
                leftName.setText(newName[next])
                right.setBackgroundResource(newFoodImage.get(next + 1))
                rightName.setText(newName[next + 1])
                next += 2
            }
        }
        right.setOnClickListener {
            if(round <= next)
            {
                newVote[select + 1]++
                var nextintent1 = Intent(this, worldcup_desf::class.java)
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
                left.setBackgroundResource(newFoodImage.get(next))
                leftName.setText(newName[next])
                right.setBackgroundResource(newFoodImage.get(next + 1))
                rightName.setText(newName[next + 1])
                next += 2
            }
        }
    }
}