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
import sgtmelon.scriptum.control.alarm.VibratorControl
import sgtmelon.scriptum.control.alarm.callback.IVibratorControl

class AboutDialog : BlankDialog(), View.OnClickListener {

    private val iVibratorControl: IVibratorControl by lazy { VibratorControl(context) }

    private var click = 0

    private val maxClick get() = context?.resources?.getInteger(R.integer.value_develop_open) ?: 1

    private val scaleTo get() = SCALE_FROM - (SCALE_FROM - SCALE_TO) * click / maxClick

    private var animate = false
    var hideOpen = false

    private val vibratorPattern get() = longArrayOf(0, 75L * click / maxClick)

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
        if (animate) return

        animate = true

        if (click != 0) iVibratorControl.start(vibratorPattern)

        if (++click == maxClick) {
            v.pulse(SCALE_TO) {
                hideOpen = true
                dialog?.cancel()
            }
        } else {
            v.pulse(scaleTo) { animate = false }
        }
    }

    fun clear() {
        click = 0
        animate = false
        hideOpen = false
    }

    private fun View.pulse(scaleTo: Float, endAction: () -> Unit) = scale(scaleTo) {
        scale(SCALE_FROM, endAction)
    }

    private fun View.scale(value: Float, endAction: () -> Unit) = animate()
            .setInterpolator(AccelerateDecelerateInterpolator()).setDuration(DURATION)
            .scaleX(value).scaleY(value)
            .withEndAction(endAction)

    private companion object {
        const val DURATION = 125L
        const val SCALE_FROM = 1f
        const val SCALE_TO = 0.9f
    }

}