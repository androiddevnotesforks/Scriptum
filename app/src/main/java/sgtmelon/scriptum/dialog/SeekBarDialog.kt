package sgtmelon.scriptum.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import sgtmelon.safedialog.DialogBlank
import sgtmelon.scriptum.R

class SeekBarDialog : DialogBlank(), SeekBar.OnSeekBarChangeListener {

    private var init = 0
    var progress = 0
        private set

    fun setArguments(progress: Int) {
        arguments = Bundle().apply {
            putInt(INIT, progress)
            putInt(VALUE, progress)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments

        init = savedInstanceState?.getInt(INIT)
                ?: bundle?.getInt(INIT)
                        ?: 0

        progress = savedInstanceState?.getInt(VALUE)
                ?: bundle?.getInt(VALUE)
                        ?: 0

        val view = LayoutInflater.from(context).inflate(R.layout.view_volume, null)

        val seekBar: SeekBar = view.findViewById(R.id.volume_seek_bar)
        seekBar.progress = progress
        seekBar.setOnSeekBarChangeListener(this)

        // TODO #RELEASE

        return AlertDialog.Builder(activity)
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
        this.progress = progress
        setEnable()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

}