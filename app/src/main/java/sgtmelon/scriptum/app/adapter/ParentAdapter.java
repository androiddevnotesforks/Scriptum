package sgtmelon.scriptum.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.CallSuper;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.scriptum.office.intf.ItemIntf;

/**
 * Абстрактный класс адаптера, с часто повторяющимся функционалом
 *
 * @param <E>  - модель списка
 * @param <VH> - холдер для модели
 */
public abstract class ParentAdapter<E, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected final Context context;
    protected final LayoutInflater inflater;

    protected final List<E> list = new ArrayList<>();

    ItemIntf.ClickListener clickListener;
    ItemIntf.LongClickListener longClickListener;
    ItemIntf.DragListener dragListener;
    ItemIntf.RollWatcher rollWatcher;

    ParentAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @CallSuper
    public void setList(List<E> list) {
        this.list.clear();
        this.list.addAll(list);
    }

    @CallSuper
    public void setListItem(int position, E item) {
        list.set(position, item);
    }

    public final void setClickListener(ItemIntf.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public final void setLongClickListener(ItemIntf.LongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public final void setDragListener(ItemIntf.DragListener dragListener) {
        this.dragListener = dragListener;
    }

    public final void setRollWatcher(ItemIntf.RollWatcher rollWatcher) {
        this.rollWatcher = rollWatcher;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
