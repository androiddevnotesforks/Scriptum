package sgtmelon.scriptum.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.BlankDialog
import sgtmelon.safedialog.applyAnimation
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R

class AboutDialog : BlankDialog(), View.OnClickListener {

    private val logoImage get() = dialog?.findViewById<ImageView?>(R.id.about_logo_image)
    private val versionText get() = dialog?.findViewById<TextView?>(R.id.about_version)

    private var click = 0

    var hideOpen = false
        private set

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        savedInstanceState?.also { click = it.getInt(VALUE) }

        return AlertDialog.Builder(context as Context)
            .setView(R.layout.view_about)
            .setCancelable(true)
            .create()
            .applyAnimation()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(VALUE, click)
    }

    override fun setupView() {
        super.setupView()

        logoImage?.setOnClickListener(this)
        versionText?.text = BuildConfig.VERSION_NAME
    }

    override fun onClick(v: View) {
        if (++click == context?.resources?.getInteger(R.integer.pref_develop_open)) {
            hideOpen = true
            dialog?.cancel()
        }
    }

    fun clear() {
        click = 0
        hideOpen = false
    }

}