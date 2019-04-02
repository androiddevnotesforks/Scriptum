package sgtmelon.scriptum.office.utils

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.app.model.data.IntroData
import sgtmelon.scriptum.databinding.IncludeInfoBinding

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

    fun IncludeInfoBinding.bind(page: Int) {
        icon = IntroData.icon[page]
        title = IntroData.title[page]
        details = IntroData.details[page]

        executePendingBindings()
    }

    fun EditText?.clear(): String {
        if (this == null) return ""

        val text = getClearText()
        setText("")
        return text
    }

    fun EditText?.getClearText(): String {
        if (this == null) return ""

        return text.toString().trim().replace("\\s+".toRegex()," ")
    }

    fun <T : ViewDataBinding> LayoutInflater.inflateBinding(layoutId: Int, parent: ViewGroup?,
                                                            attachToParent: Boolean = false): T {
        return DataBindingUtil.inflate(this, layoutId, parent, attachToParent)
    }

    fun RecyclerView.ViewHolder.checkNoPosition(function: () -> Unit): Boolean {
        if (adapterPosition == RecyclerView.NO_POSITION) return false

        function.invoke()
        return true
    }

    fun Context.getDimen(value: Float) = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics
    ).toInt()

    fun Context.showToast(@StringRes stringId: Int, length: Int = Toast.LENGTH_SHORT) =
            Toast.makeText(this, getText(stringId), length).show()

    fun <T> MutableList<T>.swap(from: Int, to: Int) {
        val item = get(from)
        removeAt(from)
        add(to, item)
    }

    fun <E> MutableCollection<E>.clearAndAdd(replace: MutableList<E>) {
        clear()
        addAll(replace)
    }

}