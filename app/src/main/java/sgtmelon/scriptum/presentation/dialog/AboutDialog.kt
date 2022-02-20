package sgtmelon.scriptum.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.annotation.NdValue
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.dialog.parent.BlankEmptyDialog
import sgtmelon.safedialog.utils.applyAnimation
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R

class AboutDialog : BlankEmptyDialog(),
    View.OnClickListener {

    private val logoImage get() = dialog?.findViewById<ImageView?>(R.id.about_logo_image)
    private val versionText get() = dialog?.findViewById<TextView?>(R.id.about_version_text)

    private var click = NdValue.CHECK
    var hideOpen = NdValue.KEY
        private set

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return AlertDialog.Builder(requireContext())
            .setView(R.layout.view_about)
            .setCancelable(true)
            .create()
            .applyAnimation()
    }

    override fun onRestoreContentState(savedState: Bundle) {
        super.onRestoreContentState(savedState)
        click = savedState.getInt(SavedTag.VALUE)
        hideOpen = savedState.getBoolean(SavedTag.KEY)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SavedTag.VALUE, click)
        outState.putBoolean(SavedTag.KEY, hideOpen)
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
        click = NdValue.CHECK
        hideOpen = NdValue.KEY
    }
}