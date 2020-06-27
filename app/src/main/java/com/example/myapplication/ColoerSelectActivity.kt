package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar

class ColoerSelectActivity : AppCompatActivity() {

    lateinit var brightness:SeekBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coloer_select)
        brightness = findViewById(R.id.brightness)


    }

    override fun onResume() {
        super.onResume()
        var bvalue:Int = 100
        brightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                bvalue = p1

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                var colorWheel:ColorWheel = findViewById(R.id.colorwheel)
                colorWheel.setbrightness(bvalue.toFloat()/100)

            }
        })
    }



}


