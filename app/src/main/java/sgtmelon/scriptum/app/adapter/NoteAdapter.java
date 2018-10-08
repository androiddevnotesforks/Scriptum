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
import sgtmelon.scriptum.app.model.NoteModel;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.databinding.ItemNoteRollBinding;
import sgtmelon.scriptum.databinding.ItemNoteTextBinding;
import sgtmelon.scriptum.office.annot.def.db.TypeDef;
import sgtmelon.scriptum.office.intf.ItemIntf;

public final class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private final List<NoteModel> listNoteModel = new ArrayList<>();

    private ItemIntf.ClickListener clickListener;
    private ItemIntf.LongClickListener longClickListener;

    public void setClickListener(ItemIntf.ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setLongClickListener(ItemIntf.LongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setListNoteModel(List<NoteModel> listNoteModel) {
        this.listNoteModel.clear();
        this.listNoteModel.addAll(listNoteModel);
    }

    @Override
    public int getItemViewType(int position) {
        return listNoteModel.get(position).getNoteItem().getType();
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TypeDef.text) {
            ItemNoteTextBinding bindingText = DataBindingUtil.inflate(
                    inflater, R.layout.item_note_text, parent, false
            );
            return new NoteHolder(bindingText);
        } else {
            ItemNoteRollBinding bindingRoll = DataBindingUtil.inflate(
                    inflater, R.layout.item_note_roll, parent, false
            );
            return new NoteHolder(bindingRoll);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        NoteModel noteModel = listNoteModel.get(position);
        holder.bind(noteModel.getNoteItem(), noteModel.getListRoll());
    }

    @Override
    public int getItemCount() {
        return listNoteModel.size();
    }

    final class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private final ItemNoteTextBinding bindingText;
        private final ItemNoteRollBinding bindingRoll;

        private final View ntClick;

        NoteHolder(ItemNoteTextBinding bindingText) {
            super(bindingText.getRoot());

            this.bindingText = bindingText;
            bindingRoll = null;

            ntClick = itemView.findViewById(R.id.click_container);

            ntClick.setOnClickListener(this);
            ntClick.setOnLongClickListener(this);
        }

        NoteHolder(ItemNoteRollBinding bindingRoll) {
            super(bindingRoll.getRoot());

            this.bindingRoll = bindingRoll;
            bindingText = null;

            ntClick = itemView.findViewById(R.id.click_container);

            ntClick.setOnClickListener(this);
            ntClick.setOnLongClickListener(this);
        }

        void bind(NoteItem noteItem, List<RollItem> listRoll) {
            if (noteItem.getType() == TypeDef.text) {
                bindingText.setNoteItem(noteItem);

                bindingText.executePendingBindings();
            } else {
                bindingRoll.setNoteItem(noteItem);
                bindingRoll.setListRoll(listRoll);

                bindingRoll.executePendingBindings();
            }
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            longClickListener.onItemLongClick(view, getAdapterPosition());
            return false;
        }

    }

}
