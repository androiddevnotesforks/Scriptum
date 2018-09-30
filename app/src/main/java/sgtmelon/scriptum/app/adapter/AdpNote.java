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
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.model.item.ItemRoll;
import sgtmelon.scriptum.app.model.repo.RepoNote;
import sgtmelon.scriptum.databinding.ItemNoteRollBinding;
import sgtmelon.scriptum.databinding.ItemNoteTextBinding;
import sgtmelon.scriptum.office.annot.def.db.DefType;
import sgtmelon.scriptum.office.intf.IntfItem;

public class AdpNote extends RecyclerView.Adapter<AdpNote.NoteHolder> {

    private final List<RepoNote> listRepo = new ArrayList<>();

    private IntfItem.Click click;
    private IntfItem.LongClick longClick;

    public void setClick(IntfItem.Click click) {
        this.click = click;
    }

    public void setLongClick(IntfItem.LongClick longClick) {
        this.longClick = longClick;
    }

    public void setListRepo(List<RepoNote> listRepoNote) {
        this.listRepo.clear();
        this.listRepo.addAll(listRepoNote);
    }

    @Override
    public int getItemViewType(int position) {
        return listRepo.get(position).getItemNote().getType();
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == DefType.text) {
            ItemNoteTextBinding bindingText = DataBindingUtil.inflate(inflater, R.layout.item_note_text, parent, false);
            return new NoteHolder(bindingText);
        } else {
            ItemNoteRollBinding bindingRoll = DataBindingUtil.inflate(inflater, R.layout.item_note_roll, parent, false);
            return new NoteHolder(bindingRoll);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        RepoNote repoNote = listRepo.get(position);
        holder.bind(repoNote.getItemNote(), repoNote.getListRoll());
    }

    @Override
    public int getItemCount() {
        return listRepo.size();
    }

    class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final ItemNoteTextBinding bindingText;
        private final ItemNoteRollBinding bindingRoll;

        private final View ntClick;

        NoteHolder(ItemNoteTextBinding bindingText) {
            super(bindingText.getRoot());

            this.bindingText = bindingText;
            bindingRoll = null;

            ntClick = itemView.findViewById(R.id.itemNote_fl_click);

            ntClick.setOnClickListener(this);
            ntClick.setOnLongClickListener(this);
        }

        NoteHolder(ItemNoteRollBinding bindingRoll) {
            super(bindingRoll.getRoot());

            this.bindingRoll = bindingRoll;
            bindingText = null;

            ntClick = itemView.findViewById(R.id.itemNote_fl_click);

            ntClick.setOnClickListener(this);
            ntClick.setOnLongClickListener(this);
        }

        void bind(ItemNote itemNote, List<ItemRoll> listRoll) {
            if (itemNote.getType() == DefType.text) {
                bindingText.setItemNote(itemNote);

                bindingText.executePendingBindings();
            } else {
                bindingRoll.setItemNote(itemNote);
                bindingRoll.setListRoll(listRoll);

                bindingRoll.executePendingBindings();
            }
        }

        @Override
        public void onClick(View view) {
            click.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            longClick.onItemLongClick(view, getAdapterPosition());
            return false;
        }

    }

}
