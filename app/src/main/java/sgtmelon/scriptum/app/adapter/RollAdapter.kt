package sgtmelon.scriptum.app.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.holder.RollReadHolder
import sgtmelon.scriptum.app.adapter.holder.RollWriteHolder
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.view.fragment.note.RollNoteFragment
import sgtmelon.scriptum.databinding.ItemRollReadBinding
import sgtmelon.scriptum.databinding.ItemRollWriteBinding
import sgtmelon.scriptum.office.annot.def.TypeRollDef
import sgtmelon.scriptum.office.intf.BindIntf
import sgtmelon.scriptum.office.intf.InputIntf
import sgtmelon.scriptum.office.intf.ItemIntf
import sgtmelon.scriptum.office.st.NoteSt

/**
 * Адаптер для [RollNoteFragment]
 */
class RollAdapter(context: Context, clickListener: ItemIntf.ClickListener,
                  private val dragListener: ItemIntf.DragListener,
                  private val rollWatcher: ItemIntf.RollWatcher,
                  private val inputIntf: InputIntf,
                  private val bindIntf: BindIntf
) : ParentAdapter<RollItem, RecyclerView.ViewHolder>(context, clickListener) {

    lateinit var noteSt: NoteSt

    var checkToggle: Boolean = false
    var cursorPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType == TypeRollDef.read) {
            true -> {
                val bindingRead = DataBindingUtil.inflate<ItemRollReadBinding>(
                        inflater, R.layout.item_roll_read, parent, false
                )

                RollReadHolder(bindingRead, clickListener)
            }
            false -> {
                val bindingWrite = DataBindingUtil.inflate<ItemRollWriteBinding>(
                        inflater, R.layout.item_roll_write, parent, false
                )

                RollWriteHolder(bindingWrite, dragListener, rollWatcher, inputIntf, bindIntf)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]

        if (holder is RollReadHolder) {
            holder.bind(item, noteSt, checkToggle)
        } else if (holder is RollWriteHolder) {
            holder.bind(item)

            if (cursorPosition != -1) {
                holder.setSelections(cursorPosition)
                cursorPosition = -1
            }
        }
    }

    override fun getItemViewType(position: Int) = when (!noteSt.isEdit) {
        true -> TypeRollDef.read
        false -> TypeRollDef.write
    }

}