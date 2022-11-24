package sgtmelon.scriptum.cleanup.ui.dialog.preference

import androidx.annotation.IntRange
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.dialog.VolumeDialog
import sgtmelon.scriptum.cleanup.ui.ParentScreen
import sgtmelon.scriptum.parent.ui.feature.DialogUi
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.setProgress
import sgtmelon.test.cappuccino.utils.withProgress
import sgtmelon.test.cappuccino.utils.withText
import sgtmelon.test.cappuccino.utils.withTextColor

/**
 * Class for UI control of [VolumeDialog].
 */
class VolumeDialogUi : ParentScreen(),
    DialogUi {

    private val initValue = preferencesRepo.volumePercent
    private var value = initValue

    //region Views

    private val titleText = getViewByText(R.string.pref_title_alarm_volume)

    private val progressText = getViewById(R.id.volume_progress_text)
    private val seekBar = getViewById(R.id.volume_seek_bar)

    private val cancelButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_cancel)
    private val applyButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_apply)


    //endregion

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun apply() = waitClose {
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
        applyButton.isDisplayed().isEnabled(value = value != initValue) {
            withTextColor(R.attr.clAccent)
        }
    }

    companion object {
        inline operator fun invoke(func: VolumeDialogUi.() -> Unit): VolumeDialogUi {
            return VolumeDialogUi().apply { waitOpen { assert() } }.apply(func)
        }

        /** Int array with values from 10 up to 100 */
        val list = Array(size = 90) { it + 10 }
    }
}