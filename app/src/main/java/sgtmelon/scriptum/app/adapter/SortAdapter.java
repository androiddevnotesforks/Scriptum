package sgtmelon.scriptum.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.model.item.SortItem;
import sgtmelon.scriptum.databinding.ItemSortBinding;
import sgtmelon.scriptum.element.SortDialog;
import sgtmelon.scriptum.office.st.SortSt;

/**
 * Адаптер для {@link SortDialog}
 */
public final class SortAdapter extends ParentAdapter<SortItem, SortAdapter.SortHolder> {

    public final SortSt sortSt = new SortSt();

    public SortAdapter(Context context) {
        super(context);
    }

    @Override
    public void setList(List<SortItem> list) {
        super.setList(list);
        sortSt.updateEnd(list);
    }

    @NonNull
    @Override
    public SortHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemSortBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.item_sort, parent, false
        );
        return new SortHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SortHolder holder, int position) {
        final SortItem item = list.get(position);
        holder.bind(item, position, sortSt.getEnd());
    }

    final class SortHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ItemSortBinding binding;
        private final View clickView;

        SortHolder(ItemSortBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

            clickView = itemView.findViewById(R.id.click_container);
            clickView.setOnClickListener(this);
        }

        void bind(SortItem sortItem, int position, int sortEnd) {
            binding.setSortItem(sortItem);
            binding.setPosition(position);
            binding.setSortEnd(sortEnd);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            if (position == sortSt.getEnd()) {
                clickListener.onItemClick(v, position);
            }
        }

    }

}