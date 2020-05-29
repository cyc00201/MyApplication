package com.example.myapplication

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.jar.Attributes

class PaintBoard(context:Context,attribute:AttributeSet) :View(context,attribute){

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

    public  fun set_painter(npainter: Paint){
        painter = npainter
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