package sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.preference

import android.view.View
import android.widget.TextView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem
import sgtmelon.scriptum.cleanup.presentation.adapter.PrintAdapter
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.ParentHolder

/**
 * Holder for developer screens and work inside [PrintAdapter].
 */
class PrintPrefTitleHolder(itemView: View) : ParentHolder(itemView) {

    private val titleText = itemView.findViewById<TextView>(R.id.print_pref_title_text)

    fun bind(item: PrintItem.Preference.Title) {
        titleText.text = itemView.context.getString(item.title)
    }
}