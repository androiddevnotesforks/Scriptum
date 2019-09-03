package sgtmelon.scriptum.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.BlankDialog
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R

class AboutDialog : BlankDialog(), View.OnClickListener {

    private var click = 0

    private val maxClick get() = context?.resources?.getInteger(R.integer.value_develop_open) ?: 1

    var hideOpen = false
        private set

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
            hideOpen = true
            dialog?.cancel()
        }
    }

    fun clear() {
        click = 0
        hideOpen = false
    }

}