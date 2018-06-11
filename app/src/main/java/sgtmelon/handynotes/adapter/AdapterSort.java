package sgtmelon.handynotes.adapter;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.model.state.StateSort;
import sgtmelon.handynotes.interfaces.ItemClick;
import sgtmelon.handynotes.model.item.ItemSort;

public class AdapterSort extends RecyclerView.Adapter<AdapterSort.SortHolder> {

    //region Variables
    private final LayoutInflater inflater;

    private final List<ItemSort> listSort;
    public final StateSort stateSort;

    @ColorInt
    private final int colorDark, colorDarkSecond;
    //endregion

    public AdapterSort(Context context) {
        this.inflater = LayoutInflater.from(context);

        listSort = new ArrayList<>();
        stateSort = new StateSort();

        colorDark = ContextCompat.getColor(context, R.color.colorDark);
        colorDarkSecond = ContextCompat.getColor(context, R.color.colorDarkSecond);
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
        View view = inflater.inflate(R.layout.item_sort, parent, false);
        return new SortHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SortHolder holder, int position) {
        ItemSort itemSort = listSort.get(position);

        int sortEnd = stateSort.getEnd();

        holder.srChevronLeft.setVisibility(position == sortEnd ? View.VISIBLE : View.GONE);
        holder.srChevronRight.setVisibility(position == sortEnd ? View.VISIBLE : View.GONE);

        holder.srText.setText(itemSort.getText());
        holder.srText.setTextColor(position <= sortEnd ? colorDark : colorDarkSecond);
    }

    @Override
    public int getItemCount() {
        return listSort.size();
    }

    class SortHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final View srClick;
        private final TextView srText;
        private final ImageView srChevronLeft, srChevronRight;

        SortHolder(View itemView) {
            super(itemView);

            srClick = itemView.findViewById(R.id.itemSort_ll_click);
            srText = itemView.findViewById(R.id.itemSort_tv_text);
            srChevronLeft = itemView.findViewById(R.id.itemSort_iv_left);
            srChevronRight = itemView.findViewById(R.id.itemSort_iv_right);

            srClick.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int p = getAdapterPosition();
            if (p == stateSort.getEnd()) click.onItemClick(view, p);
        }

    }
}