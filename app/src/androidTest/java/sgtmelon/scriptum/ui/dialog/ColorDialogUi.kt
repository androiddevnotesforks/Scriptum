package sgtmelon.scriptum.ui.dialog

import androidx.annotation.IdRes
import androidx.annotation.IntDef
import sgtmelon.scriptum.R
import sgtmelon.scriptum.dialog.ColorDialog
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.ui.ParentDialogUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.basic.BasicValue

/**
 * Class for UI control of [ColorDialog]
 */
class ColorDialogUi(
        @Place private val place: Int,
        @Color private var check: Int,
        private val callback: Callback
) : ParentDialogUi<ColorDialogUi.Assert>() {

    @Color private var initCheck: Int = check

    override fun assert() = Assert(place, check, enabled = check != initCheck)

    @IdRes private val recyclerId = R.id.color_recycler_view
    private val count: Int get() = BasicValue().getCount(recyclerId)
    private val positionRandom: Int get() = (0 until count).random()

    private fun getNewPosition(position: Int): Int = if (position == check) {
        getNewPosition(positionRandom)
    } else {
        position
    }

    fun onClickItem(@Color position: Int = positionRandom) = apply {
        check = getNewPosition(position)

        action { onClick(recyclerId, check) }

        assert()
    }

    fun onClickEveryItem() = apply { (0 until count).forEach { onClickItem(it) } }

    fun onClickCancel() = waitClose { action { onClickText(R.string.dialog_button_cancel) } }

    fun onClickAccept() = waitClose {
        action { onClickText(R.string.dialog_button_accept) }
        callback.onDialogResult(check)
    }


    /**
     * Describes [Place] of [ColorDialog] for decide title in [Assert]
     */
    @IntDef(Place.NOTE, Place.PREF)
    annotation class Place {
        companion object {
            const val NOTE = 0
            const val PREF = 1
        }
    }

    interface Callback {
        fun onDialogResult(@Color check: Int)
    }

    // TODO create check assert for all items (may be with contentDescription)
    class Assert(@Place place: Int, @Color check: Int, enabled: Boolean) : BasicMatch() {
        init {
            onDisplay(R.id.color_recycler_view)

            onDisplayText(when (place) {
                Place.NOTE -> R.string.dialog_title_color
                else -> R.string.title_note_color
            })

            onDisplayText(R.string.dialog_button_cancel)
            onDisplayText(R.string.dialog_button_accept)

            isEnabledText(R.string.dialog_button_accept, enabled)
        }
    }

    companion object {
        operator fun invoke(func: ColorDialogUi.() -> Unit, @Place place: Int, @Color check: Int,
                            callback: Callback) = ColorDialogUi(place, check, callback).apply(func)
    }

}