package sgtmelon.scriptum.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.DialogBlank
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R

class InfoDialog : DialogBlank(), View.OnClickListener {

    lateinit var logoClick: View.OnClickListener

    private var click = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (savedInstanceState != null) {
            click = savedInstanceState.getInt(VALUE)
        }

        val view = LayoutInflater.from(context).inflate(R.layout.view_about, null)

        view.findViewById<ImageView>(R.id.about_logo_image).setOnClickListener(this)
        view.findViewById<TextView>(R.id.about_version).text = BuildConfig.VERSION_NAME

        return AlertDialog.Builder(activity)
                .setView(view)
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply { putInt(VALUE, click) })

    override fun onClick(v: View) {
        if (++click == activity.resources.getInteger(R.integer.value_develop_open)) {
            click = 0
            logoClick.onClick(v)
            dialog?.cancel()
        }
    }

}
