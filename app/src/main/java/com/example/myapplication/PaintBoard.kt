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
    val m_width = Resources.getSystem().displayMetrics.widthPixels
    val m_height = Resources.getSystem().displayMetrics.heightPixels
    init {
        bmap = Bitmap.createBitmap(500,500, Bitmap.Config.ARGB_8888)
        bmap.eraseColor(Color.BLUE)
        pos_x=(m_width-500)/2
        pos_y=(m_height-500)/2-500
        base = Bitmap.createBitmap(bmap)
        canvas = Canvas(bmap)
        painter = Paint()
        painter.setColor(Color.GRAY)
        painter.setStrokeWidth(10f)
        list = mutableListOf()
        list.add(Bitmap.createBitmap(base))
        o_w=bmap.width
        o_h=bmap.height

        zoom_array_w = intArrayOf((o_w*0.3).toInt(),(o_w*0.4).toInt(),(o_w*0.5).toInt(),(o_w*0.75).toInt(),o_w,(o_w*1.25).toInt(),(o_w*1.5).toInt(),(o_w*1.75).toInt(),o_w*2)

        zoom_array_h = intArrayOf((o_h*0.3).toInt(),(o_h*0.4).toInt(),(o_h*0.5).toInt(),(o_h*0.75).toInt(),o_h,(o_h*1.25).toInt(),(o_h*1.5).toInt(),(o_h*1.75).toInt(),o_h*2)

        zoom_paint= floatArrayOf(0.3F,0.4F,0.5F,0.75F,1.0F,1.25F,1.5F,1.75F,2.0F)

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

            canvas = Canvas(bmap)
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

            canvas = Canvas(bmap)
            painter.setStrokeWidth(paint_width*zoom_paint[now_paint])
            invalidate()
        }
    }
    public  fun setpainterwidh(width:Float){
        painter.strokeWidth = width*zoom_paint[now_paint]
        paint_width=width
    }

    public fun ResetBitmapset(){
        index = 1
        list = mutableListOf(base)

    }

    public fun setpaintColor(color:Int){
        painter.color = color
    }

    public  fun setbase(nbase:Bitmap){
        move_x = 0
        move_y = 0
        pos_x = (m_width-500)/2
        pos_y = (m_height-bmap.height)/2-500+move_y
        now_zoom = 4
        now_paint = 4
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
    fun move_canvas(m_mode:Int){
        when(m_mode){
            1 -> {
                if(pos_x+move_x+100<m_width) {
                    move_x += 100
                    canvas = Canvas(bmap)
                    invalidate()
                }
            }
            2 -> {
                if(pos_x+move_x+bmap.width-100>0) {
                    move_x -= 100
                    canvas = Canvas(bmap)
                    invalidate()
                }
            }
            3 -> {
                if(pos_y+bmap.height+move_y-100>0) {
                    move_y -= 100
                    canvas = Canvas(bmap)
                    invalidate()
                }
            }
            4 -> {
                if(pos_y+100+move_y<m_height-800) {
                    move_y += 100
                    canvas = Canvas(bmap)
                    invalidate()
                }
            }
        }

    }
}