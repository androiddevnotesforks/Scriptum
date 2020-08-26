package sgtmelon.scriptum.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.IntRange
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.BlankDialog
import sgtmelon.safedialog.applyAnimation
import sgtmelon.scriptum.R

class VolumeDialog : BlankDialog(), SeekBar.OnSeekBarChangeListener {

    private val seekBar get() = dialog?.findViewById<SeekBar?>(R.id.volume_seek_bar)
    private val progressText get() = dialog?.findViewById<TextView?>(R.id.volume_progress_text)

    private var init = 0
    var progress = 0
        private set

    fun setArguments(@IntRange(from = 10, to = 100) progress: Int) = apply {
        arguments = Bundle().apply {
            putInt(INIT, progress)
            putInt(VALUE, progress)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        init = savedInstanceState?.getInt(INIT) ?: arguments?.getInt(INIT) ?: 0
        progress = savedInstanceState?.getInt(VALUE) ?: arguments?.getInt(VALUE) ?: 0

        return AlertDialog.Builder(context as Context)
            .setTitle(title)
            .setView(R.layout.view_volume)
            .setPositiveButton(getString(R.string.dialog_button_apply), onPositiveClick)
            .setNegativeButton(getString(R.string.dialog_button_cancel)) { dialog, _ -> dialog.cancel() }
            .setCancelable(true)
            .create()
            .applyAnimation()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putInt(INIT, init)
            putInt(VALUE, progress)
        })
    }

    override fun setupView() {
        super.setupView()

        seekBar?.apply {
            progress = this@VolumeDialog.progress
            setOnSeekBarChangeListener(this@VolumeDialog)
        }

        progressText?.text = getString(R.string.dialog_text_volume, progress)
    }

    override fun setEnable() {
        super.setEnable()
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
        setEnable()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    companion object {
        const val MIN_VALUE = 10
    }

}