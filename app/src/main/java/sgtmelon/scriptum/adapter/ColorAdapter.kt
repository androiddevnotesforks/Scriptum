package sgtmelon.scriptum.adapter

import android.content.Context
import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.holder.ColorHolder
import sgtmelon.scriptum.dialog.ColorDialog
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.inflateView
import sgtmelon.scriptum.repository.Preference

/**
 * Адаптер списка цветов приложения для [ColorDialog]
 *
 * @author SerjantArbuz
 */
class ColorAdapter(context: Context, private val clickListener: ItemListener.Click)
    : ParentAdapter<Int, ColorHolder>() {

    // TODO !! убрать preference

    private val theme = Preference(context).theme

    private val visibleArray: BooleanArray
    private var check: Int = 0

    init {
        setList(Color.list)
        visibleArray = BooleanArray(itemCount)
    }

    fun setCheck(check: Int) {
        this.check = check

        visibleArray.fill(element = false)
        visibleArray[check] = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ColorHolder(parent.inflateView(R.layout.item_color))

    override fun onBindViewHolder(holder: ColorHolder, position: Int) {
        holder.bindColor(theme, list[position])

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