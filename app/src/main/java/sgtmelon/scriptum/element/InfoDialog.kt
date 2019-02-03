package sgtmelon.scriptum.element

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.office.annot.DialogAnn
import sgtmelon.safedialog.office.blank.DialogBlank
import sgtmelon.scriptum.R

class InfoDialog : DialogBlank(), View.OnClickListener {

    lateinit var logoClick: View.OnClickListener

    private var click = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (savedInstanceState != null) {
            click = savedInstanceState.getInt(DialogAnn.VALUE)
        }

        val view = LayoutInflater.from(context).inflate(R.layout.view_about, null)

        view.findViewById<ImageView>(R.id.logo_image)
                .setOnClickListener(this)

        return AlertDialog.Builder(context!!)
                .setView(view)
                .setCancelable(true)
                .create()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(DialogAnn.VALUE, click)
    }

    override fun onClick(v: View) {
        if (++click == context!!.resources.getInteger(R.integer.pref_logo_click_value)) {
            click = 0
            logoClick.onClick(v)
            dialog.cancel()
        }
    }

}
