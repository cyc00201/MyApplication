package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Albums.getContentUri
import android.provider.MediaStore.Images.Media.getContentUri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.nio.file.Files.createFile

class MainActivity : AppCompatActivity() {

   // private var painter:Paint
    private lateinit var paintboard:PaintBoard
    private  lateinit var  paintwidthtext:EditText
    private  lateinit var  savebutton:Button

    private  lateinit var  openfilebutton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        paintwidthtext = findViewById(R.id.EditText)

        paintboard = findViewById(R.id.paint_board)

        savebutton = findViewById(R.id.button3)
       openfilebutton = findViewById(R.id.button5)
        //Toast.makeText(this,paintwidthtext.text.toString(),Toast.LENGTH_SHORT).show()



    }

    override fun onResume() {
        super.onResume()

        savebutton.setOnClickListener { saveClickHandler ()}
        openfilebutton.setOnClickListener { open() }
        paintwidthtext.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                paintboard.setpainterwidh(paintwidthtext.text.toString().toFloat())

                val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
                return@OnKeyListener true
            }
            false
        })



    }


    private fun open(){

        val intent = Intent(this, loaddata::class.java)
        startActivityForResult(intent, REQUEST_CODE)


    }

    companion object {
        const val REQUEST_CODE = 0
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val message:String = data!!.getStringExtra("filename")
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                val path = getExternalFilesDir(null)
                val dir = File(path,"Painter")
                val file = File(dir,message)

                paintboard.setBitmap( BitmapFactory.decodeStream(file.inputStream()))

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun checkWritable():Boolean {

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),0)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_MEDIA_LOCATION),0)
            return false
        }  else {
            return true
        }
    }




    private fun saveClickHandler (){

        if(checkWritable()){
            try {

                val fileName = (System.currentTimeMillis() / 1000).toString() + ".jpg"
                val path = getExternalFilesDir(null)
                val dir = File(path,"Painter");//File("storage/emulated/0/test.jpg")

                if(!dir.exists()){
                dir.mkdirs()
                }

                val file = File(dir, fileName)


                val stream = FileOutputStream(file)
                paintboard.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()
                stream.close()


                Toast.makeText(this, "Save Success" + file.path, Toast.LENGTH_SHORT).show()

            } catch(e:Exception) {

                Log.i("Terror",e.toString())
                Toast.makeText(this, "Save Failed$e", Toast.LENGTH_SHORT).show()

            }
        }
       else{
            Toast.makeText(this,"???",Toast.LENGTH_LONG).show()
        }

    }

}


