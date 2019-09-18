package sgtmelon.scriptum.ui.dialog

import androidx.annotation.IdRes
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
        private val place: Place,
        @Color private var check: Int,
        private val callback: Callback
) : ParentDialogUi() {

    @Color private var initCheck: Int = check

    fun assert() = Assert(place, check, enabled = check != initCheck)

    @IdRes private val recyclerId = R.id.color_recycler_view
    private val count: Int get() = BasicValue().getCount(recyclerId)
    private val positionRandom: Int get() = (0 until count).random()

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
     * Return position different from [check] and [initCheck]
     */
    private fun getNewPosition(p: Int): Int = if (p == check || p == initCheck) {
        getNewPosition(positionRandom)
    } else {
        p
    }

    /**
     * Describes [Place] of [ColorDialog] for decide title in [Assert]
     */
    enum class Place { NOTE, PREF }

    interface Callback {
        fun onDialogResult(@Color check: Int)
    }

    // TODO create check assert for all items (may be with contentDescription)
    class Assert(place: Place, @Color check: Int, enabled: Boolean) : BasicMatch() {
        init {
            onDisplay(R.id.color_recycler_view)

            onDisplayText(when (place) {
                Place.NOTE -> R.string.dialog_title_color
                Place.PREF -> R.string.title_note_color
            })

            onDisplayText(R.string.dialog_button_cancel)
            onDisplayText(R.string.dialog_button_accept)

            isEnabledText(R.string.dialog_button_accept, enabled)
        }
    }

    companion object {
        operator fun invoke(func: ColorDialogUi.() -> Unit, place: Place,
                            @Color check: Int, callback: Callback) =
                ColorDialogUi(place, check, callback).apply(func).apply { waitOpen { assert() } }
    }

}