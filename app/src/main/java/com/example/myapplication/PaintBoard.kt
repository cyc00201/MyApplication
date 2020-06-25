package com.example.myapplication

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import kotlin.math.atan
import android.graphics.drawable.BitmapDrawable as BitmapDrawable1

class PaintBoard(context:Context,attribute:AttributeSet) :ImageView(context,attribute){

    private var painter:Paint
    private var bmap :Bitmap
    private var ncanvas:Canvas
    private var startX:Float = 0f
    private var startY:Float = 0f
    var o_w:Int
    var o_h:Int
    var zoom_array_w:IntArray
    var zoom_array_h:IntArray
    var zoom_paint:FloatArray
    var now_zoom:Int=4
    var now_paint:Int=4
    var paint_width:Float
    var pos_x:Int
    var pos_y:Int
    var move_x:Int=0
    var move_y:Int=0
    val m_width= Resources.getSystem().displayMetrics.widthPixels
    val m_height=Resources.getSystem().displayMetrics.heightPixels

    init {
        //m_width = Resources.getSystem().displayMetrics.widthPixels
        //m_height = Resources.getSystem().displayMetrics.heightPixels

        bmap = Bitmap.createBitmap(500,500, Bitmap.Config.ARGB_8888)
        pos_x=(m_width-500)/2
        pos_y=(m_height-500)/2-500

        o_w=bmap.width
        o_h=bmap.height

        zoom_array_w = intArrayOf((o_w*0.3).toInt(),(o_w*0.4).toInt(),(o_w*0.5).toInt(),(o_w*0.75).toInt(),o_w,(o_w*1.25).toInt(),(o_w*1.5).toInt(),(o_w*1.75).toInt(),o_w*2)

        zoom_array_h = intArrayOf((o_h*0.3).toInt(),(o_h*0.4).toInt(),(o_h*0.5).toInt(),(o_h*0.75).toInt(),o_h,(o_h*1.25).toInt(),(o_h*1.5).toInt(),(o_h*1.75).toInt(),o_h*2)

        zoom_paint= floatArrayOf(0.3F,0.4F,0.5F,0.75F,1.0F,1.25F,1.5F,1.75F,2.0F)

        ncanvas = Canvas(bmap)
        ncanvas.drawColor(Color.BLUE)
        painter = Paint()
        painter.setColor(Color.GRAY)
        painter.setStrokeWidth(10f)
        paint_width=painter.strokeWidth
        scaleType = ScaleType.MATRIX
    }

    fun setZoomIn(){
        if(now_zoom<8) {
            now_zoom += 1
            now_paint += 1
            bmap = Bitmap.createScaledBitmap(
                bmap,
                zoom_array_w[now_zoom],
                zoom_array_h[now_zoom],
                true
            )
            pos_x = (m_width-bmap.width)/2+move_x
            pos_y = (m_height-bmap.height)/2-500+move_y

            ncanvas = Canvas(bmap)
            painter.setStrokeWidth(paint_width*zoom_paint[now_paint])
            invalidate()
        }
    }

    fun setZoomOut() {
        if(now_zoom>0) {
            now_zoom-=1
            now_paint-=1
            bmap = Bitmap.createScaledBitmap(bmap, zoom_array_w[now_zoom],
                zoom_array_h[now_zoom], true)
            pos_x = (m_width-bmap.width)/2+move_x
            pos_y = (m_height-bmap.height)/2-500+move_y

            ncanvas = Canvas(bmap)
            painter.setStrokeWidth(paint_width*zoom_paint[now_paint])
            invalidate()
        }
    }

    public  fun setPainterWidh(width:Float){
        painter.strokeWidth = width*zoom_paint[now_paint]
        paint_width=width

    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(bmap,(pos_x+move_x).toFloat(),(pos_y+move_y).toFloat(),painter)

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

                        ncanvas.drawLine(startX-pos_x-move_x, startY-pos_y-move_y, stopX-pos_x-move_x, stopY-pos_y-move_y, painter)
                        startX = event.x
                        startY = event.y
                        // call onDraw
                        invalidate()
                    }
                }
            }
            return true
    }

    fun move_canvas(m_mode:Int){
        when(m_mode){
            1 -> {
                if(pos_x+move_x+100<m_width) {
                    move_x += 100
                    ncanvas = Canvas(bmap)
                    invalidate()
                }
            }
            2 -> {
                if(pos_x+move_x+bmap.width-100>0) {
                    move_x -= 100
                    ncanvas = Canvas(bmap)
                    invalidate()
                }
            }
            3 -> {
                if(pos_y+bmap.height+move_y-100>0) {
                    move_y -= 100
                    ncanvas = Canvas(bmap)
                    invalidate()
                }
            }
            4 -> {
                if(pos_y+100+move_y<m_height-800) {
                    move_y += 100
                    ncanvas = Canvas(bmap)
                    invalidate()
                }
            }
        }

    }

}