package com.example.myapplication

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.media.MediaMetadataRetriever
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
    private var base :Bitmap
    private  var index = 1
    private var list:MutableList<Bitmap>
    private  var canvas:Canvas
    private var startX:Float = 0f
    private var startY:Float = 0f
    init {

        val width = Resources.getSystem().displayMetrics.widthPixels
        val  height = Resources.getSystem().displayMetrics.heightPixels

        bmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888)
        bmap.eraseColor(Color.BLUE)
        base = Bitmap.createBitmap(bmap)
        canvas = Canvas(bmap)
        painter = Paint()
        painter.setColor(Color.GRAY)
        painter.setStrokeWidth(10f)
        list = mutableListOf()
        list.add(Bitmap.createBitmap(base))

    }

    public  fun setpainterwidh(width:Float){
        painter.setStrokeWidth(width)
    }

    public fun ResetBitmapset(){
        index = 1
        list = mutableListOf(base)

    }

    public fun setpaintColor(color:Int){
        painter.color = color
    }

    public  fun setbase(nbase:Bitmap){
        base = Bitmap.createBitmap(nbase)
    }

    public fun unredo(value:Int){
        //復原ＯＲ重做
        if(index+value <=0 || index+value>list.size ) {
            return
        }
        index+=value
        setBitmap(Bitmap.createBitmap(list[index-1]))
    }

    public fun getBitmap():Bitmap{
        return bmap
    }

    public fun setBitmap(bmp: Bitmap){
        try {
            canvas?.drawBitmap(bmp,0f,0f,painter)

        }
        catch (e: Exception) {
            Toast.makeText(context,e.message,Toast.LENGTH_LONG).show()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(bmap,0f,0f,painter)
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

                    canvas.drawLine(startX, startY, stopX, stopY, painter)
                    startX = event.x
                    startY = event.y

                    invalidate()
                }

                MotionEvent.ACTION_UP ->{ //存步驟
                    if(index<list.size){
                        list = list.dropLast(list.size - index) as MutableList<Bitmap>
                        if(list.size == 1){
                            list = mutableListOf(base)
                        }
                    }
                    index++
                    list?.add(Bitmap.createBitmap(bmap))

                }
            }

        }

        return true
    }
}