package com.example.myapplication

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class PaintBoard(context: Context, attribute: AttributeSet) :
    androidx.appcompat.widget.AppCompatImageView(context, attribute) {

    private var painter: Paint
    private var canvas: Canvas
    private var startX: Float = 0f
    private var startY: Float = 0f
    private var layers: LayerManager

    private val mWidth = Resources.getSystem().displayMetrics.widthPixels
    private val mHeight = Resources.getSystem().displayMetrics.heightPixels
    private var offsetX: Float = 0f
    private var offsetY: Float = 0f
    private var scaleFactor: Float = 1f
    private var scaleDetector = ScaleGestureDetector(context, ScaleGestureListener())
    private var moveRate = 50.0f

    init {
        layers = LayerManager(5)
        canvas = Canvas(layers.current.bitmap)

        offsetX = (mWidth - canvas.width) * 0.5f
        offsetY = (mHeight - canvas.height) * 0.5f - 500f // TODO: How to get the center at init???

        painter = Paint()
        painter.color = Color.GRAY
        painter.strokeWidth = 10f


        scaleType = ScaleType.MATRIX
    }

    inner class ScaleGestureListener : ScaleGestureDetector.OnScaleGestureListener {
        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor

            invalidate()
            return true
        }
    }

    fun zoomIn() {
        scaleFactor *= 1.1f
        invalidate()
    }

    fun zoomOut() {
        scaleFactor *= 0.9f
        invalidate()
    }

    fun setPainterWidth(width: Float) {
        painter.strokeWidth = width * scaleFactor
    }

    fun chooseLayer(index: Int) {
        layers.chooseLayer(index)
        setBitmap(layers.current.bitmap)
    }

    fun setPainterColor(color: Int) {
        painter.color = color
    }

    fun centerCanvas() {
        offsetX = (this.width - canvas.width) * 0.5f
        offsetY = (this.height - canvas.height) * 0.5f
    }

    fun setBaseImage(bmp: Bitmap) {
        layers.setBaseImage(Bitmap.createBitmap(bmp))
        centerCanvas()

        setBitmap(layers.current.bitmap)
    }

    fun undo(value: Int) {
        layers.current.undo(value)
        setBitmap(layers.current.bitmap)
    }

    fun redo(value: Int) {
        layers.current.redo(value)
        setBitmap(layers.current.bitmap)
    }

    fun setBitmap(bmp: Bitmap) {
        try {
            canvas.setBitmap(bmp)
            canvas.drawBitmap(bmp, 0f, 0f, painter)
            invalidate()
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun getBitmap(): Bitmap {
        return layers.getMergedBitmap()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.scale(scaleFactor, scaleFactor)
        canvas.translate(offsetX, offsetY)
        canvas.drawBitmap(
            layers.background,
            0f, 0f,
            painter
        )
        for (i in 0 until (context as MainActivity).layer_spinner.selectedItemPosition) {
            layers.chooseLayer(i)
            canvas.drawBitmap(
                layers.current.bitmap,
                0f, 0f,
                painter
            )
        }
        layers.chooseLayer((context as MainActivity).layer_spinner.selectedItemPosition)
        canvas.drawBitmap(
            layers.current.bitmap,
            0f, 0f,
            painter
        )
        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x: Float = (event.x / scaleFactor) - offsetX
        val y: Float = (event.y / scaleFactor) - offsetY

        if (event.pointerCount >= 2) {
            scaleDetector.onTouchEvent(event)
            return true
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = x
                startY = y
            }
            MotionEvent.ACTION_MOVE -> {
                canvas.drawLine(
                    startX,
                    startY,
                    x,
                    y,
                    painter
                )
                startX = x
                startY = y
                invalidate()
            }
            MotionEvent.ACTION_UP -> { //存步驟
                layers.current.updateHistory()
            }
        }
        return true
    }

    fun moveCanvas(m_mode: Int) {
        when (m_mode) {
            1 -> {
                offsetX += moveRate
                invalidate()
            }
            2 -> {
                offsetX += -moveRate
                invalidate()
            }
            3 -> {
                offsetY += -moveRate
                invalidate()
            }
            4 -> {
                offsetY += moveRate
                invalidate()
            }
        }
    }
}