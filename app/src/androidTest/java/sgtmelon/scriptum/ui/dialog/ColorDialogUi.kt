package sgtmelon.scriptum.ui.dialog

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.ColorAdapter
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.dialog.ColorDialog
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.data.ColorData
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentRecyclerItem
import sgtmelon.scriptum.ui.ParentRecyclerScreen

/**
 * Class for UI control of [ColorDialog].
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

    private fun getItem(p: Int) = Item(recyclerView, p)

    //endregion

    @Color private var initCheck: Int = check

    fun onClickItem(p: Int = recyclerView.getRandomPosition()) = apply {
        check = getNewPosition(p)

        recyclerView.click(check)

        assert()
    }

    fun onClickAll() = apply { (0 until recyclerView.getCount()).forEach { onClickItem(it) } }

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun onClickAccept() = waitClose {
        applyButton.click()
        callback.onColorDialogResult(check)
    }


    fun onAssertItem(p: Int = check) {
        getItem(p).assert(ColorItem(p, isCheck = p == check))
    }

    fun onAssertAll() {
        (0 until recyclerView.getCount()).forEach {
            (0 until recyclerView.getCount()).forEach { p -> onAssertItem(p) }
            onClickItem(it).onAssertItem(it)
        }
    }

    fun assert() {
        titleText.isDisplayed()
        recyclerView.isDisplayed()

        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
        applyButton.isDisplayed().isEnabled(enabled = check != initCheck) {
            withTextColor(R.attr.clAccent)
        }
    }

    /**
     * Return position different from [check] and [initCheck]
     */
    private fun getNewPosition(p: Int = recyclerView.getRandomPosition()): Int {
        return if (p == check || p == initCheck) getNewPosition() else p
    }

    /**
     * Class for UI control of [ColorAdapter].
     */
    private class Item(listMatcher: Matcher<View>, p: Int) :
            ParentRecyclerItem<ColorItem>(listMatcher, p) {

        private val parentContainer by lazy { getChild(getViewById(R.id.color_parent_container)) }
        private val backgroundView by lazy { getChild(getViewById(R.id.color_background_view)) }
        private val checkImage by lazy { getChild(getViewById(R.id.color_check_image)) }
        private val clickView by lazy { getChild(getViewById(R.id.color_click_view)) }

        override fun assert(item: ColorItem) {
            parentContainer.isDisplayed()

            backgroundView.isDisplayed()
                    .withSize(R.dimen.icon_48dp, R.dimen.icon_48dp)
                    .withColorIndicator(R.drawable.ic_color, theme, item.color)

            val colorId = ColorData.getColorItem(theme, item.color).content
            checkImage.isDisplayed(item.isCheck).withDrawableColor(R.drawable.ic_check, colorId)

            clickView.isDisplayed().withSize(R.dimen.icon_48dp, R.dimen.icon_48dp)
        }

    }

    /**
     * Model for [Item.assert]
     */
    private data class ColorItem(val color: Int, val isCheck: Boolean)

    /**
     * Describes [Place] of [ColorDialog] for decide title
     */
    enum class Place { NOTE, PREF }

    interface Callback {
        fun onColorDialogResult(@Color check: Int)
    }

    companion object {
        operator fun invoke(func: ColorDialogUi.() -> Unit,
                            place: Place, @Color check: Int,
                            callback: Callback): ColorDialogUi {
            return ColorDialogUi(place, check, callback).apply { waitOpen { assert() } }.apply(func)
        }
    }

}