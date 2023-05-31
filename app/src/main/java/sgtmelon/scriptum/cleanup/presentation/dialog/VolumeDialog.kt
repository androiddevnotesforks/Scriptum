package sgtmelon.scriptum.cleanup.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.IntRange
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.dialog.parent.BlankButtonDialog
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.scriptum.R

/**
 * Dialog with seekBar for change volume level.
 */
class VolumeDialog : BlankButtonDialog(),
    SeekBar.OnSeekBarChangeListener {

    private val seekBar get() = dialog?.findViewById<SeekBar?>(R.id.volume_seek_bar)
    private val progressText get() = dialog?.findViewById<TextView?>(R.id.volume_progress_text)

    private var progressInit = DEF_CHECK
    var progress = DEF_CHECK
        private set

    fun setArguments(@IntRange(from = 10, to = 100) progress: Int) = apply {
        arguments = Bundle().apply {
            putInt(SavedTag.Common.VALUE_INIT, progress)
            putInt(SavedTag.Common.VALUE, progress)
        }
    }

    override fun createDialog(context: Context): Dialog {
        return AlertDialog.Builder(context)
            .setTitle(title)
            .setView(R.layout.view_volume)
            .setPositiveButton(getString(R.string.dialog_button_apply), onPositiveClick)
            .setNegativeButton(getString(R.string.dialog_button_cancel), onNegativeClick)
            .setCancelable(true)
            .create()
    }

    override fun onRestoreArgumentState(bundle: Bundle?) {
        super.onRestoreArgumentState(bundle)

        progressInit = bundle?.getInt(SavedTag.Common.VALUE_INIT) ?: DEF_CHECK
        progress = bundle?.getInt(SavedTag.Common.VALUE) ?: DEF_CHECK
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SavedTag.Common.VALUE_INIT, progressInit)
        outState.putInt(SavedTag.Common.VALUE, progress)
    }

    override fun setupView() {
        super.setupView()

        seekBar?.progress = progress
        seekBar?.setOnSeekBarChangeListener(this@VolumeDialog)

        updateProgressText()
    }

    override fun changeButtonEnable() {
        super.changeButtonEnable()
        positiveButton?.isEnabled = progressInit != progress
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (progress < MIN_VALUE) {
            seekBar?.progress = MIN_VALUE
            this.progress = MIN_VALUE
        } else {
            this.progress = progress
        }

        updateProgressText()
        changeButtonEnable()
    }

    private fun updateProgressText() {
        progressText?.text = getString(R.string.dialog_text_volume, progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

    override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit

    companion object {
        const val MIN_VALUE = 10

        private const val DEF_CHECK = MIN_VALUE
    }
}