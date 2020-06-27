package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.annotation.ColorInt

class LayerManager(initLayers: Int = 1) {
    private val layerList: ArrayList<Bitmap> = ArrayList()
    var width: Int = 500
        private set
    var height: Int = 500
        private set
    var baseBmp: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        private set
    private var current: Int
    private var layerNum: Int = initLayers
    private var isBasedOnFile: Boolean = false
    private val MAX_LAYERS: Int = 10

    init {
        baseBmp.eraseColor(Color.BLUE)
        for (i in 0 until layerNum) {
            layerList.add(newLayer())
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
        layerList.clear()
        for (i in 0 until layerNum) {
            layerList.add(newLayer())
        }
        current = 0
        isBasedOnFile = true
    }

    fun chooseLayer(index: Int) {
        this.current = index
    }

    fun setCurrentLayer(bmp: Bitmap) {
        this.layerList[current] = Bitmap.createBitmap(bmp)
    }

    fun currentLayer(): Bitmap {
        return this.layerList[current]
    }

    fun getMergedBitmap(): Bitmap {
        val canvas = Canvas(baseBmp)

        for (i in 0 until layerList.size) {
            canvas.drawBitmap(layerList[i], 0f, 0f, null)
        }
        return baseBmp
    }

    fun setDimensions(dstWidth: Int, dstHeight: Int) {
        this.width = dstWidth
        this.height = dstHeight
        this.baseBmp = Bitmap.createScaledBitmap(baseBmp, dstWidth, dstHeight, true)
        for (i in 0 until layerList.size) {
            layerList[i] = Bitmap.createScaledBitmap(layerList[i], dstWidth, dstHeight, true)
        }
    }
}