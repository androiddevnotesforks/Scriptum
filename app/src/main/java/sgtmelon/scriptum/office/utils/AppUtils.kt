package sgtmelon.scriptum.office.utils

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.model.key.ReceiverKey

fun <T : ViewDataBinding> LayoutInflater.inflateBinding(@LayoutRes layoutId: Int, parent: ViewGroup?,
                                                        attachToParent: Boolean = false): T =
        DataBindingUtil.inflate(this, layoutId, parent, attachToParent)

fun <T : ViewDataBinding> ViewGroup.inflateBinding(@LayoutRes layoutId: Int,
                                                   attachToParent: Boolean = false): T =
        DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, this, attachToParent)

fun ViewGroup.inflateView(@LayoutRes layout: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(context).inflate(layout, this, attachToRoot)

fun Activity.beforeFinish(func: () -> Unit) {
    func()
    finish()
}

fun View.showKeyboard() {
    (context.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.showSoftInput(this, 0)
}

fun Activity.hideKeyboard() {
    (getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Context.showToast(@StringRes stringId: Int, length: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, getText(stringId), length).show()

fun Context.getDimen(value: Float) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics).toInt()

fun View.requestFocusOnVisible(editText: EditText?) = setOnTouchListener { _, event ->
    if (event.action != MotionEvent.ACTION_DOWN) return@setOnTouchListener false

    editText?.let { if (it.visibility == View.VISIBLE) it.requestSelectionFocus() }

    return@setOnTouchListener false
}

fun EditText.addOnNextAction(func: () -> Unit) {
    setOnEditorActionListener { _, i, _ ->
        if (i == EditorInfo.IME_ACTION_NEXT) {
            func()
            return@setOnEditorActionListener true
        }

        return@setOnEditorActionListener false
    }
}

fun EditText.requestSelectionFocus() {
    if (!hasFocus()) requestFocus()
    setSelection(text.toString().length)
    showKeyboard()
}

fun EditText?.getClearText(): String {
    if (this == null) return ""

    return text.toString().trim().replace("\\s+".toRegex(), " ")
}

fun <T> MutableList<T>.swap(from: Int, to: Int) {
    val item = get(from)
    removeAt(from)
    add(to, item)
}

fun <E> MutableList<E>.clearAndAdd(replace: MutableList<E>) {
    clear()
    addAll(replace)
}

fun RecyclerView.ViewHolder.checkNoPosition(func: () -> Unit): Boolean {
    if (adapterPosition == RecyclerView.NO_POSITION) return false

    func()
    return true
}

fun EditText.addTextChangedListener(before: (String) -> Unit = {},
                                    on: (String) -> Unit = {},
                                    after: (String) -> Unit = {}) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                before(s.toString())

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                on(s.toString())

        override fun afterTextChanged(s: Editable?) = after(s.toString())
    })
}

fun Context.sendTo(place: String, command: String, extras: Intent.() -> Unit = {}) =
        sendBroadcast(Intent(place).apply {
            putExtra(ReceiverKey.Values.COMMAND, command)
            putExtras(Intent().apply(extras))
        })