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
class PrintPrefKeyHolder(itemView: View) : ParentHolder(itemView) {

    private val keyText = itemView.findViewById<TextView>(R.id.print_pref_key_text)
    private val defText = itemView.findViewById<TextView>(R.id.print_pref_def_text)
    private val valueText = itemView.findViewById<TextView>(R.id.print_pref_value_text)

    fun bind(item: PrintItem.Preference.Key) {
        val context = itemView.context

        keyText.text = context.getString(R.string.print_pref_key, item.key)
        defText.text = context.getString(R.string.print_pref_def, item.def)
        valueText.text = context.getString(R.string.print_pref_value, item.value)
    }
}