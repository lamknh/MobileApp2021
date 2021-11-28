package com.example.what_food

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton

class second : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_food)

        var ko_food : ImageButton = findViewById<ImageButton>(R.id.ko_food)
        var jp_food : ImageButton = findViewById<ImageButton>(R.id.jp_food)
        var ch_food : ImageButton = findViewById<ImageButton>(R.id.ch_food)
        var fast_food : ImageButton = findViewById<ImageButton>(R.id.fast_food)
        var west_food : ImageButton = findViewById<ImageButton>(R.id.west_food)
        var world_food : ImageButton = findViewById<ImageButton>(R.id.world_food)
        var dessert : ImageButton = findViewById<ImageButton>(R.id.dessert)
        var snack : ImageButton = findViewById<ImageButton>(R.id.snack)
        var btnReturn = findViewById<ImageButton>(R.id.back)

        //뒤로가기 버튼
        btnReturn.setOnClickListener{
            var goto_init = Intent(applicationContext, MainActivity::class.java)
            startActivity(goto_init)
            finish()
        }

        ko_food.setOnClickListener{
            var goto_kr = Intent(applicationContext, worldcup_ko1::class.java)
            startActivity(goto_kr)
            finish()
        }

        jp_food.setOnClickListener{
                    var goto_jp = Intent(applicationContext, worldcup_jp1::class.java)
                    startActivity(goto_jp)
                   finish()
        }

        ch_food.setOnClickListener{
                    var goto_ch = Intent(applicationContext, worldcup_ch1::class.java)
                        startActivity(goto_ch)
                        finish()
        }

        fast_food.setOnClickListener{
                        var goto_fast = Intent(applicationContext, worldcup_fast1::class.java)
                        startActivity(goto_fast)
                        finish()
        }

        west_food.setOnClickListener{
                        var goto_west = Intent(applicationContext, worldcup_west1::class.java)
                        startActivity(goto_west)
                        finish()
        }

        world_food.setOnClickListener{
                        var goto_world = Intent(applicationContext, worldcup_world1::class.java)
                        startActivity(goto_world)
                        finish()
        }

        snack.setOnClickListener{
                        var goto_snack = Intent(applicationContext, worldcup_snack1::class.java)
                        startActivity(goto_snack)
                        finish()
        }

        dessert.setOnClickListener{
            var goto_dessert = Intent(applicationContext, worldcup_des1::class.java)
            startActivity(goto_dessert)
            finish()
        }
    }
}