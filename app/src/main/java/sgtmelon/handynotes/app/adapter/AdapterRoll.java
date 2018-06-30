package sgtmelon.handynotes.app.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.databinding.ItemRollReadBinding;
import sgtmelon.handynotes.databinding.ItemRollWriteBinding;
import sgtmelon.handynotes.office.intf.IntfItem;
import sgtmelon.handynotes.app.model.item.ItemRoll;

public class AdapterRoll extends RecyclerView.Adapter<AdapterRoll.RollHolder> {

    //region Variables
    private static final int typeRead = 0, typeWrite = 1;

    private final List<ItemRoll> listRoll;

    private final boolean keyBin;
    private boolean keyEdit;
    //endregion

    public AdapterRoll(boolean keyBin, boolean keyEdit) {
        listRoll = new ArrayList<>();

        this.keyBin = keyBin;
        this.keyEdit = keyEdit;
    }

    private IntfItem.Click click;
    private IntfItem.Drag drag;
    private IntfItem.Watcher watcher;

    public void setCallback(IntfItem.Click click, IntfItem.Drag drag, IntfItem.Watcher watcher) {
        this.click = click;
        this.drag = drag;
        this.watcher = watcher;
    }

    public void updateAdapter(List<ItemRoll> listRoll) {
        this.listRoll.clear();
        this.listRoll.addAll(listRoll);
    }

    public void updateAdapter(int position, ItemRoll itemRoll) {
        listRoll.set(position, itemRoll);
    }

    //Обновление режима редактирования
    public void updateAdapter(boolean keyEdit) {
        this.keyEdit = keyEdit;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RollHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == typeWrite) {
            ItemRollWriteBinding bindingWrite = DataBindingUtil.inflate(inflater, R.layout.item_roll_write, parent, false);
            return new RollHolder(bindingWrite);
        } else {
            ItemRollReadBinding bindingRead = DataBindingUtil.inflate(inflater, R.layout.item_roll_read, parent, false);
            return new RollHolder(bindingRead);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (keyEdit) return typeWrite;
        else return typeRead;
    }

    @Override
    public void onBindViewHolder(@NonNull RollHolder holder, int position) {
        holder.bind(listRoll.get(position));
    }

    @Override
    public int getItemCount() {
        return listRoll.size();
    }

    class RollHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener, TextWatcher {

        private EditText rlEnter;
        private ImageButton rlDrag; //Кнопка для перетаскивания (< >)

        private CheckBox rlCheck;   //Отметка о выполении
        private ImageView rlClick;  //Кнопка, которая идёт поверх rlCheck, для полноценного эффекта нажатия

        private final ItemRollWriteBinding bindingWrite;
        private final ItemRollReadBinding bindingRead;

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
            if (keyEdit) {
                bindingWrite.setItemRoll(itemRoll);
                bindingWrite.executePendingBindings();
            } else {
                bindingRead.setItemRoll(itemRoll);
                bindingRead.setKeyBin(keyBin);
                bindingRead.executePendingBindings();
            }
        }

        @Override
        public void onClick(View view) {
            if (!keyEdit) {
                int p = getAdapterPosition();

                rlCheck.setChecked(!listRoll.get(p).isCheck());

                click.onItemClick(view, p);
            }
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.itemRoll_ib_drag:
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        drag.setItemDrag(true);
                    }
                    break;
                case R.id.itemRoll_et_enter:
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        drag.setItemDrag(false);
                    }
                    break;
            }
            return false;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override //После изменения текста обновляем массив
        public void afterTextChanged(Editable editable) {
            watcher.onChanged(getAdapterPosition(), rlEnter.getText().toString());
        }
    }
}
