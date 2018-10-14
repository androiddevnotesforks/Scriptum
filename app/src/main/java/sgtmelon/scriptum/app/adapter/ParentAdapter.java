package sgtmelon.scriptum.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.scriptum.office.intf.ItemIntf;

/**
 * Абстрактный класс адаптера, с часто повторяющимся функционалом
 *
 * @param <E>  - Модель списка
 * @param <VH> - Холдер для модели
 */
public abstract class ParentAdapter<E, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected final Context context;
    protected final LayoutInflater inflater;

    protected List<E> list;

    ItemIntf.ClickListener clickListener;
    ItemIntf.LongClickListener longClickListener;
    ItemIntf.DragListener dragListener;
    ItemIntf.Watcher watcher;

    ParentAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);

        list = new ArrayList<>();
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public void setListItem(int position, E item) {
        list.set(position, item);
    }

    public void setClickListener(ItemIntf.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setLongClickListener(ItemIntf.LongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setDragListener(ItemIntf.DragListener dragListener) {
        this.dragListener = dragListener;
    }

    public void setWatcher(ItemIntf.Watcher watcher) {
        this.watcher = watcher;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
