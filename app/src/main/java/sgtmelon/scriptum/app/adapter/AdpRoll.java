package sgtmelon.scriptum.app.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.model.item.ItemRoll;
import sgtmelon.scriptum.databinding.ItemRollReadBinding;
import sgtmelon.scriptum.databinding.ItemRollWriteBinding;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefRollType;
import sgtmelon.scriptum.office.intf.IntfItem;
import sgtmelon.scriptum.office.st.StNote;

public class AdpRoll extends RecyclerView.Adapter<AdpRoll.RollHolder> {


    private final Context context;
    private final List<ItemRoll> listRoll = new ArrayList<>();

    private StNote stNote;

    private IntfItem.Click click;
    private IntfItem.Drag drag;
    private IntfItem.Watcher watcher;

    public AdpRoll(Context context) {
        this.context = context;
    }

    public void setStNote(StNote stNote) {
        this.stNote = stNote;
    }

    public void setCallback(IntfItem.Click click, IntfItem.Drag drag, IntfItem.Watcher watcher) {
        this.click = click;
        this.drag = drag;
        this.watcher = watcher;
    }

    public void setListRoll(List<ItemRoll> listRoll) {
        this.listRoll.clear();
        this.listRoll.addAll(listRoll);
    }

    public void setListRoll(int position, ItemRoll itemRoll) {
        listRoll.set(position, itemRoll);
    }

    @NonNull
    @Override
    public RollHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            default:
            case DefRollType.read:
                ItemRollReadBinding bindingRead = DataBindingUtil.inflate(inflater, R.layout.item_roll_read, parent, false);
                return new RollHolder(bindingRead);
            case DefRollType.write:
                ItemRollWriteBinding bindingWrite = DataBindingUtil.inflate(inflater, R.layout.item_roll_write, parent, false);
                return new RollHolder(bindingWrite);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!stNote.isEdit()) return DefRollType.read;
        else return DefRollType.write;
    }

    @Override
    public void onBindViewHolder(@NonNull RollHolder holder, int position) {
        ItemRoll itemRoll = listRoll.get(position);

        if (stNote.isEdit()) {
            if (itemRoll.isCheck()) {
                holder.rlDrag.setColorFilter(Help.Clr.get(context, R.attr.clAccent));
            } else holder.rlDrag.setColorFilter(Help.Clr.get(context, R.attr.clIcon));
        }

        holder.bind(itemRoll);
    }

    @Override
    public int getItemCount() {
        return listRoll.size();
    }

    class RollHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener, TextWatcher {

        private final ItemRollWriteBinding bindingWrite;
        private final ItemRollReadBinding bindingRead;

        private EditText rlEnter;
        private ImageButton rlDrag; //Кнопка для перетаскивания (< >)

        private CheckBox rlCheck;   //Отметка о выполении
        private ImageView rlClick;  //Кнопка, которая идёт поверх rlCheck, для полноценного эффекта нажатия

        RollHolder(ItemRollWriteBinding bindingWrite) {
            super(bindingWrite.getRoot());

            this.bindingWrite = bindingWrite;
            bindingRead = null;

            rlEnter = itemView.findViewById(R.id.itemRoll_et_enter);
            rlDrag = itemView.findViewById(R.id.itemRoll_ib_drag);

            rlEnter.setOnTouchListener(this);
            rlEnter.addTextChangedListener(this);

            rlDrag.setOnTouchListener(this);
        }

        RollHolder(ItemRollReadBinding bindingRead) {
            super(bindingRead.getRoot());

            this.bindingRead = bindingRead;
            bindingWrite = null;

            rlCheck = itemView.findViewById(R.id.itemRoll_cb_check);
            rlClick = itemView.findViewById(R.id.itemRoll_iv_click);

            rlClick.setOnClickListener(this);
        }

        void bind(ItemRoll itemRoll) {
            if (stNote.isEdit()) {
                bindingWrite.setItemRoll(itemRoll);
                bindingWrite.executePendingBindings();
            } else {
                bindingRead.setItemRoll(itemRoll);
                bindingRead.setKeyBin(stNote.isBin());
                bindingRead.executePendingBindings();
            }
        }

        @Override
        public void onClick(View view) {
            if (!stNote.isEdit()) {
                int p = getAdapterPosition();
                rlCheck.setChecked(!listRoll.get(p).isCheck());
                click.onItemClick(view, p);
            }
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                drag.setItemDrag(view.getId() == R.id.itemRoll_ib_drag);
            }
            return false;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            watcher.onChanged(getAdapterPosition(), rlEnter.getText().toString());
        }

    }

}
