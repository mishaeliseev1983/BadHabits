package com.melyseev.badhabits

import android.view.View
import android.widget.Button
import android.widget.TextView

sealed class UIState {

    abstract fun apply(textView: TextView, resetButton: Button)

    object ZeroDays: UIState(){
        override fun apply(textView: TextView, resetButton: Button) {
            textView.text = "0"
            resetButton.visibility = View.GONE
        }
    }

    data class NDays(val days: Int): UIState(){
        override fun apply(textView: TextView, resetButton: Button) {
            textView.text = days.toString()
            resetButton.visibility = View.VISIBLE
        }

    }
}