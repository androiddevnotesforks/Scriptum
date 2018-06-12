package sgtmelon.handynotes.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.databinding.ItemNoteRollBinding;
import sgtmelon.handynotes.databinding.ItemNoteTextBinding;
import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.model.item.ItemRoll;
import sgtmelon.handynotes.db.DbDesc;
import sgtmelon.handynotes.Help;
import sgtmelon.handynotes.interfaces.ItemClick;
import sgtmelon.handynotes.model.manager.ManagerRoll;

public class AdapterNote extends RecyclerView.Adapter<AdapterNote.NoteHolder> {

    //region Variables
    private final Context context;

    private final List<ItemNote> listNote;
        //endregion

    public AdapterNote(Context context) {
        this.context = context;

        listNote = new ArrayList<>();
    }

    private ItemClick.Click click;
    private ItemClick.LongClick longClick;

    public void setCallback(ItemClick.Click click, ItemClick.LongClick longClick) {
        this.click = click;
        this.longClick = longClick;
    }

    private ManagerRoll managerRoll;

    public void setManagerRoll(ManagerRoll managerRoll) {
        this.managerRoll = managerRoll;
    }

    public void updateAdapter(List<ItemNote> listNote) {
        this.listNote.clear();
        this.listNote.addAll(listNote);
    }

    @Override
    public int getItemViewType(int position) {
        return listNote.get(position).getType();
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == DbDesc.typeText) {
            ItemNoteTextBinding bindingText = DataBindingUtil.inflate(inflater, R.layout.item_note_text, parent, false);
            return new NoteHolder(bindingText);
        } else {
            ItemNoteRollBinding bindingRoll = DataBindingUtil.inflate(inflater, R.layout.item_note_roll, parent, false);
            return new NoteHolder(bindingRoll);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        ItemNote itemNote = listNote.get(position);

        holder.ntContour.setBackgroundColor(ContextCompat.getColor(context, Help.Icon.colorsDark[itemNote.getColor()]));
        holder.ntBackground.setBackgroundColor(ContextCompat.getColor(context, Help.Icon.colors[itemNote.getColor()]));

        if (itemNote.getType() == DbDesc.typeText) {
            holder.bind(itemNote, null);
        } else {
            holder.bind(itemNote, managerRoll.getListRoll(itemNote.getCreate()));
        }
    }

    @Override
    public int getItemCount() {
        return listNote.size();
    }

    class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final View ntContour, ntBackground, ntClick;

        private ItemNoteTextBinding bindingText;
        private ItemNoteRollBinding bindingRoll;

        NoteHolder(ItemNoteTextBinding bindingText) {
            super(bindingText.getRoot());

            this.bindingText = bindingText;

            ntContour = itemView.findViewById(R.id.itemNote_fl_main);
            ntBackground = itemView.findViewById(R.id.itemNote_fl_submain);
            ntClick = itemView.findViewById(R.id.itemNote_ll_click);

            ntClick.setOnClickListener(this);
            ntClick.setOnLongClickListener(this);
        }

        NoteHolder(ItemNoteRollBinding bindingRoll) {
            super(bindingRoll.getRoot());

            this.bindingRoll = bindingRoll;

            ntContour = itemView.findViewById(R.id.itemNote_fl_main);
            ntBackground = itemView.findViewById(R.id.itemNote_fl_submain);
            ntClick = itemView.findViewById(R.id.itemNote_ll_click);

            ntClick.setOnClickListener(this);
            ntClick.setOnLongClickListener(this);
        }

        void bind(ItemNote itemNote, List<ItemRoll> listRoll) {
            if (itemNote.getType() == DbDesc.typeText) {
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
            return true;
        }
    }
}
