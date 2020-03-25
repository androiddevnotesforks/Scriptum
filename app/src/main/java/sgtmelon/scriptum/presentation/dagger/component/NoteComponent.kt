package sgtmelon.scriptum.presentation.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.presentation.dagger.ActivityScope
import sgtmelon.scriptum.presentation.dagger.module.NoteModule
import sgtmelon.scriptum.presentation.dagger.module.base.InteractorModule
import sgtmelon.scriptum.presentation.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity

/**
 * Component for [NoteActivity].
 */
@ActivityScope
@Subcomponent(modules = [InteractorModule::class, ViewModelModule::class, NoteModule::class])
interface NoteComponent {

    fun inject(activity: NoteActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(activity: NoteActivity): Builder

        fun build(): NoteComponent
    }

}