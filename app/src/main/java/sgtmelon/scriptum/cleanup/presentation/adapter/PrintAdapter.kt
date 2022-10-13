package sgtmelon.scriptum.cleanup.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem
import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem.Type
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.entity.PrintAlarmHolder
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.entity.PrintNoteHolder
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.entity.PrintRankHolder
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.entity.PrintRollHolder
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.entity.PrintVisibleHolder
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.preference.PrintPrefFileHolder
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.preference.PrintPrefKeyHolder
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.preference.PrintPrefPathHolder
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.preference.PrintPrefTitleHolder
import sgtmelon.scriptum.infrastructure.adapter.diff.PrintDiff
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentListAdapter
import sgtmelon.scriptum.infrastructure.utils.inflateView

/**
 * Adapter which displays list of [PrintItem]'s.
 */
class PrintAdapter : ParentListAdapter<PrintItem, RecyclerView.ViewHolder>(PrintDiff()) {

    override fun getListCopy(list: List<PrintItem>): List<PrintItem> {
        return ArrayList(list.map {
            return@map when (it) {
                is PrintItem.Note -> it.copy()
                is PrintItem.Roll -> it.copy()
                is PrintItem.Visible -> it.copy()
                is PrintItem.Rank -> it.copy()
                is PrintItem.Alarm -> it.copy()
                is PrintItem.Preference.Title -> it.copy()
                is PrintItem.Preference.Key -> it.copy()
                is PrintItem.Preference.Path -> it.copy()
                is PrintItem.Preference.File -> it.copy()
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (Type.values()[viewType]) {
            Type.NOTE -> PrintNoteHolder(parent.inflateView(R.layout.item_print_note))
            Type.ROLL -> PrintRollHolder(parent.inflateView(R.layout.item_print_roll))
            Type.VISIBLE -> PrintVisibleHolder(parent.inflateView(R.layout.item_print_visible))
            Type.RANK -> PrintRankHolder(parent.inflateView(R.layout.item_print_rank))
            Type.ALARM -> PrintAlarmHolder(parent.inflateView(R.layout.item_print_alarm))
            Type.PREF_TITLE -> PrintPrefTitleHolder(parent.inflateView(R.layout.item_print_pref_title))
            Type.PREF_KEY -> PrintPrefKeyHolder(parent.inflateView(R.layout.item_print_pref_key))
            Type.PREF_PATH -> PrintPrefPathHolder(parent.inflateView(R.layout.item_print_pref_path))
            Type.PREF_FILE -> PrintPrefFileHolder(parent.inflateView(R.layout.item_print_pref_file))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
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

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal
}