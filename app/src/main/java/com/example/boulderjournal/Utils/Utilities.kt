package com.example.boulderjournal.Utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.boulderjournal.R

class Utilities {
    private val mContext: Context? = null

    companion object {
        fun getColor(color: String?, context: Context?): Int {
            val newColor = color?.toLowerCase()
            val colourInt: Int
            colourInt = if (newColor == "pink") {
                ContextCompat.getColor(context!!, R.color.routePink)
            } else if (newColor == "blue") {
                ContextCompat.getColor(context!!, R.color.routeBlue)
            } else if (newColor == "orange") {
                ContextCompat.getColor(context!!, R.color.routeOrange)
            } else if (newColor == "yellow") {
                ContextCompat.getColor(context!!, R.color.routeYellow)
            } else {
                ContextCompat.getColor(context!!, R.color.colorPrimaryDark)
            }
            return colourInt
        }
    }
}

fun hideKeyboard(activity: FragmentActivity) {
    val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    // Check if no view has focus
    val currentFocusedView = activity.currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
                currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}