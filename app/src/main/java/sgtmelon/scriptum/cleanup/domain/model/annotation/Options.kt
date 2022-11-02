package sgtmelon.scriptum.cleanup.domain.model.annotation

import androidx.annotation.IntDef
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesViewModel

/**
 * Describes menu options for [NotesFragment]/[NotesViewModel] and [BinFragment]/[BinViewModelImpl]
 */
annotation class Options {

    @IntDef(Notes.NOTIFICATION, Notes.BIND, Notes.CONVERT, Notes.COPY, Notes.DELETE)
    annotation class Notes {
        companion object {
            const val NOTIFICATION = 0
            const val BIND = 1
            const val CONVERT = 2
            const val COPY = 3
            const val DELETE = 4
        }
    }

    @IntDef(Bin.RESTORE, Bin.COPY, Bin.CLEAR)
    annotation class Bin {
        companion object {
            const val RESTORE = 0
            const val COPY = 1
            const val CLEAR = 2
        }
    }
}