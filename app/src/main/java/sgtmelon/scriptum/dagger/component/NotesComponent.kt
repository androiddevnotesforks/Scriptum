package sgtmelon.scriptum.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.NotesModule
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.screen.ui.main.NotesFragment

@ActivityScope
@Subcomponent(modules = [InteractorModule::class, NotesModule::class])
interface NotesComponent {

    fun inject(fragment: NotesFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: NotesFragment): Builder

        fun build(): NotesComponent
    }

}