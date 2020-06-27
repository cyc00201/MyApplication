package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.annotation.ColorInt

class LayerManager(initLayers: Int = 1) {
    private val layers: ArrayList<Bitmap> = ArrayList()
    var width: Int = 500
        private set
    var height: Int = 500
        private set
    var baseBmp: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        private set
    private var current: Int
    private var isBasedOnFile: Boolean = false
    private val MAX_LAYERS: Int = 10

    init {
        baseBmp.eraseColor(Color.BLUE)
        for (i in 0 until initLayers) {
            layers.add(newLayer())
        }

        current = 0;
    }

    private fun newLayer(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT)
        return bitmap
    }

    fun setBackgroundColor(@ColorInt color: Int) {
        if (isBasedOnFile) return /* Avoid to erase the opened image */
        baseBmp.eraseColor(color)
    }

    fun setBaseImage(bmp: Bitmap) {
        this.baseBmp = bmp
        layers.clear()
        layers.add(newLayer())
        current = 0
        isBasedOnFile = true
    }

    fun chooseLayer(index: Int) {
        this.current = index
    }

    fun setCurrentLayer(bmp: Bitmap) {
        this.layers[current] = Bitmap.createBitmap(bmp)
    }

    fun currentLayer(): Bitmap {
        return this.layers[current]
    }

    fun getMergedBitmap(): Bitmap {
        val canvas = Canvas(baseBmp)

        for (i in 0 until layers.size) {
            canvas.drawBitmap(layers[i], 0f, 0f, null)
        }
        return baseBmp
    }

    fun setDimensions(dstWidth: Int, dstHeight: Int) {
        this.width = dstWidth
        this.height = dstHeight
        this.baseBmp = Bitmap.createScaledBitmap(baseBmp, dstWidth, dstHeight, true)
        for (i in 0 until layers.size) {
            layers[i] = Bitmap.createScaledBitmap(layers[i], dstWidth, dstHeight, true)
        }
    }
}