package sgtmelon.scriptum.cleanup.ui.dialog.preference

import androidx.annotation.IntRange
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.click
import sgtmelon.scriptum.cleanup.basic.extension.isDisplayed
import sgtmelon.scriptum.cleanup.basic.extension.isEnabled
import sgtmelon.scriptum.cleanup.basic.extension.setProgress
import sgtmelon.scriptum.cleanup.basic.extension.withProgress
import sgtmelon.scriptum.cleanup.basic.extension.withText
import sgtmelon.scriptum.cleanup.basic.extension.withTextColor
import sgtmelon.scriptum.cleanup.presentation.dialog.VolumeDialog
import sgtmelon.scriptum.cleanup.ui.IDialogUi
import sgtmelon.scriptum.cleanup.ui.ParentUi

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

    private val cancelButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_cancel)
    private val applyButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_apply)


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
        titleText.isDisplayed().withTextColor(R.attr.clContent)

        val text = context.getString(R.string.dialog_text_volume, value)
        progressText.isDisplayed().withText(text, R.attr.clContentSecond, R.dimen.text_16sp)

        seekBar.isDisplayed().withProgress(value, max = 100)

        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clContentSecond)
        applyButton.isDisplayed().isEnabled(isEnabled = value != initValue) {
            withTextColor(R.attr.clAccent)
        }
    }

    companion object {
        operator fun invoke(func: VolumeDialogUi.() -> Unit): VolumeDialogUi {
            return VolumeDialogUi().apply { waitOpen { assert() } }.apply(func)
        }

        /** Int array with values from 10 up to 100 */
        val list = IntArray(size = 90) { it + 10 }
    }
}