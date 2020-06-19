package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import java.io.File

class Adapter(private val context: Activity, private val IdList: MutableList<String>)
    : BaseAdapter()  {

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list,null)
        val button = rowView.findViewById<Button>(R.id.test)
        button.setText( IdList[p0])
        button.setOnClickListener {click(button)}
      //  val imageView = rowView.findViewById<ImageView>(R.id.image_item)
      //  imageView.setImageResource(IdList[p0])
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

    private fun click(button:Button){
        (context as loaddata).fileisselect = 1
      (context as loaddata).filename = button.text.toString()
        //Toast.makeText(context,button.text,Toast.LENGTH_SHORT).show()

       context.finish()

    }

}