package sgtmelon.scriptum.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.RankModule
import sgtmelon.scriptum.dagger.module.RollNoteModule
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment

@ActivityScope
@Subcomponent(modules = [InteractorModule::class, RollNoteModule::class])
interface RollNoteComponent {

    fun inject(fragment: RollNoteFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: RollNoteFragment): Builder

        fun build(): RollNoteComponent
    }

}