package sgtmelon.scriptum.presentation.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.presentation.dagger.ActivityScope
import sgtmelon.scriptum.presentation.dagger.module.base.InteractorModule
import sgtmelon.scriptum.presentation.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.presentation.screen.ui.impl.main.NotesFragment

/**
 * Component for [NotesFragment].
 */
@ActivityScope
@Subcomponent(modules = [InteractorModule::class, ViewModelModule::class])
interface NotesComponent {

    fun inject(fragment: NotesFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: NotesFragment): Builder

        fun build(): NotesComponent
    }

}