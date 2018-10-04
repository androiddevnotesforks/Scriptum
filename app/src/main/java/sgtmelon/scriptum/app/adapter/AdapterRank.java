package sgtmelon.scriptum.app.adapter;

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
import sgtmelon.iconanim.app.SwitchButton;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.model.item.ItemRank;
import sgtmelon.scriptum.databinding.ItemRankBinding;
import sgtmelon.scriptum.office.intf.IntfItem;

public final class AdapterRank extends RecyclerView.Adapter<AdapterRank.RankHolder> {

    private final List<ItemRank> listRank = new ArrayList<>();
    private boolean[] startAnim;

    private IntfItem.Click click;
    private IntfItem.LongClick longClick;
    private IntfItem.Drag drag;

    public boolean[] getStartAnim() {
        return startAnim;
    }

    public void setClick(IntfItem.Click click) {
        this.click = click;
    }

    public void setLongClick(IntfItem.LongClick longClick) {
        this.longClick = longClick;
    }

    public void setDrag(IntfItem.Drag drag) {
        this.drag = drag;
    }

    public void setListRank(List<ItemRank> listRank) {
        this.listRank.clear();
        this.listRank.addAll(listRank);

        startAnim = new boolean[listRank.size()];
        Arrays.fill(startAnim, false);
    }

    public void setListRank(List<ItemRank> listRank, boolean[] startAnim) {
        this.listRank.clear();
        this.listRank.addAll(listRank);

        this.startAnim = startAnim;
    }

    public void setListRank(int position, ItemRank itemRank) {
        listRank.set(position, itemRank);
    }

    @NonNull
    @Override
    public RankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ItemRankBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.item_rank, parent, false
        );
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

    final class RankHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnTouchListener, View.OnLongClickListener {

        private final ItemRankBinding binding;

        private final View rkClick;

        private final SwitchButton rkVisible;
        private final ImageButton rkCancel;

        RankHolder(ItemRankBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

            rkClick = itemView.findViewById(R.id.click_container);

            rkClick.setOnTouchListener(this);
            rkClick.setOnClickListener(this);

            rkVisible = itemView.findViewById(R.id.visible_button);

            rkVisible.setOnTouchListener(this);
            rkVisible.setOnClickListener(this);
            rkVisible.setOnLongClickListener(this);

            rkCancel = itemView.findViewById(R.id.cancel_button);

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
                case R.id.visible_button:
                    rkVisible.setDrawable(!listRank.get(p).isVisible(), true);
                    click.onItemClick(view, p);
                    break;
                case R.id.click_container:
                    click.onItemClick(view, p);
                    break;
                case R.id.cancel_button:
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
                case R.id.click_container:
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        drag.setItemDrag(true);
                    }
                    break;
                case R.id.visible_button:
                case R.id.cancel_button:
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        drag.setItemDrag(false);
                    }
                    break;
            }
            return false;
        }

    }

}
