package sgtmelon.scriptum.cleanup.ui.dialog

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.withColorIndicator
import sgtmelon.scriptum.cleanup.presentation.dialog.ColorDialog
import sgtmelon.scriptum.cleanup.ui.IDialogUi
import sgtmelon.scriptum.cleanup.ui.ParentRecyclerScreen
import sgtmelon.scriptum.infrastructure.adapter.ColorAdapter
import sgtmelon.scriptum.infrastructure.model.data.ColorData
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerItemPart
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.getCount
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.withContentDescription
import sgtmelon.test.cappuccino.utils.withDrawableColor
import sgtmelon.test.cappuccino.utils.withSize
import sgtmelon.test.cappuccino.utils.withTextColor

/**
 * Class for UI control of [ColorDialog].
 */
class ColorDialogUi(
    place: Place,
    private var color: Color,
    private val callback: Callback
) : ParentRecyclerScreen(R.id.color_recycler_view), IDialogUi {

    //region Views

    private val titleText = getViewByText(when (place) {
        Place.NOTE -> R.string.dialog_title_color
        Place.PREF -> R.string.pref_title_note_color
    })

    private val cancelButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_cancel)
    private val applyButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_apply)

    private fun getItem(p: Int) = Item(recyclerView, p)

    //endregion

    private var initColor: Color = color

    fun onClickItem(setColor: Color = Color.values().random()) = apply {
        val newColor = getNewColor(setColor)
        color = newColor

        recyclerView.click(newColor.ordinal)

        assert()
    }

    fun onClickAll() = apply {
        val values = Color.values()
        for (i in 0 until recyclerView.getCount()) onClickItem(values[i])
    }

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun onClickApply() = waitClose {
        if (color == initColor) throw IllegalAccessException("Apply button not enabled")

        applyButton.click()
        callback.onColorDialogResult(color)
    }


    fun onAssertItem(check: Color = color) = apply {
        getItem(check.ordinal).assert(ColorItem(check, isCheck = check == color))
    }

    fun onAssertAll() {
        val count = recyclerView.getCount()
        
        for (p1 in 0 until count) {
            for (p2 in 0 until count) {
                onAssertItem(Color.values()[p2])
            }

            val p1Color = Color.values()[p1]
            onClickItem(p1Color).onAssertItem(p1Color)
        }
    }

    fun assert() {
        titleText.isDisplayed().withTextColor(R.attr.clContent)
        recyclerView.isDisplayed()

        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clContentSecond)
        applyButton.isDisplayed().isEnabled(value = color != initColor) {
            withTextColor(R.attr.clAccent)
        }
    }

    /**
     * Return position different from [color] and [initColor]
     */
    private fun getNewColor(color: Color = Color.values().random()): Color {
        return if (color == this.color || color == initColor) getNewColor() else color
    }

    /**
     * Class for UI control of [ColorAdapter].
     */
    private class Item(
        listMatcher: Matcher<View>,
        p: Int
    ) : RecyclerItemPart<ColorItem>(listMatcher, p) {

        private val parentContainer by lazy { getChild(getView(R.id.parent_container)) }
        private val backgroundView by lazy { getChild(getView(R.id.background_view)) }
        private val checkImage by lazy { getChild(getView(R.id.check_image)) }
        private val clickView by lazy { getChild(getView(R.id.click_view)) }

        override fun assert(item: ColorItem) {
            parentContainer.isDisplayed()

            backgroundView.isDisplayed()
                .withSize(R.dimen.icon_48dp, R.dimen.icon_48dp)
                .withColorIndicator(R.drawable.ic_color, theme, item.color)

            val colorId = ColorData.getColorItem(theme, item.color).content
            checkImage.isDisplayed(item.isCheck).withDrawableColor(R.drawable.ic_check, colorId)

            // TODO record exception in real code
            val colorName = context.resources.getStringArray(R.array.pref_color)[item.color.ordinal]
            val description = context.getString(R.string.description_item_color, colorName)
            clickView.isDisplayed()
                    .withSize(R.dimen.icon_48dp, R.dimen.icon_48dp)
                    .withContentDescription(description)
        }

    }

    /**
     * Model for [Item.assert]
     */
    private data class ColorItem(val color: Color, val isCheck: Boolean)

    /**
     * Describes [Place] of [ColorDialog] for decide title
     */
    enum class Place { NOTE, PREF }

    interface Callback {
        fun onColorDialogResult(color: Color)
    }

    companion object {
        inline operator fun invoke(
            func: ColorDialogUi.() -> Unit,
            place: Place,
            color: Color,
            callback: Callback
        ): ColorDialogUi {
            return ColorDialogUi(place, color, callback).apply { waitOpen { assert() } }.apply(func)
        }
    }
}