package sgtmelon.scriptum.app.model.item

import sgtmelon.scriptum.app.adapter.SortAdapter
import sgtmelon.scriptum.widget.SortDialog

/**
 * Модель для сортировки [SortDialog], [SortAdapter]
 */
class SortItem(var text: String?, var key: Int) {

    override fun toString(): String {
        return "text = $text | key = $key"
    }

}