package sgtmelon.scriptum.app.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.holder.RollReadHolder
import sgtmelon.scriptum.app.adapter.holder.RollWriteHolder
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
class RollAdapter(context: Context) : ParentAdapter<RollItem, RecyclerView.ViewHolder>(context) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TypeRollDef.read) {
            val bindingRead = DataBindingUtil.inflate<ItemRollReadBinding>(
                    inflater, R.layout.item_roll_read, parent, false
            )

            RollReadHolder(bindingRead)
        } else {
            val bindingWrite = DataBindingUtil.inflate<ItemRollWriteBinding>(
                    inflater, R.layout.item_roll_write, parent, false
            )

            RollWriteHolder(bindingWrite, dragListener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]

        if (holder is RollReadHolder) {
            holder.bind(item, noteSt, checkToggle)

            holder.clickView.setOnClickListener { v ->
                holder.rollCheck.isChecked = !item.isCheck
                clickListener.onItemClick(v, position)
            }
        } else if (holder is RollWriteHolder) {
            holder.bind(item)

            holder.rollEnter.addTextChangedListener(RollTextWatcher(
                    holder.rollEnter, position, item, inputIntf, bindIntf, rollWatcher
            ))

            if (cursorPosition != -1) {
                holder.setSelections(cursorPosition)
                cursorPosition = -1
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (!noteSt.isEdit) TypeRollDef.read
        else TypeRollDef.write
    }

}