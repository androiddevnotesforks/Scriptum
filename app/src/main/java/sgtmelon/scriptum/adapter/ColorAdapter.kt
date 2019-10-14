package sgtmelon.scriptum.adapter

import android.content.Context
import android.view.ViewGroup
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.holder.ColorHolder
import sgtmelon.scriptum.dialog.ColorDialog
import sgtmelon.scriptum.extension.inflateView
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.repository.preference.PreferenceRepo

/**
 * Adapter which displays list of application colors for [ColorDialog]
 */
class ColorAdapter(context: Context, private val clickListener: ItemListener.Click)
    : ParentAdapter<Int, ColorHolder>() {

    // TODO #RELEASE remove iPreferenceRepo

    private val theme = PreferenceRepo(context).theme

    private val visibleArray: BooleanArray
    private var check: Int = 0

    init {
        setList(Color.list)
        visibleArray = BooleanArray(itemCount)
    }

    fun setCheck(check: Int) = apply {
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