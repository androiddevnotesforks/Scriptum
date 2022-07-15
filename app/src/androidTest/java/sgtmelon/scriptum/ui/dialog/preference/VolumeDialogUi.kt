package sgtmelon.scriptum.ui.dialog.preference

import androidx.annotation.IntRange
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.cleanup.presentation.dialog.VolumeDialog
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentUi

/**
 * Class for UI control of [VolumeDialog].
 */
class VolumeDialogUi : ParentUi(),
    IDialogUi {

    private val initValue = preferences.volume
    private var value = preferences.volume

    //region Views

    private val titleText = getViewByText(R.string.pref_title_alarm_volume)

    private val progressText = getViewById(R.id.volume_progress_text)
    private val seekBar = getViewById(R.id.volume_seek_bar)

    private val cancelButton = getViewByText(R.string.dialog_button_cancel)
    private val applyButton = getViewByText(R.string.dialog_button_apply)


    //endregion

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun onClickApply() = waitClose {
        if (value == initValue) throw IllegalAccessException("Apply button not enabled")

        applyButton.click()
    }

    fun seekTo(@IntRange(from = 10, to = 100) progress: Int) = apply {
        value = progress

        seekBar.setProgress(progress)
        assert()
    }

    fun assert() {
        titleText.isDisplayed()

        val text = context.getString(R.string.dialog_text_volume, value)
        progressText.isDisplayed().withText(text, R.attr.clContentSecond, R.dimen.text_16sp)

        seekBar.isDisplayed().withProgress(value, max = 100)

        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
        applyButton.isDisplayed().isEnabled(isEnabled = value != initValue) {
            withTextColor(R.attr.clAccent)
        }
    }

    companion object {
        operator fun invoke(func: VolumeDialogUi.() -> Unit): VolumeDialogUi {
            return VolumeDialogUi().apply { waitOpen { assert() } }.apply(func)
        }

        fun random() = (10..100).random()
    }
}