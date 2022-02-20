package sgtmelon.scriptum.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.IntRange
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.dialog.parent.BlankDialog
import sgtmelon.safedialog.annotation.NdValue
import sgtmelon.safedialog.annotation.SavedTag
import sgtmelon.safedialog.utils.applyAnimation
import sgtmelon.scriptum.R

class VolumeDialog : BlankDialog(), SeekBar.OnSeekBarChangeListener {

    private val seekBar get() = dialog?.findViewById<SeekBar?>(R.id.volume_seek_bar)
    private val progressText get() = dialog?.findViewById<TextView?>(R.id.volume_progress_text)

    private var init = NdValue.CHECK
    var progress = NdValue.CHECK
        private set

    fun setArguments(@IntRange(from = 10, to = 100) progress: Int) = apply {
        arguments = Bundle().apply {
            putInt(SavedTag.INIT, progress)
            putInt(SavedTag.VALUE, progress)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        return AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(R.layout.view_volume)
            .setPositiveButton(getString(R.string.dialog_button_apply), onPositiveClick)
            .setNegativeButton(getString(R.string.dialog_button_cancel)) { dialog, _ -> dialog.cancel() }
            .setCancelable(true)
            .create()
            .applyAnimation()
    }

    override fun onRestoreArgumentState(bundle: Bundle?) {
        super.onRestoreArgumentState(bundle)

        init = bundle?.getInt(SavedTag.INIT) ?: NdValue.CHECK
        progress = bundle?.getInt(SavedTag.VALUE) ?: NdValue.CHECK
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SavedTag.INIT, init)
        outState.putInt(SavedTag.VALUE, progress)
    }

    override fun setupView() {
        super.setupView()

        seekBar?.apply {
            progress = this@VolumeDialog.progress
            setOnSeekBarChangeListener(this@VolumeDialog)
        }

        progressText?.text = getString(R.string.dialog_text_volume, progress)
    }

    override fun changeButtonEnable() {
        super.changeButtonEnable()
        positiveButton?.isEnabled = init != progress
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (progress < MIN_VALUE) {
            seekBar?.progress = MIN_VALUE
            this.progress = MIN_VALUE
        } else {
            this.progress = progress
        }

        progressText?.text = getString(R.string.dialog_text_volume, this.progress)
        changeButtonEnable()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

    override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit

    companion object {
        const val MIN_VALUE = 10
    }
}