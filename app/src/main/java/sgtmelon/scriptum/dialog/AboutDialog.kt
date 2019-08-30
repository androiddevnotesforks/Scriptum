package sgtmelon.scriptum.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.BlankDialog
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R

class AboutDialog : BlankDialog(), View.OnClickListener {

    var logoClick: View.OnClickListener? = null

    private val maxClick by lazy {
        context?.resources?.getInteger(R.integer.value_develop_open) ?: 1
    }
    private var click = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        savedInstanceState?.also { click = it.getInt(VALUE) }

        val view = LayoutInflater.from(context).inflate(R.layout.view_about, null)

        view.findViewById<ImageView>(R.id.about_logo_image).setOnClickListener(this)
        view.findViewById<TextView>(R.id.about_version).text = BuildConfig.VERSION_NAME

        return AlertDialog.Builder(context as Context)
                .setView(view)
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply { putInt(VALUE, click) })

    override fun onClick(v: View) {
        if (++click == maxClick) {
            click = 0
            logoClick?.onClick(v)
            dialog?.cancel()
        } else {
            v.isEnabled = false
            v.scale(getScaleTo()) { v.scale(SCALE_FROM) { v.isEnabled = true } }
        }
    }

    private fun getScaleTo() = SCALE_FROM - (SCALE_FROM - SCALE_TO) * click / maxClick

    private fun View.scale(value: Float, func: () -> Unit) = animate()
            .setInterpolator(AccelerateDecelerateInterpolator()).setDuration(DURATION)
            .scaleX(value).scaleY(value)
            .withEndAction(func)

    private companion object {
        const val DURATION = 150L
        const val SCALE_FROM = 1f
        const val SCALE_TO = 0.9f
    }

}
