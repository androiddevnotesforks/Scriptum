package sgtmelon.scriptum.source.ui.screen.dialogs

import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.dialogs.ColorDialog
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.source.ui.feature.DialogUi
import sgtmelon.scriptum.source.ui.parts.UiPart
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.getCount
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.withTextColor

/**
 * Class for UI control of [ColorDialog].
 */
class ColorDialogUi(
    place: Place,
    private var color: Color,
    private val callback: Callback
) : UiPart(),
    DialogUi {

    //region Views

    private val titleText = getViewByText(
        when (place) {
            Place.NOTE -> R.string.dialog_title_color
            Place.PREF -> R.string.pref_title_note_color
        }
    )

    private val recyclerView = getView(R.id.color_recycler_view)
    private val cancelButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_cancel)
    private val applyButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_apply)

    private fun getItem(p: Int) = ColorItemUi(recyclerView, p)

    //endregion

    private var initColor: Color = color

    fun select(setColor: Color = Color.values().random()) = apply {
        val newColor = getNewColor(setColor)
        color = newColor

        recyclerView.click(newColor.ordinal)

        assert()
    }

    /**
     * Return position different from [color] and [initColor]
     */
    private fun getNewColor(color: Color = Color.values().random()): Color {
        return if (color == this.color || color == initColor) getNewColor() else color
    }

    fun selectAll() = apply {
        val values = Color.values()
        for (i in 0 until recyclerView.getCount()) select(values[i])
    }

    fun cancel() = waitClose { cancelButton.click() }

    fun apply() = waitClose {
        if (color == initColor) throw IllegalAccessException("Apply button not enabled")

        applyButton.click()
        callback.onColorDialogResult(color)
    }


    fun assertItem(check: Color = color) = apply {
        val item = ColorItemUi.Model(check, isCheck = check == color)
        getItem(check.ordinal).assert(item)
    }

    fun assertAll() {
        val count = recyclerView.getCount()

        for (p1 in 0 until count) {
            for (p2 in 0 until count) {
                assertItem(Color.values()[p2])
            }

            val p1Color = Color.values()[p1]
            select(p1Color).assertItem(p1Color)
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

    /** Describes [Place] of [ColorDialog] for decide title. */
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
            return ColorDialogUi(place, color, callback)
                .apply { waitOpen { assert() } }
                .apply(func)
        }
    }
}