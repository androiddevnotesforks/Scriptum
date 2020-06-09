package sgtmelon.scriptum.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment

/**
 * Component for [TextNoteFragment].
 */
@ActivityScope
@Subcomponent(modules = [InteractorModule::class, ViewModelModule::class])
interface TextNoteComponent {

    fun inject(fragment: TextNoteFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: TextNoteFragment): Builder

        fun build(): TextNoteComponent
    }

}