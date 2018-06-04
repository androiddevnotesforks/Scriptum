package sgtmelon.handynotes.adapter;

import android.content.Context;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.interfaces.ItemClick;
import sgtmelon.handynotes.interfaces.RollTextWatcher;
import sgtmelon.handynotes.model.item.ItemRoll;

public class AdapterRoll extends RecyclerView.Adapter<AdapterRoll.RollHolder> {

    //region Variables
    private static final int typeRead = 0, typeWrite = 1;

    private final LayoutInflater inflater;

    private final List<ItemRoll> listRoll;

    private final boolean keyBin;
    private boolean keyEdit;
    //endregion

    public AdapterRoll(Context context, boolean keyBin, boolean keyEdit) {
        this.inflater = LayoutInflater.from(context);

        listRoll = new ArrayList<>();

        this.keyBin = keyBin;
        this.keyEdit = keyEdit;
    }

    private ItemClick.Click click;
    private ItemClick.Drag drag;
    private RollTextWatcher watcher;

    public void setCallback(ItemClick.Click click, ItemClick.Drag drag, RollTextWatcher watcher) {
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

    @Override
    public int getItemViewType(int position) {
        if (keyEdit) return typeWrite;
        else return typeRead;
    }

    @NonNull
    @Override
    public RollHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case typeRead:
                view = inflater.inflate(R.layout.list_item_roll_read, parent, false);
                break;
            case typeWrite:
                view = inflater.inflate(R.layout.list_item_roll_write, parent, false);
                break;
        }
        return new RollHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RollHolder holder, int position) {
        ItemRoll itemRoll = listRoll.get(position);

        if (keyEdit) {
            holder.rlEnter.setText(itemRoll.getText());
        } else {
            holder.rlText.setText(itemRoll.getText());
            holder.rlCheck.setChecked(itemRoll.isCheck());
            holder.rlClick.setVisibility(keyBin ? View.INVISIBLE : View.VISIBLE);   //Если из корзины, нажатие - недоступно
        }
    }

    @Override
    public int getItemCount() {
        return listRoll.size();
    }

    class RollHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener, TextWatcher {

        //region Variables
        private EditText rlEnter;
        private ImageButton rlDrag; //Кнопка для перетаскивания (< >)

        private TextView rlText;
        private CheckBox rlCheck;   //Отметка о выполении
        private ImageView rlClick;  //Кнопка, которая идёт поверх rlCheck, для полноценного эффекта нажатия
        //endregion

        RollHolder(View itemView) {
            super(itemView);
            if (keyEdit) {
                rlEnter = itemView.findViewById(R.id.editText_itemRoll_enter);
                rlDrag = itemView.findViewById(R.id.iButton_itemRoll_drag);

                rlEnter.setOnTouchListener(this);
                rlEnter.addTextChangedListener(this);

                rlDrag.setOnTouchListener(this);
            } else {
                rlText = itemView.findViewById(R.id.tView_itemRoll_text);
                rlCheck = itemView.findViewById(R.id.checkBox_itemRoll_check);
                rlClick = itemView.findViewById(R.id.iView_itemRoll_click);

                rlClick.setOnClickListener(this);
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
                case R.id.iButton_itemRoll_drag:
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        drag.setDrag(true);
                    }
                    break;
                case R.id.editText_itemRoll_enter:
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        drag.setDrag(false);
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
            watcher.onRollChanged(getAdapterPosition(), rlEnter.getText().toString());
        }
    }
}
