package sgtmelon.scriptum.source.ui.model

import androidx.annotation.StringRes

/**
 * Object for describe preference screens
 */
sealed class PreferenceItem {

    class Header(@StringRes val titleId: Int) : PreferenceItem()

    class Simple(@StringRes val titleId: Int, val isEnabled: Boolean = true) : PreferenceItem()

    sealed class Summary(@StringRes val titleId: Int, val isEnabled: Boolean) : PreferenceItem() {

        class Text(
            @StringRes titleId: Int,
            val summaryText: String,
            isEnabled: Boolean = true
        ) : Summary(titleId, isEnabled)

        class Id(
            @StringRes titleId: Int,
            @StringRes val summaryId: Int,
            isEnabled: Boolean = true
        ) : Summary(titleId, isEnabled)
    }

    class Switch(
        @StringRes val titleId: Int,
        @StringRes val summaryId: Int,
        val isChecked: Boolean,
        val isEnabled: Boolean = true
    ) : PreferenceItem()
}