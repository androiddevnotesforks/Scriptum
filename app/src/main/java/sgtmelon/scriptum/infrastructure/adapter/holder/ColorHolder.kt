package sgtmelon.scriptum.infrastructure.adapter.holder

import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import sgtmelon.extensions.getColorCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.extension.bindIndicatorColor
import sgtmelon.scriptum.databinding.ItemColorBinding
import sgtmelon.scriptum.infrastructure.adapter.animation.ColorAnimation
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.adapter.callback.click.ColorClickListener
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.utils.extensions.getRipple
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisibleIf

class ColorHolder(
    private val binding: ItemColorBinding
) : ParentHolder(binding.root),
    UnbindCallback {

    private val animation = ColorAnimation()

    fun bindColor(color: Color) = with(binding) {
        val colorItem = backgroundView.bindIndicatorColor(color)
        if (colorItem != null) {
            val contentColor = context.getColorCompat(colorItem.content)

            checkImage.setColorFilter(contentColor)

            val ripple = clickView.background as? RippleDrawable
            ripple?.setColor(ColorStateList.valueOf(colorItem.getRipple(context)))
        }

        val colorName = context.resources.getStringArray(R.array.pref_color)[color.ordinal]
        clickView.contentDescription = context.getString(R.string.description_item_color, colorName)
    }

    fun bindClick(
        checkArray: BooleanArray,
        check: Int,
        position: Int,
        callback: ColorClickListener,
        onUpdate: () -> Unit
    ) {
        binding.clickView.setOnClickListener {
            onClick(checkArray, check, position, callback, onUpdate)
        }
        binding.clickView.setOnLongClickListener {
            onClick(checkArray, check, position, callback, onUpdate)
            return@setOnLongClickListener true
        }
    }

    private inline fun onClick(
        checkArray: BooleanArray,
        check: Int,
        position: Int,
        callback: ColorClickListener,
        onUpdate: () -> Unit
    ) {
        callback.onColorClick(position)

        if (check != position) {
            onUpdate()
            checkArray[position] = true
            animation.startCheckFade(binding) { changeCheck(isVisible = true) }
        }
    }

    fun bindCheck(checkArray: BooleanArray, check: Int, position: Int) {
        if (checkArray[position]) {
            if (check != position) {
                checkArray[position] = false
                animation.startCheckFade(binding) { changeCheck(isVisible = false) }
            } else {
                changeCheck(isVisible = true)
            }
        } else {
            changeCheck(isVisible = false)
        }
    }

    private fun changeCheck(isVisible: Boolean) = binding.checkImage.makeVisibleIf(isVisible)

    override fun unbind() {
        binding.clickView.setOnClickListener(null)
    }
}