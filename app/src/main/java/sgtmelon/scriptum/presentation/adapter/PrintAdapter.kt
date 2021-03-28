package sgtmelon.scriptum.presentation.adapter

import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.domain.model.item.PrintItem
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PrintActivity

/**
 * Adapter which displays print list of db entities or preference keys for [PrintActivity].
 */
class PrintAdapter : ParentAdapter<PrintItem, RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemViewType(position: Int) = when (list[position]) {
        is PrintItem.Alarm -> Type.NOTE
        is PrintItem.Note -> Type.ROLL
        is PrintItem.Preference -> Type.VISIBLE
        is PrintItem.Rank -> Type.RANK
        is PrintItem.Roll -> Type.ALARM
        is PrintItem.Visible -> Type.PREFERENCE
    }

    @IntDef(Type.NOTE, Type.ROLL, Type.VISIBLE, Type.RANK, Type.ALARM, Type.PREFERENCE)
    private annotation class Type {
        companion object {
            const val NOTE = 0
            const val ROLL = 1
            const val VISIBLE = 2
            const val RANK = 3
            const val ALARM = 4
            const val PREFERENCE = 5
        }
    }
}