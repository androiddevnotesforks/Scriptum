package sgtmelon.scriptum.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.model.item.ItemRoll;
import sgtmelon.scriptum.app.model.repo.RepoNote;
import sgtmelon.scriptum.app.selection.SelNoteDetails;
import sgtmelon.scriptum.app.selection.ViewHolderDetails;
import sgtmelon.scriptum.databinding.ItemNoteRollBinding;
import sgtmelon.scriptum.databinding.ItemNoteTextBinding;
import sgtmelon.scriptum.office.annot.def.db.DefType;
import sgtmelon.scriptum.office.intf.IntfItem;

public class AdpNote extends RecyclerView.Adapter<AdpNote.NoteHolder> {

    private final Context context;
    private final List<RepoNote> listRepoNote = new ArrayList<>();

    private boolean[] visible;

    public AdpNote(Context context) {
        this.context = context;
    }

    private IntfItem.Click click;

    public void setClick(IntfItem.Click click) {
        this.click = click;
    }

    private SelectionTracker<RepoNote> selectionTracker;

    public void setSelectionTracker(SelectionTracker<RepoNote> selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    public void update(List<RepoNote> listRepoNote) {
        this.listRepoNote.clear();
        this.listRepoNote.addAll(listRepoNote);

        visible = new boolean[getItemCount()];
        Arrays.fill(visible, false);
    }

    @Override
    public int getItemViewType(int position) {
        return listRepoNote.get(position).getItemNote().getType();
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
        RepoNote repoNote = listRepoNote.get(position);

        boolean isSelected = selectionTracker.isSelected(repoNote);

        if (!visible[position] && !isSelected) {
            holder.ntCheck.setVisibility(View.GONE);
        } else if (!visible[position] && isSelected) {
            visible[position] = true;
            holder.ntCheck.startAnimation(holder.alphaIn);
        } else if (visible[position] && !isSelected) {
            visible[position] = false;
            holder.ntCheck.startAnimation(holder.alphaOut);
        }

        holder.bind(repoNote.getItemNote(), repoNote.getListRoll());
    }

    @Override
    public int getItemCount() {
        return listRepoNote.size();
    }

    class NoteHolder extends RecyclerView.ViewHolder implements ViewHolderDetails, View.OnClickListener,
            Animation.AnimationListener, View.OnTouchListener {

        private final View ntClick, ntCheck;

        private final Animation alphaIn, alphaOut;

        private final ItemNoteTextBinding bindingText;
        private final ItemNoteRollBinding bindingRoll;

        NoteHolder(ItemNoteTextBinding bindingText) {
            super(bindingText.getRoot());

            this.bindingText = bindingText;
            bindingRoll = null;

            ntClick = itemView.findViewById(R.id.itemNote_fl_click);
            ntCheck = itemView.findViewById(R.id.itemNote_fl_check);

            ntClick.setOnTouchListener(this);
            ntClick.setOnClickListener(this);

            alphaIn = AnimationUtils.loadAnimation(context, R.anim.alpha_in);
            alphaOut = AnimationUtils.loadAnimation(context, R.anim.alpha_out);

            alphaIn.setAnimationListener(this);
            alphaOut.setAnimationListener(this);
        }

        NoteHolder(ItemNoteRollBinding bindingRoll) {
            super(bindingRoll.getRoot());

            this.bindingRoll = bindingRoll;
            bindingText = null;

            ntClick = itemView.findViewById(R.id.itemNote_fl_click);
            ntCheck = itemView.findViewById(R.id.itemNote_fl_check);

            ntClick.setOnTouchListener(this);
            ntClick.setOnClickListener(this);

            alphaIn = AnimationUtils.loadAnimation(context, R.anim.alpha_in);
            alphaOut = AnimationUtils.loadAnimation(context, R.anim.alpha_out);

            alphaIn.setAnimationListener(this);
            alphaOut.setAnimationListener(this);
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
        public SelNoteDetails getDetailsNote() {
            int position = getAdapterPosition();

            return new SelNoteDetails(position, listRepoNote.get(position));
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                return selectionTracker.hasSelection();
            }
            return false;
        }

        @Override
        public void onClick(View view) {
            if (!selectionTracker.hasSelection()) {
                click.onItemClick(view, getAdapterPosition());
            }
        }

        @Override
        public void onAnimationStart(Animation animation) {
            if (animation == alphaIn) {
                ntCheck.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (animation == alphaOut) {
                ntCheck.setVisibility(View.GONE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

}
