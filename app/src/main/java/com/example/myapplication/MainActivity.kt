package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

   // private var painter:Paint
    private lateinit var paintboard:PaintBoard
    private  lateinit var  paintwidthtext:EditText


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("1","1")

        paintwidthtext = findViewById(R.id.EditText)

        paintboard = findViewById(R.id.paint_board)

        var zoomInButton: ImageButton =findViewById(R.id.zoomInButton)
        var zoomOutButton: ImageButton =findViewById(R.id.zoomOutButton)
        var btn_mover:ImageButton=findViewById(R.id.move_r)
        var btn_movel:ImageButton=findViewById(R.id.move_l)
        var btn_movet:ImageButton=findViewById(R.id.move_t)
        var btn_moveb:ImageButton=findViewById(R.id.move_b)
        zoomInButton.setOnClickListener{paintboard.setZoomIn()}
        zoomOutButton.setOnClickListener { paintboard.setZoomOut() }
        btn_mover.setOnClickListener { paintboard.move_canvas(1) }
        btn_movel.setOnClickListener { paintboard.move_canvas(2) }
        btn_movet.setOnClickListener { paintboard.move_canvas(3) }
        btn_moveb.setOnClickListener { paintboard.move_canvas(4) }


        //Toast.makeText(this,paintwidthtext.text.toString(),Toast.LENGTH_SHORT).show()


    }

    override fun onResume() {
        super.onResume()

        paintwidthtext.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                paintboard.setPainterWidh(paintwidthtext.text.toString().toFloat())

                val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
                return@OnKeyListener true
            }
            false
        })
    }



}


