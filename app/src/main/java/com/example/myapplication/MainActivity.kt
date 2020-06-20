package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.ComponentCallbacks2
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
import android.widget.*
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
    private  lateinit var  colorbutton:Button
    private  lateinit var  redobutton: ImageButton
    private  lateinit var  undobutton: ImageButton
    private  lateinit var  openfilebutton:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        paintwidthtext = findViewById(R.id.EditText)
        paintboard = findViewById(R.id.paint_board)
        colorbutton = findViewById(R.id.button)
        savebutton = findViewById(R.id.button3)
        redobutton = findViewById(R.id.imageButton2)
        undobutton = findViewById(R.id.imageButton)
       openfilebutton = findViewById(R.id.button5)

        //Toast.makeText(this,paintwidthtext.text.toString(),Toast.LENGTH_SHORT).show()



    }

    override fun onResume() {
        super.onResume()

        savebutton.setOnClickListener { saveClickHandler ()}
        openfilebutton.setOnClickListener { open() }
        redobutton.setOnClickListener { paintboard.unredo(1) }
        undobutton.setOnClickListener {paintboard.unredo(-1)  }
        colorbutton.setOnClickListener { colorselect() }
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
        startActivityForResult(intent, File_REQUEST_CODE)
    }



    private fun colorselect(){
        val intent = Intent(this, ColoerSelectActivity ::class.java)
        startActivity(intent)
    }

    companion object {
        const val File_REQUEST_CODE = 0
        const val Color_REQUEST_CODE = 1
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == File_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {//讀取回傳的檔名
                val message:String = data!!.getStringExtra("filename")
                val path = getExternalFilesDir(null)
                val dir = File(path,"Painter")
                val file = File(dir,message)

                val stream = file.inputStream()
                paintboard.setBitmap( BitmapFactory.decodeStream(stream))
                stream.close()

            }
        }
        else if(requestCode == Color_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                Toast.makeText(this,"select",Toast.LENGTH_SHORT).show()
            }
        }
        else {
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
                stream.close()


                Toast.makeText(this, "Save Success" + file.path, Toast.LENGTH_SHORT).show()

            } catch(e:Exception) {

                Log.i("Terror",e.toString())
                Toast.makeText(this, "Save Failed$e", Toast.LENGTH_SHORT).show()

            }
        }


    }

}


