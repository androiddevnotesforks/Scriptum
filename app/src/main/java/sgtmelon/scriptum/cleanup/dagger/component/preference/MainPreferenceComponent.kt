package sgtmelon.scriptum.cleanup.dagger.component.preference

import androidx.lifecycle.ViewModelStoreOwner
import dagger.BindsInstance
import dagger.Subcomponent
import sgtmelon.scriptum.cleanup.dagger.module.ViewModelModule
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.PreferenceActivity

@ActivityScope
@Subcomponent(modules = [ViewModelModule::class])
interface MainPreferenceComponent {

    fun inject(activity: PreferenceActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun set(owner: ViewModelStoreOwner): Builder

        fun build(): MainPreferenceComponent
    }
}