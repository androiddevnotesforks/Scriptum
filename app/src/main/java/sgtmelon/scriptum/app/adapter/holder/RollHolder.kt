package sgtmelon.scriptum.app.adapter.holder

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.RollAdapter
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.databinding.ItemRollReadBinding
import sgtmelon.scriptum.databinding.ItemRollWriteBinding
import sgtmelon.scriptum.office.intf.ItemIntf
import sgtmelon.scriptum.office.st.NoteSt

/**
 * Держатель пункта списка для [RollAdapter]
 */
@SuppressLint("ClickableViewAccessibility")
class RollHolder : RecyclerView.ViewHolder, View.OnTouchListener {

    private val bindingWrite: ItemRollWriteBinding?
    private val bindingRead: ItemRollReadBinding?

    lateinit var dragListener: ItemIntf.DragListener

    val rollEnter: EditText?
    val rollCheck: CheckBox?

    /**
     * Кнопка, которая идёт поверх rollCheck, для полноценного эффекта нажатия
     */
    val clickView: View

    constructor(bindingWrite: ItemRollWriteBinding) : super(bindingWrite.root) {
        this.bindingWrite = bindingWrite

        bindingRead = null
        rollCheck = null

        rollEnter = itemView.findViewById(R.id.roll_enter)
        clickView = itemView.findViewById(R.id.click_button)

        rollEnter.setOnTouchListener(this)
        clickView.setOnTouchListener(this)
    }

    constructor(bindingRead: ItemRollReadBinding) : super(bindingRead.root) {
        this.bindingRead = bindingRead

        bindingWrite = null
        rollEnter = null

        rollCheck = itemView.findViewById(R.id.roll_check)
        clickView = itemView.findViewById(R.id.click_button)
    }

    fun bind(noteSt: NoteSt, rollItem: RollItem, checkToggle: Boolean) {
        if (noteSt.isEdit) {
            bindingWrite!!.rollItem = rollItem
            bindingWrite.executePendingBindings()
        } else {
            bindingRead!!.rollItem = rollItem
            bindingRead.keyBin = noteSt.isBin
            bindingRead.checkToggle = checkToggle
            bindingRead.executePendingBindings()
        }
    }

    fun setSelections(@IntRange(from = 0) position: Int) {
        rollEnter!!.requestFocus()
        rollEnter.setSelection(position)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            dragListener.setDrag(v.id == R.id.click_button)
        }
        return false
    }

}