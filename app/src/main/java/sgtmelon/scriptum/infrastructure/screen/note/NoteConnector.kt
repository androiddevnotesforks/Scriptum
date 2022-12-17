package sgtmelon.scriptum.infrastructure.screen.note

import sgtmelon.scriptum.infrastructure.model.key.preference.Color

interface NoteConnector {

    val bundleProvider: NoteBundleProvider

    fun updateHolder(color: Color)

    fun convertNote()

    @Deprecated("Improve it, I don't think it's work correct with split screen for example.")
    fun isOrientationChanging(): Boolean

}