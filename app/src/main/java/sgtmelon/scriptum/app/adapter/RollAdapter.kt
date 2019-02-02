package sgtmelon.scriptum.app.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.databinding.DataBindingUtil
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.holder.RollHolder
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.view.fragment.RollFragment
import sgtmelon.scriptum.databinding.ItemRollReadBinding
import sgtmelon.scriptum.databinding.ItemRollWriteBinding
import sgtmelon.scriptum.office.annot.def.TypeRollDef
import sgtmelon.scriptum.office.intf.BindIntf
import sgtmelon.scriptum.office.intf.InputIntf
import sgtmelon.scriptum.office.intf.RollTextWatcher
import sgtmelon.scriptum.office.st.NoteSt

/**
 * Адаптер для [RollFragment]
 */
class RollAdapter(context: Context) : ParentAdapter<RollItem, RollHolder>(context) {

    private lateinit var noteSt: NoteSt
    private lateinit var inputIntf: InputIntf
    private lateinit var bindIntf: BindIntf

    private var checkToggle: Boolean = false
    private var cursorPosition = -1

    fun setNoteSt(noteSt: NoteSt) {
        this.noteSt = noteSt
    }

    fun setInputIntf(inputIntf: InputIntf) {
        this.inputIntf = inputIntf
    }

    fun setBindIntf(bindIntf: BindIntf) {
        this.bindIntf = bindIntf
    }

    fun setCheckToggle(checkToggle: Boolean) {
        this.checkToggle = checkToggle
    }

    fun setCursorPosition(@IntRange(from = -1) cursorPosition: Int) {
        this.cursorPosition = cursorPosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RollHolder {
        when (viewType) {
            TypeRollDef.write -> {
                val bindingWrite = DataBindingUtil.inflate<ItemRollWriteBinding>(
                        inflater, R.layout.item_roll_write, parent, false
                )

                val rollHolder = RollHolder(bindingWrite)
                rollHolder.dragListener = dragListener

                return rollHolder
            }
            else -> {
                val bindingRead = DataBindingUtil.inflate<ItemRollReadBinding>(
                        inflater, R.layout.item_roll_read, parent, false
                )

                val rollHolder = RollHolder(bindingRead)
                rollHolder.dragListener = dragListener

                return rollHolder
            }
        }
    }

    override fun onBindViewHolder(holder: RollHolder, position: Int) {
        val item = list[position]
        holder.bind(noteSt, item, checkToggle)

        if (holder.rollEnter != null) {
            val rollTextWatcher = RollTextWatcher(position, item)
            rollTextWatcher.rollEnter = holder.rollEnter
            rollTextWatcher.inputIntf = inputIntf
            rollTextWatcher.bindIntf = bindIntf
            rollTextWatcher.rollWatcher = rollWatcher

            holder.rollEnter.addTextChangedListener(rollTextWatcher)
        }

        holder.clickView.setOnClickListener { v ->
            if (!noteSt.isEdit) {
                holder.rollCheck!!.isChecked = !item.isCheck
                clickListener.onItemClick(v, position)
            }
        }

        if (cursorPosition != -1) {
            holder.setSelections(cursorPosition)
            cursorPosition = -1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (noteSt.isEdit) TypeRollDef.write
        else TypeRollDef.read
    }

}