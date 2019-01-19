package sgtmelon.scriptum.app.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.model.item.CursorItem;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.view.fragment.RollFragment;
import sgtmelon.scriptum.databinding.ItemRollReadBinding;
import sgtmelon.scriptum.databinding.ItemRollWriteBinding;
import sgtmelon.scriptum.office.annot.def.TypeRollDef;
import sgtmelon.scriptum.office.intf.BindIntf;
import sgtmelon.scriptum.office.intf.InputIntf;
import sgtmelon.scriptum.office.st.NoteSt;

/**
 * Адаптер для {@link RollFragment}
 */
public final class RollAdapter extends ParentAdapter<RollItem, RollAdapter.RollHolder> {

    private NoteSt noteSt;
    private InputIntf inputIntf;
    private BindIntf bindIntf;

    private boolean checkToggle;
    private int cursorPosition = -1;

    public RollAdapter(@NonNull Context context) {
        super(context);
    }

    public void setNoteSt(@NonNull NoteSt noteSt) {
        this.noteSt = noteSt;
    }

    public void setInputIntf(@NonNull InputIntf inputIntf) {
        this.inputIntf = inputIntf;
    }

    public void setBindIntf(@NonNull BindIntf bindIntf) {
        this.bindIntf = bindIntf;
    }

    public void setCheckToggle(boolean checkToggle) {
        this.checkToggle = checkToggle;
    }

    public void setCursorPosition(@IntRange(from = -1) int cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    @NonNull
    @Override
    public RollHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            default:
            case TypeRollDef.read:
                final ItemRollReadBinding bindingRead = DataBindingUtil.inflate(
                        inflater, R.layout.item_roll_read, parent, false
                );
                return new RollHolder(bindingRead);
            case TypeRollDef.write:
                final ItemRollWriteBinding bindingWrite = DataBindingUtil.inflate(
                        inflater, R.layout.item_roll_write, parent, false
                );
                return new RollHolder(bindingWrite);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RollHolder holder, int position) {
        final RollItem item = list.get(position);
        holder.bind(item);

        if (cursorPosition != -1) {
            holder.setSelections(cursorPosition);
            cursorPosition = -1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return noteSt.isEdit()
                ? TypeRollDef.write
                : TypeRollDef.read;
    }

    final class RollHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnTouchListener, TextWatcher {

        private final ItemRollWriteBinding bindingWrite;
        private final ItemRollReadBinding bindingRead;

        private EditText rollEnter;

        private CheckBox rollCheck;     //Отметка о выполении
        private View clickView;         //Кнопка, которая идёт поверх rollCheck, для полноценного эффекта нажатия

        private String textFrom = "";
        private int cursorFrom = 0;

        RollHolder(ItemRollWriteBinding bindingWrite) {
            super(bindingWrite.getRoot());

            this.bindingWrite = bindingWrite;
            bindingRead = null;

            rollEnter = itemView.findViewById(R.id.roll_enter);
            clickView = itemView.findViewById(R.id.click_button);

            rollEnter.setOnTouchListener(this);
            rollEnter.addTextChangedListener(this); // TODO: 10.01.2019 исправить, добавить undo/redo

            clickView.setOnTouchListener(this);
        }

        RollHolder(ItemRollReadBinding bindingRead) {
            super(bindingRead.getRoot());

            this.bindingRead = bindingRead;
            bindingWrite = null;

            rollCheck = itemView.findViewById(R.id.roll_check);
            clickView = itemView.findViewById(R.id.click_button);

            clickView.setOnClickListener(this);
        }

        void bind(@NonNull RollItem rollItem) {
            if (noteSt.isEdit()) {
                bindingWrite.setRollItem(rollItem);
                bindingWrite.executePendingBindings();
            } else {
                bindingRead.setRollItem(rollItem);
                bindingRead.setKeyBin(noteSt.isBin());
                bindingRead.setCheckToggle(checkToggle);
                bindingRead.executePendingBindings();
            }
        }

        void setSelections(@IntRange(from = 0) int position) {
            rollEnter.requestFocus();
            rollEnter.setSelection(position);
        }

        @Override
        public void onClick(View v) {
            if (noteSt.isEdit()) return;

            final int p = getAdapterPosition();
            rollCheck.setChecked(!list.get(p).isCheck());
            clickListener.onItemClick(v, p);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dragListener.setDrag(v.getId() == R.id.click_button);
            }

            return false;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            textFrom = s.toString();
            cursorFrom = rollEnter.getSelectionEnd();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            final int p = getAdapterPosition();

            if (p == RecyclerView.NO_POSITION) return;

            final String textTo = s.toString();
            final int cursorTo = rollEnter.getSelectionEnd();

            if (textFrom.equals(textTo)) return;

            if (!TextUtils.isEmpty(textTo)) {
                if (!TextUtils.isEmpty(textFrom)) {
                    final CursorItem cursorItem = new CursorItem(cursorFrom, cursorTo);
                    inputIntf.onRollChange(p, textFrom, textTo, cursorItem);

                    textFrom = textTo;
                    cursorFrom = cursorTo;
                }
            } else {
                inputIntf.onRollRemove(p, list.get(p).toString());
            }

            bindIntf.bindInput();
        }

        @Override
        public void afterTextChanged(Editable s) {
            final int p = getAdapterPosition();

            if (p == RecyclerView.NO_POSITION) return;

            if (!TextUtils.isEmpty(textFrom)) {
                rollWatcher.afterRollChanged(p, rollEnter.getText().toString());
            }

            if (!inputIntf.getEnabled()) {
                inputIntf.setEnabled(true);
            }
        }

    }

}