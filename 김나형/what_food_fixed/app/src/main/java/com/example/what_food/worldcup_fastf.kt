package com.example.what_food
//결승전
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class worldcup_fastf : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.worldcup_fastf)
    //초기버튼 정의 및 실행
        var btninit = findViewById<ImageButton>(R.id.HomeButton3)

        btninit.setOnClickListener{
            var goto_init = Intent(applicationContext, second::class.java)
            startActivity(goto_init)
            finish()
        }
        var newName = kotlin.arrayOfNulls<String>(2)
        var newFoodImage = IntArray(2)
        var intent = intent
        var voteResult = intent.getIntArrayExtra("VoteCount1")
        var FoodName = intent.getStringArrayExtra("ImageName1")
        var FoodImage = intent.getIntArrayExtra("WinFoodImage")

        var select = 0
        var x = 0
        for(i in voteResult!!.indices) {
            if (voteResult[i] == 1)
            {
                newName[x] = FoodName!![i]
                newFoodImage.set(x,FoodImage!!.get(i))
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

        left.setBackgroundResource(newFoodImage.get(select))
        right.setBackgroundResource(newFoodImage.get(select+1))
        leftName.setText(newName[select])
        rightName.setText(newName[select+1])

        left.setOnClickListener {
            Toast.makeText(applicationContext, "1우승", Toast.LENGTH_SHORT).show()
            var resultintent = Intent(this, worldcup_fast_result::class.java)
            resultintent.putExtra("resultMenuImage",newFoodImage.get(select))
            resultintent.putExtra("resultMenuName",newName[select])
            startActivity(resultintent)
        }
        right.setOnClickListener {
            Toast.makeText(applicationContext, "2우승", Toast.LENGTH_SHORT).show()
            var resultintent = Intent(this, worldcup_fast_result::class.java)
            resultintent.putExtra("resultMenuImage",newFoodImage.get(select+1))
            resultintent.putExtra("resultMenuName",newName[select+1])
            startActivity(resultintent)
        }
    }
}