package sgtmelon.scriptum.app.adapter;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.iconanim.library.SwitchButton;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.model.item.RankItem;
import sgtmelon.scriptum.app.view.fragment.RankFragment;
import sgtmelon.scriptum.databinding.ItemRankBinding;

/**
 * Адаптер для {@link RankFragment}
 */
public final class RankAdapter extends ParentAdapter<RankItem, RankAdapter.RankHolder> {

    private boolean[] startAnim;

    public RankAdapter(Context context) {
        super(context);
    }

    public boolean[] getStartAnim() {
        return startAnim;
    }

    @Override
    public void setList(List<RankItem> list) {
        super.setList(list);

        startAnim = new boolean[list.size()];
        Arrays.fill(startAnim, false);
    }

    @NonNull
    @Override
    public RankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRankBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.item_rank, parent, false
        );
        return new RankHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RankHolder holder, int position) {
        RankItem item = list.get(position);

        holder.bind(item);

        holder.rkVisible.setDrawable(item.isVisible(), startAnim[position]);
        if (startAnim[position]) {
            startAnim[position] = false;
        }
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

        void bind(RankItem rankItem) {
            binding.setRankItem(rankItem);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            int p = getAdapterPosition();

            switch (view.getId()) {
                case R.id.visible_button:
                    rkVisible.setDrawable(!list.get(p).isVisible(), true);
                    clickListener.onItemClick(view, p);
                    break;
                case R.id.click_container:
                    clickListener.onItemClick(view, p);
                    break;
                case R.id.cancel_button:
                    clickListener.onItemClick(view, p);
                    break;
            }
        }

        @Override
        public boolean onLongClick(View view) {
            longClickListener.onItemLongClick(view, getAdapterPosition());
            return true;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.click_container:
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        dragListener.setItemDrag(true);
                    }
                    break;
                case R.id.visible_button:
                case R.id.cancel_button:
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        dragListener.setItemDrag(false);
                    }
                    break;
            }
            return false;
        }

    }

}
