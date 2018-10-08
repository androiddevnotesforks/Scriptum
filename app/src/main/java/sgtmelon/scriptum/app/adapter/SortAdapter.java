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
import sgtmelon.scriptum.app.model.item.SortItem;
import sgtmelon.scriptum.databinding.ItemSortBinding;
import sgtmelon.scriptum.office.intf.ItemIntf;
import sgtmelon.scriptum.office.st.SortSt;


public final class SortAdapter extends RecyclerView.Adapter<SortAdapter.SortHolder> {

    public final SortSt sortSt = new SortSt();

    private final List<SortItem> listSort = new ArrayList<>();

    private ItemIntf.ClickListener clickListener;

    public void setClickListener(ItemIntf.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setListSort(List<SortItem> listSort) {
        this.listSort.clear();
        this.listSort.addAll(listSort);

        sortSt.updateEnd(listSort);
    }

    public void setListSort(int position, SortItem sortItem) {
        listSort.set(position, sortItem);
    }

    @NonNull
    @Override
    public SortHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ItemSortBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.item_sort, parent, false
        );
        return new SortHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SortHolder holder, int position) {
        holder.bind(listSort.get(position), position, sortSt.getEnd());
    }

    @Override
    public int getItemCount() {
        return listSort.size();
    }

    final class SortHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ItemSortBinding binding;

        private final View srClick;

        SortHolder(ItemSortBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

            srClick = itemView.findViewById(R.id.click_container);
            srClick.setOnClickListener(this);
        }

        void bind(SortItem sortItem, int position, int sortEnd) {
            binding.setSortItem(sortItem);
            binding.setPosition(position);
            binding.setSortEnd(sortEnd);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            int p = getAdapterPosition();
            if (p == sortSt.getEnd()) clickListener.onItemClick(view, p);
        }

    }

}