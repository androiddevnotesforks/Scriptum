package sgtmelon.scriptum.data.item

import androidx.annotation.StringRes

/**
 * Object for describe preference screens
 */
sealed class PreferenceItem {
    class Header(@StringRes val titleId: Int) : PreferenceItem()
    class Simple(@StringRes val titleId: Int) : PreferenceItem()
    class Summary(@StringRes val titleId: Int, val summaryText: String) : PreferenceItem()
}