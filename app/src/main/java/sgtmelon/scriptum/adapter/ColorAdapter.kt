package sgtmelon.scriptum.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.holder.ColorHolder
import sgtmelon.scriptum.dialog.ColorDialog
import sgtmelon.scriptum.model.data.ColorData
import sgtmelon.scriptum.model.item.ColorItem
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.office.utils.Preference
import sgtmelon.scriptum.office.utils.getDimen

/**
 * Адаптер списка цветов приложения для [ColorDialog]
 *
 * @author SerjantArbuz
 */
class ColorAdapter(private val context: Context,
                   private val clickListener: ItemListener.ClickListener
) : RecyclerView.Adapter<ColorHolder>() {

    private val inflater = LayoutInflater.from(context)

    private val colorList: List<ColorItem> = ColorData.getColorList(Preference(context).theme)

    @Dimension private val strokeDimen = context.getDimen(value = 1f)

    private var check: Int = 0
    private var visible: BooleanArray? = null

    fun setCheck(check: Int) {
        this.check = check

        visible = BooleanArray(itemCount)
        visible!![check] = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorHolder {
        val view = inflater.inflate(R.layout.item_color, parent, false)
        return ColorHolder(view)
    }

    override fun onBindViewHolder(holder: ColorHolder, position: Int) {
        val colorItem = colorList[position]

        val fillColor = ContextCompat.getColor(context, colorItem.fill)
        val strokeColor = ContextCompat.getColor(context, colorItem.stroke)
        val checkColor = ContextCompat.getColor(context, colorItem.check)

        if (holder.backgroundView.background is GradientDrawable) {
            val drawable = holder.backgroundView.background as GradientDrawable
            drawable.setColor(fillColor)
            drawable.setStroke(strokeDimen, strokeColor)
        }

        holder.clickView.setOnClickListener { v ->
            val oldCheck = check                   //Сохраняем старую позицию

            clickListener.onItemClick(v, position)

            if (oldCheck != position) {             //Если выбранный цвет не совпадает с тем, на который нажали
                check = position                   //Присваиваем новую позицию
                visible!![check] = true

                notifyItemChanged(oldCheck)        //Скрываем старую отметку
                holder.showCheck()
            }
        }

        holder.checkImage.setColorFilter(checkColor)

        if (visible!![position]) {                            //Если отметка видна
            if (this.check == position) {                   //Если текущая позиция совпадает с выбранным цветом
                holder.checkImage.visibility = View.VISIBLE
            } else {
                visible!![position] = false                  //Делаем отметку невидимой с анимацией
                holder.hideCheck()
            }
        } else {
            holder.checkImage.visibility = View.GONE
        }
    }

    override fun getItemCount() = colorList.size

}