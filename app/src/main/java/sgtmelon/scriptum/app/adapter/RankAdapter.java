package sgtmelon.scriptum.app.adapter;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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
    }

    @NonNull
    @Override
    public RankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemRankBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.item_rank, parent, false
        );
        return new RankHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RankHolder holder, int position) {
        final RankItem item = list.get(position);

        holder.bind(item);
        holder.visibleButton.setDrawable(item.isVisible(), startAnim[position]);

        if (startAnim[position]) {
            startAnim[position] = false;
        }
    }

    final class RankHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnTouchListener, View.OnLongClickListener {

        private final ItemRankBinding binding;

        private final View clickView;

        private final SwitchButton visibleButton;
        private final ImageButton cancelButton;

        RankHolder(ItemRankBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

            clickView = itemView.findViewById(R.id.click_container);

            clickView.setOnTouchListener(this);
            clickView.setOnClickListener(this);

            visibleButton = itemView.findViewById(R.id.visible_button);

            visibleButton.setOnTouchListener(this);
            visibleButton.setOnClickListener(this);
            visibleButton.setOnLongClickListener(this);

            cancelButton = itemView.findViewById(R.id.cancel_button);

            cancelButton.setOnTouchListener(this);
            cancelButton.setOnClickListener(this);
        }

        void bind(RankItem rankItem) {
            binding.setRankItem(rankItem);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            final int p = getAdapterPosition();

            switch (view.getId()) {
                case R.id.visible_button:
                    visibleButton.setDrawable(!list.get(p).isVisible(), true);
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
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                dragListener.setDrag(view.getId() == R.id.click_container);
            }
            return false;
        }

    }

}