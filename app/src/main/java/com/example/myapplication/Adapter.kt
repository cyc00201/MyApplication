package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.drawToBitmap
import java.io.File

class Adapter(private val context: Activity, private val IdList: MutableList<String>)
    : BaseAdapter()  {

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list,null)
        val path = context.getExternalFilesDir(null)
        val dir = File(path,"Painter")
        val file = File(dir,IdList[p0])
        val stream = file.inputStream()
        val bitmap:Bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(stream),
            300,
            300,
            true)
         val button = rowView.findViewById<Button>(R.id.listbutton)
         button.background = bitmap.toDrawable(rowView.resources)
         val text = rowView.findViewById<TextView>(R.id.listtext)
         text.text = IdList[p0]
         button.setOnClickListener {click(button,text)}
        return rowView
    }

    override fun getItem(p0: Int): Any {
        return IdList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return IdList.size
    }

    private fun click(button:Button,text:TextView){
        (context as loaddata).fileisselect = 1
         context.filename = text.text.toString()
        //Toast.makeText(context,button.text,Toast.LENGTH_SHORT).show()
       context.finish()

    }

}