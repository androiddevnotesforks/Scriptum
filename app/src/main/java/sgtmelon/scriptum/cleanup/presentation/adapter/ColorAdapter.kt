package sgtmelon.scriptum.cleanup.presentation.adapter

import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.ColorHolder
import sgtmelon.scriptum.cleanup.presentation.dialog.ColorDialog
import sgtmelon.scriptum.cleanup.presentation.listener.ItemListener
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.infrastructure.utils.inflateView

/**
 * Adapter which displays list of application colors for [ColorDialog].
 */
class ColorAdapter(
    private val clickListener: ItemListener.Click
) : ParentAdapter<Color, ColorHolder>() {

    private val visibleArray: BooleanArray
    private var check: Int = 0

    init {
        setList(Color.values().toList())
        visibleArray = BooleanArray(itemCount)
    }

    fun setCheck(check: Int) = apply {
        this.check = check

        visibleArray.fill(element = false)
        visibleArray[check] = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorHolder {
        return ColorHolder(parent.inflateView(R.layout.item_color))
    }

    override fun onBindViewHolder(holder: ColorHolder, position: Int) {
        holder.bindColor(list[position])

        holder.clickView.setOnClickListener { v ->
            clickListener.onItemClick(v, position)

            if (check != position) {
                notifyItemChangedOld(position)

                visibleArray[check] = true
                holder.animateCheckShow()
            }
        }

        if (visibleArray[position]) {
            if (check != position) {
                visibleArray[position] = false
                holder.animateCheckHide()
            } else {
                holder.checkShow()
            }
        } else {
            holder.checkHide()
        }
    }

    private fun notifyItemChangedOld(position: Int) {
        val positionOld = check
        check = position
        notifyItemChanged(positionOld)
    }
}