package sgtmelon.scriptum.factory

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import sgtmelon.scriptum.screen.ui.AppActivity
import sgtmelon.scriptum.screen.ui.callback.IAppActivity
import sgtmelon.scriptum.screen.ui.callback.IPreferenceFragment
import sgtmelon.scriptum.screen.ui.callback.note.INoteActivity
import sgtmelon.scriptum.screen.ui.callback.note.INoteChild
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.screen.vm.AppViewModel
import sgtmelon.scriptum.screen.vm.PreferenceViewModel
import sgtmelon.scriptum.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.screen.vm.callback.IPreferenceViewModel
import sgtmelon.scriptum.screen.vm.callback.note.INoteViewModel
import sgtmelon.scriptum.screen.vm.callback.note.IRollNoteViewModel
import sgtmelon.scriptum.screen.vm.callback.note.ITextNoteViewModel
import sgtmelon.scriptum.screen.vm.note.NoteViewModel
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel

/**
 * Factory for create viewModel's.
 */
object ViewModelFactory {

    fun get(activity: AppActivity): IAppViewModel {
        return ViewModelProvider(activity).get(AppViewModel::class.java).apply {
            setCallback(activity as? IAppActivity)
        }
    }

    object Note {
        fun get(activity: NoteActivity): INoteViewModel {
            return ViewModelProvider(activity).get(NoteViewModel::class.java).apply {
                setCallback(activity as? INoteActivity)
            }
        }

        fun get(fragment: TextNoteFragment): ITextNoteViewModel {
            return ViewModelProvider(fragment).get(TextNoteViewModel::class.java).apply {
                setCallback(fragment as? TextNoteFragment)
                setParentCallback(fragment.context as? INoteChild)
            }
        }

        fun get(fragment: RollNoteFragment): IRollNoteViewModel {
            return ViewModelProvider(fragment).get(RollNoteViewModel::class.java).apply {
                setCallback(fragment as? RollNoteFragment)
                setParentCallback(fragment.context as? INoteChild)
            }
        }
    }


    fun get(context: Context, callback: IPreferenceFragment): IPreferenceViewModel {
        return PreferenceViewModel(context, callback)
    }

}