package sgtmelon.scriptum.cleanup.dagger.module

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.develop.data.DevelopRepo
import sgtmelon.scriptum.develop.domain.DevelopInteractor
import sgtmelon.scriptum.develop.domain.DevelopInteractorImpl

@Deprecated("Convert to use cases if possible")
@Module
class InteractorModule {

    @Provides
    @ActivityScope
    fun provideDevelopInteractor(repository: DevelopRepo): DevelopInteractor {
        return DevelopInteractorImpl(repository)
    }
}