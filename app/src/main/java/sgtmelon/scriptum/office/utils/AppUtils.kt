package sgtmelon.scriptum.office.utils

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import sgtmelon.scriptum.databinding.IncludeInfoBinding
import sgtmelon.scriptum.office.data.IntroData

object AppUtils {

    // TODO раскидать по разным Utils

    fun Activity.beforeFinish(function: () -> Unit) {
        function.invoke()
        finish()
    }

    fun Activity.hideKeyboard() {
        val view: View = currentFocus ?: return

        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun View.change(alpha: Float, scale: Float) {
        this.alpha = alpha
        this.scaleX = scale
        this.scaleY = scale
    }

    fun IncludeInfoBinding.bind(page: Int) {
        icon = IntroData.icon[page]
        title = IntroData.title[page]
        details = IntroData.details[page]

        executePendingBindings()
    }

    fun EditText?.clear(): String {
        if (this == null) return ""

        val text = text.toString()
        setText("")
        return text
    }

    fun FloatingActionButton.setState(state: Boolean) {
        isEnabled = state

        when (state) {
            true -> show()
            false -> hide()
        }
    }

}