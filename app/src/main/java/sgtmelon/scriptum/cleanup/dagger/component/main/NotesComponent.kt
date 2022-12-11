package sgtmelon.scriptum.cleanup.dagger.component.main

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.infrastructure.screen.main.notes.NotesFragment

/**
 * Component for [NotesFragment].
 */
@ActivityScope
@Subcomponent(modules = [ViewModelModule::class])
interface NotesComponent {

    fun inject(fragment: NotesFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(owner: ViewModelStoreOwner): Builder

        fun build(): NotesComponent
    }
}