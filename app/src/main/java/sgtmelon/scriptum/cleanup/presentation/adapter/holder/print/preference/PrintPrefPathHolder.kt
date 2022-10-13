package sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.preference

import android.view.View
import android.widget.TextView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.ParentHolder
import sgtmelon.scriptum.infrastructure.adapter.PrintAdapter
import sgtmelon.scriptum.infrastructure.model.item.PrintItem

/**
 * Holder for developer screens and work inside [PrintAdapter].
 */
class PrintPrefPathHolder(itemView: View) : ParentHolder(itemView) {

    // TODO add view binding

    private val pathText = itemView.findViewById<TextView>(R.id.print_pref_path_text)

    fun bind(item: PrintItem.Preference.Path) {
        pathText.text = item.file.path
    }
}