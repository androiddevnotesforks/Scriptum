package sgtmelon.scriptum.adapter

import android.content.Context
import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.holder.ColorHolder
import sgtmelon.scriptum.dialog.ColorDialog
import sgtmelon.scriptum.model.data.ColorData
import sgtmelon.scriptum.model.item.ColorItem
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.Preference

/**
 * Адаптер списка цветов приложения для [ColorDialog]
 *
 * @author SerjantArbuz
 */
class ColorAdapter(context: Context, private val clickListener: ItemListener.ClickListener)
    : ParentAdapter<ColorItem, ColorHolder>(context) {

    private val visibleArray: BooleanArray
    private var check: Int = 0

    init {
        setList(ColorData.getColorList(Preference(context).theme))
        visibleArray = BooleanArray(itemCount)
    }

    fun setCheck(check: Int) {
        this.check = check

        visibleArray.fill(element = false)
        visibleArray[check] = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ColorHolder(inflater.inflate(R.layout.item_color, parent, false))

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