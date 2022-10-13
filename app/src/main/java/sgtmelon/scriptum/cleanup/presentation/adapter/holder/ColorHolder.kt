package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import android.view.animation.AccelerateDecelerateInterpolator
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sgtmelon.extensions.getColorCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.extension.bindIndicatorColor
import sgtmelon.scriptum.databinding.ItemColorBinding
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.adapter.callback.click.ColorClickListener
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.utils.makeVisibleIf
import sgtmelon.test.idling.addIdlingListener

class ColorHolder(
    private val binding: ItemColorBinding
) : ParentHolder(binding.root),
    UnbindCallback {

    fun bindColor(color: Color) = with(binding) {
        val colorItem = backgroundView.bindIndicatorColor(color)
        if (colorItem != null) {
            checkImage.setColorFilter(context.getColorCompat(colorItem.content))
        }

        val colorName = context.resources.getStringArray(R.array.pref_note_color)[color.ordinal]
        clickView.contentDescription = context.getString(R.string.description_item_color, colorName)
    }

    fun bindClick(
        visibleArray: BooleanArray,
        check: Int,
        position: Int,
        callback: ColorClickListener,
        onUpdate: () -> Unit
    ) {
        binding.clickView.setOnClickListener {
            callback.onColorClick(position)

            if (check != position) {
                onUpdate()
                visibleArray[check] = true
                prepareAnimation { changeCheck(isVisible = true) }
            }
        }
    }

    fun bindCheck(visibleArray: BooleanArray, check: Int, position: Int) {
        if (visibleArray[position]) {
            if (check != position) {
                visibleArray[position] = false
                prepareAnimation { changeCheck(isVisible = false) }
            } else {
                changeCheck(isVisible = true)
            }
        } else {
            changeCheck(isVisible = false)
        }
    }

    private fun changeCheck(isVisible: Boolean) = binding.checkImage.makeVisibleIf(isVisible)

    private inline fun prepareAnimation(changeFunc: () -> Unit) {
        val transition = Fade()
            .setDuration(context.resources.getInteger(R.integer.color_fade_time).toLong())
            .setInterpolator(AccelerateDecelerateInterpolator())
            .addTarget(binding.checkImage)
            .addIdlingListener()

        TransitionManager.beginDelayedTransition(binding.parentContainer, transition)

        changeFunc()
    }

    override fun unbind() {
        binding.clickView.setOnClickListener(null)
    }
}