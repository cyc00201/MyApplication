package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream

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
        var zoomInButton: Button =findViewById(R.id.zoomInButton)
        var zoomOutButton: Button =findViewById(R.id.zoomOutButton)
        var btn_mover:Button=findViewById(R.id.move_r)
        var btn_movel:Button=findViewById(R.id.move_l)
        var btn_movet:Button=findViewById(R.id.move_t)
        var btn_moveb:Button=findViewById(R.id.move_b)
        zoomInButton.setOnClickListener{paintboard.setZoomIn()}
        zoomOutButton.setOnClickListener { paintboard.setZoomOut() }
        btn_mover.setOnClickListener { paintboard.move_canvas(1) }
        btn_movel.setOnClickListener { paintboard.move_canvas(2) }
        btn_movet.setOnClickListener { paintboard.move_canvas(3) }
        btn_moveb.setOnClickListener { paintboard.move_canvas(4) }
    }

    override fun onResume() {
        super.onResume()

        savebutton.setOnClickListener { saveClickHandler ()}
        openfilebutton.setOnClickListener { to_openfile_interface() }
        redobutton.setOnClickListener { paintboard.redo(1) }
        undobutton.setOnClickListener { paintboard.undo(1)  }
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


    private fun to_openfile_interface(){
        val intent = Intent(this, loaddata::class.java)
        startActivityForResult(intent, File_REQUEST_CODE)
    }

    private  fun openfile(data:Intent){
        val message:String = data.getStringExtra("filename")
        val path = getExternalFilesDir(null)
        val dir = File(path,"Painter")
        val file = File(dir,message)
        val stream = file.inputStream()
        val bitmap:Bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(stream),
            paintboard.getBitmap().width,
            paintboard.getBitmap().height,
            true)
        paintboard.setbase(bitmap)
        stream.close()
    }

    private fun colorselect(){
        val intent = Intent(this, ColoerSelectActivity ::class.java)
        startActivityForResult(intent, Color_REQUEST_CODE)
    }

    companion object {
        const val File_REQUEST_CODE = 0
        const val Color_REQUEST_CODE = 1
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == File_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {//讀取回傳的檔名

                val alert_tosave  = AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("是否儲存現有圖片？")
                    .setPositiveButton("是"
                    ) { dialog, id ->
                        saveClickHandler()
                        openfile(data!!)
                        // FIRE ZE MISSILES!
                    }
                    .setNegativeButton("否")
                    { dialog, id ->
                        openfile(data!!)
                        // FIRE ZE MISSILES!
                    }
                    .create()
                alert_tosave.show()


            }
        }
        else if(requestCode == Color_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                //選色
                val color:Int= data!!.getIntExtra("pixel",Color.GRAY)
                colorbutton.setBackgroundColor(color)
                paintboard.setpaintColor(color)
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
                Bitmap.createScaledBitmap(paintboard.getBitmap(),
                    Resources.getSystem().displayMetrics.widthPixels,
                    Resources.getSystem().displayMetrics.widthPixels,
                true).
                compress(Bitmap.CompressFormat.JPEG, 100, stream)

                stream.close()


                Toast.makeText(this, "Save Success" + file.path, Toast.LENGTH_SHORT).show()

            } catch(e:Exception) {

                Log.i("Terror",e.toString())
                Toast.makeText(this, "Save Failed$e", Toast.LENGTH_SHORT).show()

            }
        }


    }

}






