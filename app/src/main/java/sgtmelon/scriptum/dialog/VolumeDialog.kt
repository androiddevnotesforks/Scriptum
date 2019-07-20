package sgtmelon.scriptum.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.IntRange
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.DialogBlank
import sgtmelon.scriptum.R

class VolumeDialog : DialogBlank(), SeekBar.OnSeekBarChangeListener {

    private var init = 0
    var progress = 0
        private set

    private var progressText: TextView? = null

    fun setArguments(@IntRange(from = 10, to = 100) progress: Int) {
        arguments = Bundle().apply {
            putInt(INIT, progress)
            putInt(VALUE, progress)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        init = savedInstanceState?.getInt(INIT) ?: arguments?.getInt(INIT) ?: 0
        progress = savedInstanceState?.getInt(VALUE) ?: arguments?.getInt(VALUE) ?: 0

        val view = LayoutInflater.from(context).inflate(R.layout.view_volume, null)

        view.findViewById<SeekBar>(R.id.volume_seek_bar).apply {
            progress = this@VolumeDialog.progress
            setOnSeekBarChangeListener(this@VolumeDialog)
        }

        progressText = view.findViewById(R.id.volume_progress_text)
        progressText?.text = getString(R.string.dialog_text_volume, progress)

        return AlertDialog.Builder(context as Context)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(getString(R.string.dialog_btn_accept), onPositiveClick)
                .setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialog, _ -> dialog.cancel() }
                .setCancelable(true)
                .create()
                .apply { window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) }
    }

    override fun onSaveInstanceState(outState: Bundle) =
            super.onSaveInstanceState(outState.apply {
                putInt(INIT, init)
                putInt(VALUE, progress)
            })

    override fun setEnable() {
        super.setEnable()
        buttonPositive?.isEnabled = init != progress
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (progress < MIN_VALUE) {
            seekBar?.progress = MIN_VALUE
            this.progress = MIN_VALUE
        } else if (progress % STEP_VALUE == 0) {
            this.progress = progress
        } else {
            this.progress = progress / 5 * 5
        }

        progressText?.text = getString(R.string.dialog_text_volume, progress)
        setEnable()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    companion object {
        const val MIN_VALUE = 10
        const val STEP_VALUE = 5
    }

}