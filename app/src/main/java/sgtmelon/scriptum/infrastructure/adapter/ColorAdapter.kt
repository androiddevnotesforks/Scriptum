package sgtmelon.scriptum.infrastructure.adapter

import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.adapter.callback.click.ColorClickListener
import sgtmelon.scriptum.infrastructure.adapter.holder.ColorHolder
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentAdapter
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.utils.extensions.inflateBinding

/**
 * Adapter which displays list of [Color]'s.
 */
class ColorAdapter(
    private val callback: ColorClickListener,
    private var check: Int
) : ParentAdapter<Color, ColorHolder>() {

    private val checkArray: BooleanArray

    init {
        setList(Color.values().toList())

        checkArray = BooleanArray(itemCount)
        checkArray.fill(element = false)
        checkArray[check] = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorHolder {
        return ColorHolder(parent.inflateBinding(R.layout.item_color))
    }

    override fun onBindViewHolder(holder: ColorHolder, position: Int) {
        val item = getItem(position) ?: return

        holder.bindColor(item)
        holder.bindClick(checkArray, check, position, callback) { unselectColor(position) }
        holder.bindCheck(checkArray, check, position)
    }

    private fun unselectColor(position: Int) {
        val updatePosition = check
        check = position
        notifyItemChanged(updatePosition)
    }
}