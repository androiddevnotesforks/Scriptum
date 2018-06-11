package sgtmelon.handynotes.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.interfaces.ItemClick;
import sgtmelon.handynotes.model.item.ItemRank;
import sgtmelon.handynotes.view.ButtonVisible;

public class AdapterRank extends RecyclerView.Adapter<AdapterRank.RankHolder> {

    //region Variables
    private final Context context;
    private final LayoutInflater inflater;

    private final List<ItemRank> listRank;

    private boolean[] iconStartAnim;
    //endregion

    public AdapterRank(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);

        listRank = new ArrayList<>();
    }

    public boolean[] getIconStartAnim() {
        return iconStartAnim;
    }

    private ItemClick.Click click;
    private ItemClick.LongClick longClick;
    private ItemClick.Drag drag;

    public void setCallback(ItemClick.Click click, ItemClick.LongClick longClick, ItemClick.Drag drag){
        this.click = click;
        this.longClick = longClick;
        this.drag = drag;
    }

    public void updateAdapter(List<ItemRank> listRank) {
        this.listRank.clear();
        this.listRank.addAll(listRank);

        iconStartAnim = new boolean[listRank.size()];
        Arrays.fill(iconStartAnim, false);
    }

    public void updateAdapter(List<ItemRank> listRank, boolean[] iconStartAnim) {
        this.listRank.clear();
        this.listRank.addAll(listRank);

        this.iconStartAnim = iconStartAnim;
    }

    public void updateAdapter(int position, ItemRank itemRank) {
        listRank.set(position, itemRank);
    }

    @NonNull
    @Override
    public RankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_rank, parent, false);
        return new RankHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankHolder holder, int position) {
        ItemRank itemRank = listRank.get(position);

        holder.rkName.setText(itemRank.getName());

        String textCount = context.getString(R.string.list_item_rank_text_count) + " " + itemRank.getTextCount();
        String rollCount = context.getString(R.string.list_item_rank_roll_count) + " " + itemRank.getRollCount();
        String rollCheck = context.getString(R.string.list_item_rank_roll_check) + " " + itemRank.getRollCheck() + "%";

        holder.rkTextCount.setText(textCount);
        holder.rkRollCount.setText(rollCount);

        if (itemRank.getRollCount() == 0) {
            holder.rkRollCheck.setVisibility(View.GONE);
        } else {
            holder.rkRollCheck.setVisibility(View.VISIBLE);
            holder.rkRollCheck.setText(rollCheck);
        }

        holder.rkVisible.setVisible(itemRank.isVisible(), iconStartAnim[position]);

        if (iconStartAnim[position]) iconStartAnim[position] = false;
    }

    @Override
    public int getItemCount() {
        return listRank.size();
    }

    class RankHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {

        private final View rkClick;
        private final TextView rkName, rkTextCount, rkRollCount, rkRollCheck;
        private final ButtonVisible rkVisible;
        private final ImageButton rkCancel;

        RankHolder(View itemView) {
            super(itemView);

            rkClick = itemView.findViewById(R.id.itemRank_ll_click);

            rkClick.setOnTouchListener(this);
            rkClick.setOnClickListener(this);

            rkName = itemView.findViewById(R.id.itemRank_tv_name);
            rkTextCount = itemView.findViewById(R.id.itemRank_tv_textCount);
            rkRollCount = itemView.findViewById(R.id.itemRank_tv_rollCount);
            rkRollCheck = itemView.findViewById(R.id.itemRank_tv_rollDone);

            rkVisible = itemView.findViewById(R.id.itemRank_bv_visible);
            rkCancel = itemView.findViewById(R.id.itemRank_ib_cancel);

            rkVisible.setOnTouchListener(this);
            rkVisible.setOnClickListener(this);
            rkVisible.setOnLongClickListener(this);

            rkCancel.setOnTouchListener(this);
            rkCancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int p = getAdapterPosition();

            switch (view.getId()) {
                case R.id.itemRank_bv_visible:
                    rkVisible.setVisible(!listRank.get(p).isVisible(), true);
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
                        drag.setDrag(true);
                    }
                    break;
                case R.id.itemRank_bv_visible:
                case R.id.itemRank_ib_cancel:
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        drag.setDrag(false);
                    }
                    break;
            }
            return false;
        }
    }
}
