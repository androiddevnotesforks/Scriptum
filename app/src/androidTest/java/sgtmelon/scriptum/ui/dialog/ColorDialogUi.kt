package sgtmelon.scriptum.ui.dialog

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.dialog.ColorDialog
import sgtmelon.scriptum.extension.getCompatColor
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.data.ColorData
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentRecyclerItem
import sgtmelon.scriptum.ui.ParentRecyclerScreen

/**
 * Class for UI control of [ColorDialog]
 */
class ColorDialogUi(place: Place, @Color private var check: Int, private val callback: Callback) :
        ParentRecyclerScreen(R.id.color_recycler_view), IDialogUi {

    //region Views

    private val titleText = getViewByText(when (place) {
        Place.NOTE -> R.string.dialog_title_color
        Place.PREF -> R.string.title_note_color
    })

    private val cancelButton = getViewByText(R.string.dialog_button_cancel)
    private val applyButton = getViewByText(R.string.dialog_button_apply)

    private fun getItem(p: Int) = Item(p)

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


    fun onAssertItem(p: Int) {
        getItem(p).assert(p)
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


    private inner class Item(p: Int) : ParentRecyclerItem<Int>(recyclerView, p) {

        private val parentContainer by lazy { getChild(getViewById(R.id.color_parent_container)) }
        private val backgroundView by lazy { getChild(getViewById(R.id.color_background_view)) }
        private val checkImage by lazy { getChild(getViewById(R.id.color_check_image)) }
        private val clickView by lazy { getChild(getViewById(R.id.color_click_view)) }

        override fun assert(model: Int) {
            parentContainer.isDisplayed()

            backgroundView.isDisplayed().withColorIndicator(R.drawable.ic_color, theme, model)

            val checkTint = context.getCompatColor(ColorData.getColorItem(theme, model).content)
            checkImage.isDisplayed(visible = model == check).withDrawable(
                    R.drawable.ic_check, checkTint
            )

            clickView.isDisplayed()
        }

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