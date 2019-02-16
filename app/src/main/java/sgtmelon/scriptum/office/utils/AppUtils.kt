package sgtmelon.scriptum.office.utils

import android.app.Activity
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import sgtmelon.scriptum.app.adapter.ParentAdapter
import sgtmelon.scriptum.databinding.IncludeInfoBinding
import sgtmelon.scriptum.office.data.IntroData

object AppUtils {

    // TODO раскидать по разным Utils

    //TODO вынести в родительский адаптер просто и добавить другие обновления
    fun <T> ParentAdapter<T, RecyclerView.ViewHolder>.notifyItemChanged(list: MutableList<T>, p: Int) {
        setList(list)
        notifyItemChanged(p)
    }

    //TODO вынести в родительский адаптер просто и добавить другие обновления
    fun <T> ParentAdapter<T, RecyclerView.ViewHolder>.notifyItemRemoved(list: MutableList<T>, p: Int) {
        setList(list)
        notifyItemRemoved(p)
    }

    fun Activity.beforeFinish(function: () -> Unit) {
        function.invoke()
        finish()
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