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
abstract class ParentAdapter<E, VH : RecyclerView.ViewHolder> protected constructor(
        protected val context: Context, protected val clickListener: ItemIntf.ClickListener
) : RecyclerView.Adapter<VH>() {

    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    protected val list: MutableList<E> = ArrayList()

    @CallSuper
    open fun setList(list: List<E>) {
        this.list.clear()
        this.list.addAll(list)
    }

    @CallSuper
    open fun setListItem(position: Int, item: E) {
        list[position] = item
    }

    override fun getItemCount(): Int {
        return list.size
    }

}