package sgtmelon.scriptum.cleanup.domain.model.annotation

import androidx.annotation.IntDef
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main.BinViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.main.NotesViewModel

/**
 * Describes menu options for [NotesFragment]/[NotesViewModel] and [BinFragment]/[BinViewModel]
 */
annotation class Options {

    @IntDef(Notes.BIND, Notes.CONVERT, Notes.COPY, Notes.DELETE)
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