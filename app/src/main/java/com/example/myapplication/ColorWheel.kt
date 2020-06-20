package com.example.myapplication

import android.app.Activity
import android.app.usage.UsageEvents
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.graphics.toColor
import java.lang.Math.*


class ColorWheel(context: Context,attributes: AttributeSet): androidx.appcompat.widget.AppCompatImageView(context,attributes) {
    private  var bmap :Bitmap

    private  var brightness = 1.0f

    init {
        val bwidth =  Resources.getSystem().displayMetrics.widthPixels
        val bheight = Resources.getSystem().displayMetrics.heightPixels

        bmap = Bitmap.createBitmap(bwidth,bheight, Bitmap.Config.ARGB_8888)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        colorWheel()
        canvas?.drawBitmap(bmap,0f,-100f,Paint())


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {

                    val intent = Intent().apply {
                        putExtra("pixel",getPixel(event.x.toInt(),event.y.toInt()))
                        // Put your data here if you want.
                    }
                    (context as ColoerSelectActivity).setResult(Activity.RESULT_OK, intent)
                    (context as ColoerSelectActivity).finish()
                }
            }
        }
        return super.onTouchEvent(event)
    }


   public fun setbrightness(value:Float){
        brightness = value
       colorWheel()
       draw(Canvas(bmap))
    }

    private fun setPixel(x: Int, y: Int,hsv:FloatArray) {bmap.setPixel(x,y,Color.HSVToColor(hsv))}

    public fun getPixel(x: Int, y: Int):Int { return bmap.getPixel(x,y)}

    private fun colorWheel() {

        bmap.eraseColor(Color.WHITE)
        val centerX = bmap.width / 2
        val centerY = bmap.height / 2
        val radius = minOf(centerX, centerY)
        for (y in 0 until bmap.height) {
            val dy = (y - centerY).toDouble()
            for (x in 0 until bmap.width) {
                val dx = (x - centerX).toDouble()
                val dist = kotlin.math.sqrt(dx * dx + dy * dy)
                if (dist <= radius) {
                    val theta = kotlin.math.atan2(dy, dx)
                    val hue = toDegrees(theta + PI)

                    var hsv = FloatArray(3)
                    hsv[0] = hue.toFloat()
                    hsv[1] = (dist/radius).toFloat()
                    hsv[2] = brightness

                    setPixel(x, y,hsv)
                }
            }
        }
    }
}