package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.annotation.ColorInt

class LayerManager(val width: Int, val height: Int, initLayers: Int = 1) {
    private val layers: ArrayList<Bitmap> = ArrayList()
    var baseBmp: Bitmap = newTransparentLayer()
        set(baseBmp: Bitmap) {
            field = baseBmp
            layers.clear()
            isBasedOnFile = true
        }
    private var currentLayer: Int
    private var isBasedOnFile: Boolean = false
    private val MAX_LAYERS: Int = 10

    init {
        for (i in 0 until initLayers) {
            layers.add(newTransparentLayer())
        }

        currentLayer = 0;
    }

    private fun newTransparentLayer(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT)
        return bitmap
    }

    fun setBackgroundColor(@ColorInt color: Int) {
        if (isBasedOnFile) return /* Avoid to erase the opened image */
        baseBmp.eraseColor(color)
    }

    fun setCurrentLayer(index: Int) {
        this.currentLayer = index
    }

    fun replaceCurrentLayer(bmp: Bitmap) {
        this.layers[currentLayer] = bmp
    }

    fun currentLayer(): Bitmap {
        return this.layers[currentLayer]
    }

    fun setBackground(bitmap: Bitmap) {
        baseBmp = bitmap
        layers.clear()
        layers.add(newTransparentLayer())
    }

    fun getMergedBitmap(): Bitmap {
        val canvas = Canvas(baseBmp)

        for (i in 0 until layers.size) {
            canvas.drawBitmap(layers[i], 0f, 0f, null)
        }
        return baseBmp
    }
}