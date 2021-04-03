package sgtmelon.scriptum.presentation.adapter.holder.print

import android.view.View
import android.widget.TextView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.item.PrintItem
import sgtmelon.scriptum.presentation.adapter.PrintAdapter
import sgtmelon.scriptum.presentation.adapter.holder.ParentHolder

/**
 * Holder for developer screens and work inside [PrintAdapter].
 */
class PrintPrefTitleHolder(itemView: View) : ParentHolder(itemView) {

    private val titleText = itemView.findViewById<TextView>(R.id.print_pref_title_text)

    fun bind(item: PrintItem.Preference.Title) {
        titleText.text = itemView.context.getString(item.title)
    }
}