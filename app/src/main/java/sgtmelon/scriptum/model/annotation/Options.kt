package sgtmelon.scriptum.model.annotation

import androidx.annotation.IntDef
import sgtmelon.scriptum.screen.ui.main.BinFragment
import sgtmelon.scriptum.screen.ui.main.NotesFragment
import sgtmelon.scriptum.screen.vm.main.BinViewModel
import sgtmelon.scriptum.screen.vm.main.NotesViewModel

/**
 * Describes menu options for [NotesFragment]/[NotesViewModel] and [BinFragment]/[BinViewModel]
 */
object Options {

    @IntDef(Notes.BIND, Notes.CONVERT, Notes.COPY, Notes.DELETE)
    annotation class Notes {
        companion object {
            const val BIND = 0
            const val CONVERT = 1
            const val COPY = 2
            const val DELETE = 3
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