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
class PrintPrefPathHolder(itemView: View) : ParentHolder(itemView) {

    private val pathText = itemView.findViewById<TextView>(R.id.print_pref_path_text)

    fun bind(item: PrintItem.Preference.Path) {
        val context = itemView.context
        val file = item.item

        pathText.text = context.getString(R.string.print_pref_file_path, file.path)
    }
}