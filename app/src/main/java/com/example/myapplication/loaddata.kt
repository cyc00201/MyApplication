package com.example.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import java.io.File
import java.lang.StringBuilder

class loaddata : AppCompatActivity() {


    var list: MutableList<String> = mutableListOf()

    var fileisselect = 0
    lateinit var  filename:String

    lateinit var linearlayout: LinearLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loaddata)

        val backbutton = findViewById<Button>(R.id.backbutton)
        backbutton.setOnClickListener { finish() }
        testfun()

    }

    override fun onResume() {
        super.onResume()
       linearlayout = findViewById(R.id.layout)
        val listView = ListView(this)
        val adapter = Adapter(this, list)
        listView.adapter = adapter
        linearlayout.addView(listView)

    }

    override fun finish() {
        //Toast.makeText(this,"finish",Toast.LENGTH_SHORT).show()


        sendDataBackToPreviousActivity()

        super.finish()
    }


    private fun sendDataBackToPreviousActivity() {

        if(fileisselect == 1) {
        val intent = Intent().apply {
            putExtra("filename", filename)
            // Put your data here if you want.
        }
        setResult(Activity.RESULT_OK, intent)
        }
        else{
            val intent = Intent()
            setResult(Activity.RESULT_CANCELED,intent)
        }
    }

    private fun testfun() {

        val path = getExternalFilesDir(null)


        File(path, "Painter").walk().forEach {

            var temp = it.toString().substring(72)
            if (!temp.isEmpty()) {
                temp = temp.substring(1)
                //Log.i("Terror",temp)
                list.add(temp)
            }
        }
    }


}



