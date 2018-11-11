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

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.view.fragment.RollFragment;
import sgtmelon.scriptum.databinding.ItemRollReadBinding;
import sgtmelon.scriptum.databinding.ItemRollWriteBinding;
import sgtmelon.scriptum.office.annot.def.TypeRollDef;
import sgtmelon.scriptum.office.intf.InputIntf;
import sgtmelon.scriptum.office.st.NoteSt;

/**
 * Адаптер для {@link RollFragment}
 */
public final class RollAdapter extends ParentAdapter<RollItem, RollAdapter.RollHolder> {

    private NoteSt noteSt;
    private InputIntf inputIntf;

    public RollAdapter(Context context) {
        super(context);
    }

    public void setNoteSt(NoteSt noteSt) {
        this.noteSt = noteSt;
    }

    public void setInputIntf(InputIntf inputIntf) {
        this.inputIntf = inputIntf;
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
        private View dragView; //Кнопка для перетаскивания (< >)

        private CheckBox rollCheck;   //Отметка о выполении
        private View clickView;  //Кнопка, которая идёт поверх rollCheck, для полноценного эффекта нажатия

        private String textBefore;

        RollHolder(ItemRollWriteBinding bindingWrite) {
            super(bindingWrite.getRoot());

            this.bindingWrite = bindingWrite;
            bindingRead = null;

            rollEnter = itemView.findViewById(R.id.roll_enter);
            dragView = itemView.findViewById(R.id.drag_button);

            rollEnter.setOnTouchListener(this);
            rollEnter.addTextChangedListener(this);

            dragView.setOnTouchListener(this);
        }

        RollHolder(ItemRollReadBinding bindingRead) {
            super(bindingRead.getRoot());

            this.bindingRead = bindingRead;
            bindingWrite = null;

            rollCheck = itemView.findViewById(R.id.roll_check);
            clickView = itemView.findViewById(R.id.click_image);

            clickView.setOnClickListener(this);
        }

        void bind(RollItem rollItem) {
            if (noteSt.isEdit()) {
                bindingWrite.setRollItem(rollItem);
                bindingWrite.executePendingBindings();
            } else {
                bindingRead.setRollItem(rollItem);
                bindingRead.setKeyBin(noteSt.isBin());
                bindingRead.executePendingBindings();
            }
        }

        @Override
        public void onClick(View view) {
            if (!noteSt.isEdit()) {
                int p = getAdapterPosition();
                rollCheck.setChecked(!list.get(p).isCheck());
                clickListener.onItemClick(view, p);
            }
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                dragListener.setDrag(view.getId() == R.id.drag_button);
            }
            return false;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            textBefore = charSequence.toString();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            final String textChanged = charSequence.toString();
            final int p = getAdapterPosition();

            if (!TextUtils.isEmpty(textBefore) && !textChanged.equals(textBefore) && p != -1) {
                if (!TextUtils.isEmpty(textChanged)) {
                    inputIntf.onRollChange(p, textBefore);
                    textBefore = textChanged;
                } else {
                    inputIntf.onRollRemove(p, textBefore);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            final int p = getAdapterPosition();
            if (!TextUtils.isEmpty(textBefore) && p != -1) {
                rollWatcher.afterRollChanged(p, rollEnter.getText().toString());
            }
        }

    }

}