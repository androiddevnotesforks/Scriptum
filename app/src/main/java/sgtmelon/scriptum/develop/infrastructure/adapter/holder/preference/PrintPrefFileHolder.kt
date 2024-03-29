package sgtmelon.scriptum.develop.infrastructure.adapter.holder.preference

import android.text.format.Formatter
import java.io.File
import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ItemPrintPrefFileBinding
import sgtmelon.scriptum.develop.infrastructure.model.PrintItem
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder

class PrintPrefFileHolder(
    private val binding: ItemPrintPrefFileBinding
) : ParentHolder(binding.root) {

    fun bind(item: PrintItem.Preference.File) = with(binding) {
        with(item.file) {
            nameText.text = context.getString(R.string.print_pref_file_name, name)
            pathText.text = context.getString(R.string.print_pref_file_path, path)

            val fileSize = Formatter.formatShortFileSize(context, File(path).length())
            sizeText.text = context.getString(R.string.print_pref_file_size, fileSize)
        }
    }
}