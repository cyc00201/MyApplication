package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.annotation.ColorInt

class LayerManager(initLayers: Int = 1) {
    private val layerList: ArrayList<Layer> = ArrayList()
    var width: Int = 500
        private set
    var height: Int = 500
        private set
    var background: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        private set
    var current: Layer
        private set
    private var layerNum: Int = initLayers
    private var isBasedOnFile: Boolean = false
    private val MAX_LAYERS: Int = 10

    init {
        background.eraseColor(Color.BLUE)
        for (i in 0 until layerNum) {
            layerList.add(newLayer())
        }
        current = layerList[0];
    }

    private fun newLayer(): Layer {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT)
        return Layer(bitmap)
    }

    fun getlistbitmap(index:Int): Bitmap{
        return  layerList[index].bitmap

    }
    fun setBackgroundColor(@ColorInt color: Int) {
        if (isBasedOnFile) return /* Avoid to erase the opened image */
        background.eraseColor(color)
    }

    fun setBaseImage(bmp: Bitmap) {
        this.background = bmp
        layerList.clear()
        for (i in 0 until layerNum) {
            layerList.add(newLayer())
        }
        current = layerList[0]
        isBasedOnFile = true
    }

    fun chooseLayer(index: Int) {
        current = layerList[index]
    }

    fun getMergedBitmap(): Bitmap {
        val canvas = Canvas(background)

        for (i in 0 until layerList.size) {
            canvas.drawBitmap(layerList[i].bitmap, 0f, 0f, null)
        }
        return background
    }

    fun setCurrentLayer(bmp: Bitmap) {
        this.layerList[layerList.indexOf(current)] = Layer(Bitmap.createBitmap(bmp))
    }

    fun setDimensions(dstWidth: Int, dstHeight: Int) {
        this.width = dstWidth
        this.height = dstHeight
        this.background = Bitmap.createScaledBitmap(background, dstWidth, dstHeight, true)
        for (i in 0 until layerList.size) {
            layerList[i] =
                Layer(Bitmap.createScaledBitmap(layerList[i].bitmap, dstWidth, dstHeight, true))
        }
    }
}