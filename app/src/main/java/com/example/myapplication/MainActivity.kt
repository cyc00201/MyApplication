package com.example.myapplication

import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_DOWN
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

   // private var painter:Paint
    private lateinit var paintboard:PaintBoard
    private  lateinit var  paintwidthtext:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        paintwidthtext = findViewById(R.id.EditText)

        paintboard = findViewById(R.id.paint_board)


        //Toast.makeText(this,paintwidthtext.text.toString(),Toast.LENGTH_SHORT).show()

    }

    override fun onResume() {
        super.onResume()

        paintwidthtext.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                paintboard.setpainterwidh(paintwidthtext.text.toString().toFloat())
                return@OnKeyListener true
            }
            false
        })
    }







}


