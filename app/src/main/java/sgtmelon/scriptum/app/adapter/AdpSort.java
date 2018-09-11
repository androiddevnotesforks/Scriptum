package sgtmelon.scriptum.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.model.item.ItemSort;
import sgtmelon.scriptum.databinding.ItemSortBinding;
import sgtmelon.scriptum.office.intf.IntfItem;
import sgtmelon.scriptum.office.st.StSort;


public class AdpSort extends RecyclerView.Adapter<AdpSort.SortHolder> {

    private final List<ItemSort> listSort;
    public final StSort stSort;

    public AdpSort() {
        listSort = new ArrayList<>();
        stSort = new StSort();
    }

    private IntfItem.Click click;

    public void setCallback(IntfItem.Click click) {
        this.click = click;
    }

    public void update(List<ItemSort> listSort) {
        this.listSort.clear();
        this.listSort.addAll(listSort);

        stSort.updateEnd(listSort);
    }

    public void update(int position, ItemSort itemSort) {
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
        holder.bind(listSort.get(position), position, stSort.getEnd());
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
            if (p == stSort.getEnd()) click.onItemClick(view, p);
        }

    }
}