package com.example.myapplication

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import java.util.*

class Layer(bmp: Bitmap) {

    private val undoStack: Stack<Bitmap> = Stack()
    private val redoStack: Stack<Bitmap> = Stack()

    var bitmap: Bitmap = bmp
        private set

    init {
        undoStack.push(Bitmap.createBitmap(bitmap))
    }

    fun undo(value: Int) {
        // There should be at least one history image to be the current bitmap
     Log.i("Terror",undoStack.size.toString())
        if (value <= 0 || undoStack.size - value < 1)
            return
        for (i in 0 until value) {
            redoStack.push(undoStack.pop())
        }
        bitmap = Bitmap.createBitmap(undoStack.peek())
    }

    fun redo(value: Int) {
        if (value <= 0 || redoStack.size - value < 0)
            return
        for (i in 0 until value) {
            undoStack.push(redoStack.pop())
        }
        bitmap = Bitmap.createBitmap(undoStack.peek())
    }

    fun updateHistory() {
        undoStack.push(Bitmap.createBitmap(bitmap))
        redoStack.clear()
    }
}