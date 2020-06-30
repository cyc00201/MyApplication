package com.example.myapplication

import android.graphics.*
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
    public var isBasedOnFile: Boolean = false
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



    fun setBaseImage(bmp: Bitmap) {
        background.eraseColor(Color.BLUE)
        layerList.clear()
        layerList.add(Layer(bmp.copy(Bitmap.Config.ARGB_8888,true)))
        for (i in 1 until layerNum) {
            layerList.add(newLayer())
        }
        current = layerList[0]
        isBasedOnFile = true
    }

    fun chooseLayer(index: Int) {
        current = layerList[index]
    }

    fun MergedBitmap() {
        isBasedOnFile = true
        val canvas = Canvas(background)
        var painter = Paint()
        painter.setXfermode(
            PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP))
        for (i in 0 until layerList.size) {
            canvas.drawBitmap(layerList[i].bitmap, 0f, 0f, painter)
        }
    }


}