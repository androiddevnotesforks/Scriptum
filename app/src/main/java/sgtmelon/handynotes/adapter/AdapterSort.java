package sgtmelon.handynotes.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.databinding.ItemSortBinding;
import sgtmelon.handynotes.model.state.StateSort;
import sgtmelon.handynotes.interfaces.ItemClick;
import sgtmelon.handynotes.model.item.ItemSort;

public class AdapterSort extends RecyclerView.Adapter<AdapterSort.SortHolder> {

    private final List<ItemSort> listSort;
    public final StateSort stateSort;

    public AdapterSort() {
        listSort = new ArrayList<>();
        stateSort = new StateSort();
    }

    private ItemClick.Click click;

    public void setCallback(ItemClick.Click click) {
        this.click = click;
    }

    public void updateAdapter(List<ItemSort> listSort) {
        this.listSort.clear();
        this.listSort.addAll(listSort);

        stateSort.updateEnd(listSort);
    }

    public void updateAdapter(int position, ItemSort itemSort) {
        listSort.set(position, itemSort);
    }

    @NonNull
    @Override
    public SortHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSortBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_sort, parent, false);

        return new SortHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SortHolder holder, int position) {
        holder.bind(listSort.get(position), position, stateSort.getEnd());
    }

    @Override
    public int getItemCount() {
        return listSort.size();
    }

    class SortHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final View srClick;

        private final ItemSortBinding binding;

        SortHolder(ItemSortBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

            srClick = itemView.findViewById(R.id.itemSort_ll_click);
            srClick.setOnClickListener(this);
        }

        void bind(ItemSort itemSort, int position, int sortEnd) {
            binding.setItemSort(itemSort);
            binding.setPosition(position);
            binding.setSortEnd(sortEnd);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            int p = getAdapterPosition();
            if (p == stateSort.getEnd()) click.onItemClick(view, p);
        }

    }
}