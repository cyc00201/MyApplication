package com.example.myapplication

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import java.util.Stack


class PaintBoard(context: Context, attribute: AttributeSet) :
    androidx.appcompat.widget.AppCompatImageView(context, attribute) {

    private var painter: Paint
    private var canvas: Canvas
    private var startX: Float = 0f
    private var startY: Float = 0f
    private var history: Stack<Bitmap>
    private var redoStack: Stack<Bitmap>
    private var layers: LayerManager


    var o_w: Int
    var o_h: Int
    var zoom_array_w: IntArray
    var zoom_array_h: IntArray
    var zoom_paint: FloatArray
    var now_zoom: Int = 4
    var now_paint: Int = 4
    var paint_width: Float
    var pos_x: Int
    var pos_y: Int
    var move_x: Int = 0
    var move_y: Int = 0
    val m_width = Resources.getSystem().displayMetrics.widthPixels
    val m_height = Resources.getSystem().displayMetrics.heightPixels

    init {
        layers = LayerManager(5)
        history = Stack()
        redoStack = Stack()
        history.push(Bitmap.createBitmap(layers.currentLayer()))

        pos_x = (m_width - 500) / 2
        pos_y = (m_height - 500) / 2 - 500
        canvas = Canvas(layers.currentLayer())

        painter = Paint()
        painter.setColor(Color.GRAY)
        painter.setStrokeWidth(10f)
        o_w = layers.currentLayer().width
        o_h = layers.currentLayer().height

        zoom_array_w = intArrayOf(
            (o_w * 0.3).toInt(),
            (o_w * 0.4).toInt(),
            (o_w * 0.5).toInt(),
            (o_w * 0.75).toInt(),
            o_w,
            (o_w * 1.25).toInt(),
            (o_w * 1.5).toInt(),
            (o_w * 1.75).toInt(),
            o_w * 2
        )

        zoom_array_h = intArrayOf(
            (o_h * 0.3).toInt(),
            (o_h * 0.4).toInt(),
            (o_h * 0.5).toInt(),
            (o_h * 0.75).toInt(),
            o_h,
            (o_h * 1.25).toInt(),
            (o_h * 1.5).toInt(),
            (o_h * 1.75).toInt(),
            o_h * 2
        )

        zoom_paint = floatArrayOf(0.3F, 0.4F, 0.5F, 0.75F, 1.0F, 1.25F, 1.5F, 1.75F, 2.0F)

        paint_width = painter.strokeWidth
        scaleType = ScaleType.MATRIX
    }

    fun setZoomIn() {
        if (now_zoom < 8) {
            now_zoom += 1
            now_paint += 1
            layers.setDimensions(zoom_array_w[now_zoom], zoom_array_h[now_zoom])
            if (pos_x + move_x + layers.width > m_width) {
                move_x = m_width - pos_x - layers.width
            }

            if (pos_y + move_y + layers.height > m_height - 744) {
                move_y = m_height - 744 - pos_y - layers.height
            }
            // pos_x = (m_width-bmap.width)/2+move_x
            // pos_y = (m_height-bmap.height)/2-500+move_y

            painter.setStrokeWidth(paint_width * zoom_paint[now_paint])
            setBitmap(layers.currentLayer())
        }
    }

    fun setZoomOut() {
        if (now_zoom > 0) {
            now_zoom -= 1
            now_paint -= 1
            layers.setDimensions(zoom_array_w[now_zoom], zoom_array_h[now_zoom])
            // pos_x = (m_width - layers.width) / 2 + move_x
            // pos_y = (m_height - layers.height) / 2 - 500 + move_y

            painter.setStrokeWidth(paint_width * zoom_paint[now_paint])
            setBitmap(layers.currentLayer())
        }
    }

    fun setpainterwidh(width: Float) {
        painter.strokeWidth = width * zoom_paint[now_paint]
        paint_width = width
    }

    fun chooseLayer(index: Int) {
        layers.chooseLayer(index)
        setBitmap(layers.currentLayer())
    }

    fun setpaintColor(color: Int) {
        painter.color = color
    }

    fun setbase(nbase: Bitmap) {
        move_x = 0
        move_y = 0
        pos_x = (m_width - 500) / 2
        pos_y = (m_height - layers.height) / 2 - 500 + move_y
        now_zoom = 4
        now_paint = 4
        layers.setBaseImage(Bitmap.createBitmap(nbase))

        // TODO: New current layer should be a new transparent bitmap
        layers.setCurrentLayer(Bitmap.createBitmap(nbase))

        resetUndoList()
        setBitmap(layers.currentLayer())
    }

    fun resetUndoList() {
        history.clear()
        history.add(Bitmap.createBitmap(layers.currentLayer()))
        redoStack.clear()
    }

    fun undo(value: Int) {
        // There should be at least one history image to be the current bitmap
        if (value <= 0 || history.size - value < 1)
            return
        for (i in 0 until value) {
            redoStack.push(history.pop())
        }
        layers.setCurrentLayer(Bitmap.createBitmap(history.peek()))
        setBitmap(layers.currentLayer())
    }

    fun redo(value: Int) {
        if (value <= 0 || redoStack.size - value < 0)
            return
        for (i in 0 until value) {
            history.push(redoStack.pop())
        }
        layers.setCurrentLayer(Bitmap.createBitmap(history.peek()))
        setBitmap(layers.currentLayer())
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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(
            layers.currentLayer(),
            (pos_x + move_x).toFloat(),
            (pos_y + move_y).toFloat(),
            painter
        )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    startY = event.y
                }
                MotionEvent.ACTION_MOVE -> {

                    val stopX = event.x
                    val stopY = event.y

                    canvas.drawLine(
                        startX - pos_x - move_x,
                        startY - pos_y - move_y,
                        stopX - pos_x - move_x,
                        stopY - pos_y - move_y,
                        painter
                    )
                    startX = event.x
                    startY = event.y

                    invalidate()
                }

                MotionEvent.ACTION_UP -> { //存步驟
                    redoStack.clear()
                    history.push(Bitmap.createBitmap(layers.currentLayer()))
                }
            }

        }

        return true
    }

    fun move_canvas(m_mode: Int) {
        when (m_mode) {
            1 -> {
                if (pos_x + layers.width + move_x + 100 < m_width) {
                    move_x += 100.toInt()
                } else {
                    move_x += m_width - (pos_x + layers.width + move_x)
                }
                canvas = Canvas(layers.currentLayer())
                invalidate()
            }
            2 -> {
                if (pos_x + move_x - 100 > 0) {
                    move_x -= 100
                } else {
                    move_x -= (pos_x + move_x)
                }
                canvas = Canvas(layers.currentLayer())
                invalidate()
            }
            3 -> {
                if (pos_y + move_y - 100 > 0) {
                    move_y -= 100
                } else {
                    move_y -= (pos_y + move_y)
                }
                canvas = Canvas(layers.currentLayer())
                invalidate()
            }
            4 -> {
                if (pos_y + layers.height + 100 + move_y < m_height - 744) {
                    move_y += 100
                } else {
                    move_y += m_height - 744 - (pos_y + layers.height + move_y)
                }
                canvas = Canvas(layers.currentLayer())
                invalidate()
            }
        }

        Log.i("Terror", (pos_x + move_x).toString() + " " + (pos_y + move_y).toString())
    }
}