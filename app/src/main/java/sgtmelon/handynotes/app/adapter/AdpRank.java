package sgtmelon.handynotes.app.adapter;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.model.item.ItemRank;
import sgtmelon.handynotes.databinding.ItemRankBinding;
import sgtmelon.handynotes.element.button.BtnVisiblePreL;
import sgtmelon.handynotes.office.intf.IntfItem;

public class AdpRank extends RecyclerView.Adapter<AdpRank.RankHolder> {

    private final List<ItemRank> listRank;
    private boolean[] startAnim;

    public AdpRank() {
        listRank = new ArrayList<>();
    }

    public boolean[] getStartAnim() {
        return startAnim;
    }

    private IntfItem.Click click;
    private IntfItem.LongClick longClick;
    private IntfItem.Drag drag;

    public void setCallback(IntfItem.Click click, IntfItem.LongClick longClick, IntfItem.Drag drag) {
        this.click = click;
        this.longClick = longClick;
        this.drag = drag;
    }

    public void update(List<ItemRank> listRank) {
        this.listRank.clear();
        this.listRank.addAll(listRank);

        startAnim = new boolean[listRank.size()];
        Arrays.fill(startAnim, false);
    }

    public void update(List<ItemRank> listRank, boolean[] iconStartAnim) {
        this.listRank.clear();
        this.listRank.addAll(listRank);

        this.startAnim = iconStartAnim;
    }

    public void update(int position, ItemRank itemRank) {
        listRank.set(position, itemRank);
    }

    @NonNull
    @Override
    public RankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRankBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_rank, parent, false);

        return new RankHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RankHolder holder, int position) {
        ItemRank itemRank = listRank.get(position);

        holder.bind(listRank.get(position));

        holder.rkVisible.setDrawable(itemRank.isVisible(), startAnim[position]);
        if (startAnim[position]) startAnim[position] = false;
    }

    @Override
    public int getItemCount() {
        return listRank.size();
    }

    class RankHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {

        private final View rkClick;

        private final BtnVisiblePreL rkVisible;
        private final ImageButton rkCancel;

        private final ItemRankBinding binding;

        RankHolder(ItemRankBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

            rkClick = itemView.findViewById(R.id.itemRank_ll_click);

            rkClick.setOnTouchListener(this);
            rkClick.setOnClickListener(this);

            rkVisible = itemView.findViewById(R.id.itemRank_bv_visible);

            rkVisible.setOnTouchListener(this);
            rkVisible.setOnClickListener(this);
            rkVisible.setOnLongClickListener(this);

            rkCancel = itemView.findViewById(R.id.itemRank_ib_cancel);

            rkCancel.setOnTouchListener(this);
            rkCancel.setOnClickListener(this);
        }

        void bind(ItemRank itemRank) {
            binding.setItemRank(itemRank);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            int p = getAdapterPosition();

            switch (view.getId()) {
                case R.id.itemRank_bv_visible:
                    rkVisible.setDrawable(!listRank.get(p).isVisible(), true);
                    click.onItemClick(view, p);
                    break;
                case R.id.itemRank_ll_click:
                    click.onItemClick(view, p);
                    break;
                case R.id.itemRank_ib_cancel:
                    click.onItemClick(view, p);
                    break;
            }
        }

        @Override
        public boolean onLongClick(View view) {
            longClick.onItemLongClick(view, getAdapterPosition());
            return true;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.itemRank_ll_click:
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        drag.setItemDrag(true);
                    }
                    break;
                case R.id.itemRank_bv_visible:
                case R.id.itemRank_ib_cancel:
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        drag.setItemDrag(false);
                    }
                    break;
            }
            return false;
        }
    }
}
