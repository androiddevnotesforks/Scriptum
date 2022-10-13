package sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.preference

import android.text.format.Formatter
import android.view.View
import android.widget.TextView
import java.io.File
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.ParentHolder
import sgtmelon.scriptum.infrastructure.develop.PrintAdapter
import sgtmelon.scriptum.infrastructure.develop.PrintItem

/**
 * Holder for developer screens and work inside [PrintAdapter].
 */
class PrintPrefFileHolder(itemView: View) : ParentHolder(itemView) {

    // TODO add view binding

    private val nameText = itemView.findViewById<TextView>(R.id.print_pref_name_text)
    private val sizeText = itemView.findViewById<TextView>(R.id.print_pref_size_text)
    private val pathText = itemView.findViewById<TextView>(R.id.print_pref_path_text)

    fun bind(item: PrintItem.Preference.File) {
        val context = itemView.context

        with(item.file) {
            nameText.text = context.getString(R.string.print_pref_file_name, name)
            pathText.text = context.getString(R.string.print_pref_file_path, path)

            val fileSize = Formatter.formatShortFileSize(context, File(path).length())
            sizeText.text = context.getString(R.string.print_pref_file_size, fileSize)
        }
    }
}