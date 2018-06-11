package sgtmelon.handynotes.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.model.item.ItemRoll;
import sgtmelon.handynotes.database.NoteDB;
import sgtmelon.handynotes.service.Help;
import sgtmelon.handynotes.interfaces.ItemClick;
import sgtmelon.handynotes.model.manager.ManagerRoll;

public class AdapterNote extends RecyclerView.Adapter<AdapterNote.NoteHolder> {

    //region Variables
    private final Context context;
    private final LayoutInflater inflater;

    private final List<ItemNote> listNote;

    private final Drawable checkOn, checkOff;
    private final Drawable bindTx, bindRl;
    //endregion

    public AdapterNote(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        listNote = new ArrayList<>();

        checkOn = Help.Icon.getDrawable(context, R.drawable.ic_info_check_done);
        checkOff = Help.Icon.getDrawable(context, R.drawable.ic_info_check_outline);

        bindTx = Help.Icon.getDrawable(context, R.drawable.ic_menu_bind_text);
        bindRl = Help.Icon.getDrawable(context, R.drawable.ic_menu_bind_roll);
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
        View view = null;
        switch (viewType) {
            case NoteDB.typeText:
                view = inflater.inflate(R.layout.item_note_text, parent, false);
                break;
            case NoteDB.typeRoll:
                view = inflater.inflate(R.layout.item_note_roll, parent, false);
                break;
        }
        return new NoteHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        ItemNote itemNote = listNote.get(position);

        holder.ntContour.setBackgroundColor(ContextCompat.getColor(context, Help.Icon.colorsDark[itemNote.getColor()]));
        holder.ntBackground.setBackgroundColor(ContextCompat.getColor(context, Help.Icon.colors[itemNote.getColor()]));

        if (!itemNote.getName().equals("")) {
            holder.ntName.setVisibility(View.VISIBLE);
            holder.ntName.setText(itemNote.getName());
        } else holder.ntName.setVisibility(View.GONE);

        holder.ntText.setText(itemNote.getText());

        if (itemNote.getType() == NoteDB.typeRoll) {
            List<ItemRoll> listRoll = managerRoll.getListRoll(itemNote.getCreate());

            int size = listRoll.size();
            for (int i = 0; i < size; i++) {
                ItemRoll itemRoll = listRoll.get(size - 1 - i); //Заполнение с конца (последний индекс вычитает позицию)
                int p = 3 - i;

                holder.rlLayouts[p].setVisibility(View.VISIBLE);
                holder.rlChecks[p].setImageDrawable(itemRoll.isCheck() ? checkOn : checkOff);
                holder.rlTexts[p].setText(itemRoll.getText());
            }

            for (int j = 0; j < 4 - size; j++) {    //Скрываем пункты с начала, которые не нужны
                holder.rlLayouts[j].setVisibility(View.GONE);
            }
        }

        if (!itemNote.isStatus()) {
            holder.ntBind.setVisibility(View.GONE);
        } else {
            holder.ntBind.setVisibility(View.VISIBLE);
            holder.ntBind.setImageDrawable(itemNote.getType() == NoteDB.typeText ? bindTx : bindRl);
        }

        holder.ntRank.setVisibility(itemNote.getRankId().length == 0 ? View.GONE : View.VISIBLE);

        holder.ntCreate.setText(Help.Time.formatNoteDate(context, itemNote.getCreate()));
        holder.ntChange.setText(Help.Time.formatNoteDate(context, itemNote.getChange()));
    }

    @Override
    public int getItemCount() {
        return listNote.size();
    }

    class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final View ntContour, ntBackground, ntClick;
        private final TextView ntName, ntText, ntCreate, ntChange;
        private final ImageView ntBind, ntRank;

        //Массивы для быстрой работы со списком
        private ImageView[] rlChecks;
        private TextView[] rlTexts;
        private LinearLayout[] rlLayouts;

        NoteHolder(View itemView, int viewType) {
            super(itemView);

            ntContour = itemView.findViewById(R.id.itemNote_fl_main);
            ntBackground = itemView.findViewById(R.id.itemNote_fl_submain);
            ntClick = itemView.findViewById(R.id.itemNote_ll_click);

            ntClick.setOnClickListener(this);
            ntClick.setOnLongClickListener(this);

            ntName = itemView.findViewById(R.id.itemNote_tv_name);
            ntText = itemView.findViewById(R.id.itemNote_tv_text);
            ntCreate = itemView.findViewById(R.id.itemNote_tv_create);
            ntChange = itemView.findViewById(R.id.itemNote_tv_change);

            ntBind = itemView.findViewById(R.id.itemNote_iv_bind);
            ntRank = itemView.findViewById(R.id.itemNote_iv_rank);

            if (viewType == NoteDB.typeRoll) {
                rlChecks = new ImageView[]{
                        itemView.findViewById(R.id.itemNote_iv_check_1),
                        itemView.findViewById(R.id.itemNote_iv_check_2),
                        itemView.findViewById(R.id.itemNote_iv_check_3),
                        itemView.findViewById(R.id.itemNote_iv_check_4)};

                rlTexts = new TextView[]{
                        itemView.findViewById(R.id.itemNote_tv_text_1),
                        itemView.findViewById(R.id.itemNote_tv_text_2),
                        itemView.findViewById(R.id.itemNote_tv_text_3),
                        itemView.findViewById(R.id.itemNote_tv_text_4)};

                rlLayouts = new LinearLayout[]{
                        itemView.findViewById(R.id.itemNote_ll_roll_1),
                        itemView.findViewById(R.id.itemNote_ll_roll_2),
                        itemView.findViewById(R.id.itemNote_ll_roll_3),
                        itemView.findViewById(R.id.itemNote_ll_roll_4)};
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
