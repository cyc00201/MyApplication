package com.example.myapplication

import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

   // private var painter:Paint
    private lateinit var paintboard:PaintBoard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button :Button= findViewById(R.id.button)
        button.setOnClickListener { test() }
        paintboard = findViewById(R.id.paint_board)
    }
    fun test(){
        var paint:Paint
        paint = Paint()
        paint.setStrokeWidth(20f)
        paintboard.set_painter(paint)
    }
}
