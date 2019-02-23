package sgtmelon.scriptum.widget

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.DialogBlank
import sgtmelon.scriptum.R

class InfoDialog : DialogBlank(), View.OnClickListener {

    lateinit var logoClick: View.OnClickListener

    private var click = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (savedInstanceState != null) {
            click = savedInstanceState.getInt(VALUE)
        }

        val view = LayoutInflater.from(context).inflate(R.layout.view_about, null)

        view.findViewById<ImageView>(R.id.logo_image)
                .setOnClickListener(this)

        return AlertDialog.Builder(activity)
                .setView(view)
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(VALUE, click)
    }

    override fun onClick(v: View) {
        if (++click == activity.resources.getInteger(R.integer.pref_logo_click_value)) {
            click = 0
            logoClick.onClick(v)
            dialog.cancel()
        }
    }

}
