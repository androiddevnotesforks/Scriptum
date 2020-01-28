package sgtmelon.scriptum.dagger.component

import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.AppModule
import sgtmelon.scriptum.dagger.module.PreferenceModule
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.screen.ui.preference.PreferenceFragment

@ActivityScope
@Subcomponent(modules = [InteractorModule::class, PreferenceModule::class])
interface PreferenceComponent {

    fun inject(fragment: PreferenceFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(fragment: PreferenceFragment): Builder

        fun build(): PreferenceComponent
    }

}