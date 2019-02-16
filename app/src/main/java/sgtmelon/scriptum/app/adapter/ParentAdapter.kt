package sgtmelon.scriptum.app.adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.office.intf.ItemIntf
import java.util.*

/**
 * Абстрактный класс адаптера, с часто повторяющимся функционалом
 *
 * @param <E>  - модель списка
 * @param <VH> - холдер для модели
 */
abstract class ParentAdapter<T, VH : RecyclerView.ViewHolder> protected constructor(
        protected val context: Context, protected val clickListener: ItemIntf.ClickListener
) : RecyclerView.Adapter<VH>() {

    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    protected val list: MutableList<T> = ArrayList()

    @CallSuper
    open fun setList(list: List<T>) {
        this.list.clear()
        this.list.addAll(list)
    }

    @CallSuper
    open fun setListItem(position: Int, item: T) {
        list[position] = item
    }

    override fun getItemCount(): Int {
        return list.size
    }

}