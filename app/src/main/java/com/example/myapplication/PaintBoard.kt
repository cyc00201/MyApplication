package com.example.myapplication

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import java.io.File
import java.io.InputStream
import java.net.URL


class PaintBoard(context:Context,attribute:AttributeSet) : androidx.appcompat.widget.AppCompatImageView(context,attribute){

    private var painter:Paint
    private var bmap :Bitmap
    private var ncanvas:Canvas
    private var startX:Float = 0f
    private var startY:Float = 0f

    init {

        val width = Resources.getSystem().displayMetrics.widthPixels
        val height = Resources.getSystem().displayMetrics.heightPixels
        bmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888)

        ncanvas = Canvas(bmap)
        ncanvas.drawColor(Color.BLUE)
        painter = Paint()
        painter.setColor(Color.GRAY)
        painter.setStrokeWidth(10f)

    }

    public  fun setpainterwidh(width:Float){
        painter.setStrokeWidth(width)

    }

    public fun getBitmap():Bitmap{
        return bmap
    }
    public fun setBitmap(bmp: Bitmap){

        try {

           ncanvas.drawBitmap(bmp,0f,0f,painter)
        } catch (e: Exception) {
            Log.e("Terror", e.message)
            Toast.makeText(context,e.message,Toast.LENGTH_LONG).show()
        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            canvas.drawBitmap(bmap,0f,0f,painter)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    startY = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val stopX = event.x
                    val stopY = event.y

                    ncanvas.drawLine(startX, startY, stopX, stopY, painter)
                    startX = event.x
                    startY = event.y

                    // call onDraw
                    invalidate()
                }
            }
        }

        return true
    }
}