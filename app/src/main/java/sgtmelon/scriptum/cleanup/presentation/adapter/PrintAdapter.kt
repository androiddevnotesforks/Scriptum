package sgtmelon.scriptum.cleanup.presentation.adapter

import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.entity.PrintAlarmHolder
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.entity.PrintNoteHolder
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.entity.PrintRankHolder
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.entity.PrintRollHolder
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.entity.PrintVisibleHolder
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.preference.PrintPrefFileHolder
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.preference.PrintPrefKeyHolder
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.preference.PrintPrefPathHolder
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.preference.PrintPrefTitleHolder
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.develop.PrintDevelopActivity
import sgtmelon.scriptum.infrastructure.utils.inflateView

/**
 * Adapter which displays print list of db entities or preference keys for [PrintDevelopActivity].
 */
class PrintAdapter : ParentAdapter<PrintItem, RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        Type.NOTE -> PrintNoteHolder(parent.inflateView(R.layout.item_print_note))
        Type.ROLL -> PrintRollHolder(parent.inflateView(R.layout.item_print_roll))
        Type.VISIBLE -> PrintVisibleHolder(parent.inflateView(R.layout.item_print_visible))
        Type.RANK -> PrintRankHolder(parent.inflateView(R.layout.item_print_rank))
        Type.ALARM -> PrintAlarmHolder(parent.inflateView(R.layout.item_print_alarm))
        Type.PREF_TITLE -> PrintPrefTitleHolder(parent.inflateView(R.layout.item_print_pref_title))
        Type.PREF_KEY -> PrintPrefKeyHolder(parent.inflateView(R.layout.item_print_pref_key))
        Type.PREF_PATH -> PrintPrefPathHolder(parent.inflateView(R.layout.item_print_pref_path))
        else -> PrintPrefFileHolder(parent.inflateView(R.layout.item_print_pref_file))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list.getOrNull(position) ?: return) {
            is PrintItem.Note -> (holder as? PrintNoteHolder)?.bind(item)
            is PrintItem.Roll -> (holder as? PrintRollHolder)?.bind(item)
            is PrintItem.Visible -> (holder as? PrintVisibleHolder)?.bind(item)
            is PrintItem.Rank -> (holder as? PrintRankHolder)?.bind(item)
            is PrintItem.Alarm -> (holder as? PrintAlarmHolder)?.bind(item)
            is PrintItem.Preference.Title -> (holder as? PrintPrefTitleHolder)?.bind(item)
            is PrintItem.Preference.Key -> (holder as? PrintPrefKeyHolder)?.bind(item)
            is PrintItem.Preference.Path -> (holder as? PrintPrefPathHolder)?.bind(item)
            is PrintItem.Preference.File -> (holder as? PrintPrefFileHolder)?.bind(item)
        }
    }

    override fun getItemViewType(position: Int) = when (list[position]) {
        is PrintItem.Note -> Type.NOTE
        is PrintItem.Roll -> Type.ROLL
        is PrintItem.Visible -> Type.VISIBLE
        is PrintItem.Rank -> Type.RANK
        is PrintItem.Alarm -> Type.ALARM
        is PrintItem.Preference.Title -> Type.PREF_TITLE
        is PrintItem.Preference.Key -> Type.PREF_KEY
        is PrintItem.Preference.Path -> Type.PREF_PATH
        is PrintItem.Preference.File -> Type.PREF_FILE
    }

    @IntDef(
        Type.NOTE,
        Type.ROLL,
        Type.VISIBLE,
        Type.RANK,
        Type.ALARM,
        Type.PREF_TITLE,
        Type.PREF_KEY,
        Type.PREF_PATH,
        Type.PREF_FILE
    )
    private annotation class Type {
        companion object {
            const val NOTE = 0
            const val ROLL = 1
            const val VISIBLE = 2
            const val RANK = 3
            const val ALARM = 4
            const val PREF_TITLE = 5
            const val PREF_KEY = 6
            const val PREF_PATH = 7
            const val PREF_FILE = 8
        }
    }
}