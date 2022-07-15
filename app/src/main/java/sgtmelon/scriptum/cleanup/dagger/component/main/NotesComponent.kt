package sgtmelon.scriptum.cleanup.dagger.component.main

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.dagger.module.base.InteractorModule
import sgtmelon.scriptum.cleanup.dagger.module.base.ViewModelModule
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.NotesFragment

/**
 * Component for [NotesFragment].
 */
@ActivityScope
@Subcomponent(modules = [
    InteractorModule::class,
    ViewModelModule::class
])
interface NotesComponent {

    fun inject(fragment: NotesFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: NotesFragment): Builder

        fun build(): NotesComponent
    }
}