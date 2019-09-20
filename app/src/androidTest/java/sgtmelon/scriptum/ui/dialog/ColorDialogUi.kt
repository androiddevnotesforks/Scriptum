package sgtmelon.scriptum.ui.dialog

import sgtmelon.scriptum.R
import sgtmelon.scriptum.dialog.ColorDialog
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.ui.ParentDialogUi
import sgtmelon.scriptum.ui.basic.*

/**
 * Class for UI control of [ColorDialog]
 */
class ColorDialogUi(place: Place, @Color private var check: Int, private val callback: Callback) :
        ParentDialogUi() {

    // TODO create check assert for all items (may be with contentDescription)

    //region Views

    private val titleText = getViewByText(when (place) {
        Place.NOTE -> R.string.dialog_title_color
        Place.PREF -> R.string.title_note_color
    })

    private val recyclerView = getViewById(R.id.color_recycler_view)

    private val cancelButton = getViewByText(R.string.dialog_button_cancel)
    private val applyButton = getViewByText(R.string.dialog_button_apply)

    //endregion

    @Color private var initCheck: Int = check

    fun onClickItem(@Color position: Int = recyclerView.getRandomPosition()) = apply {
        check = getNewPosition(position)

        recyclerView.click(check)

        assert()
    }

    fun onClickEveryItem() = apply {
        (0 until recyclerView.getCount()).forEach { onClickItem(it) }
    }

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun onClickAccept() = waitClose {
        applyButton.click()
        callback.onDialogResult(check)
    }


    fun assert() {
        titleText.isDisplayed()
        recyclerView.isDisplayed()

        cancelButton.isDisplayed().isEnabled()
        applyButton.isDisplayed().isEnabled(enabled = check != initCheck)
    }

    /**
     * Return position different from [check] and [initCheck]
     */
    private fun getNewPosition(p: Int = recyclerView.getRandomPosition()): Int {
        return if (p == check || p == initCheck) getNewPosition() else p
    }


    /**
     * Describes [Place] of [ColorDialog] for decide title in [Assert]
     */
    enum class Place { NOTE, PREF }

    interface Callback {
        fun onDialogResult(@Color check: Int)
    }

    companion object {
        operator fun invoke(func: ColorDialogUi.() -> Unit, place: Place,
                            @Color check: Int, callback: Callback) =
                ColorDialogUi(place, check, callback).apply { waitOpen { assert() } }.apply(func)
    }

}