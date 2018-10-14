package sgtmelon.scriptum.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.model.NoteModel;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.view.fragment.BinFragment;
import sgtmelon.scriptum.app.view.fragment.NotesFragment;
import sgtmelon.scriptum.databinding.ItemNoteRollBinding;
import sgtmelon.scriptum.databinding.ItemNoteTextBinding;
import sgtmelon.scriptum.office.annot.def.db.TypeDef;

/**
 * Адаптер для {@link NotesFragment}, {@link BinFragment}
 */
public final class NoteAdapter extends ParentAdapter<NoteModel, NoteAdapter.NoteHolder> {

    public NoteAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        NoteModel noteModel = list.get(position);
        holder.bind(noteModel.getNoteItem(), noteModel.getListRoll());
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getNoteItem().getType();
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
