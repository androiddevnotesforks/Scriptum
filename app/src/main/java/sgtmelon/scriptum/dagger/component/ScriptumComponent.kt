package sgtmelon.scriptum.dagger.component

import dagger.BindsInstance
import dagger.Component
import sgtmelon.scriptum.dagger.module.base.ContextModule
import sgtmelon.scriptum.dagger.module.base.RepoModule
import sgtmelon.scriptum.screen.ui.ScriptumApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, RepoModule::class])
interface ScriptumComponent {

    fun getMainBuilder(): MainComponent.Builder

    fun getRankBuilder(): RankComponent.Builder

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun set(application: ScriptumApplication): Builder

        fun build(): ScriptumComponent
    }

}