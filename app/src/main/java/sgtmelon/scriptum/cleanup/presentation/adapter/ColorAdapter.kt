package sgtmelon.scriptum.cleanup.presentation.adapter

import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.adapter.callback.click.ColorClickListener
import sgtmelon.scriptum.infrastructure.adapter.holder.ColorHolder
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.utils.inflateBinding

/**
 * Adapter which displays list of [Color]'s.
 */
class ColorAdapter(
    private val callback: ColorClickListener,
    private var check: Int
) : ParentAdapter<Color, ColorHolder>() {

    private val visibleArray: BooleanArray

    init {
        setList(Color.values().toList())

        visibleArray = BooleanArray(itemCount)
        visibleArray.fill(element = false)
        visibleArray[check] = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorHolder {
        return ColorHolder(parent.inflateBinding(R.layout.item_color))
    }

    override fun onBindViewHolder(holder: ColorHolder, position: Int) {
        val item = getItem(position) ?: return

        holder.bindColor(item)
        holder.bindClick(visibleArray, check, position, callback) { unselectColor(position) }
        holder.bindCheck(visibleArray, check, position)
    }

    private fun unselectColor(position: Int) {
        val updatePosition = check
        check = position
        notifyItemChanged(updatePosition)
    }
}