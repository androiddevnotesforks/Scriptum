package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.data.dataSource.system.RingtoneDataSource
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCaseImpl

@Module
class UseCaseModule {

    @Provides
    fun provideGetMelodyListUseCase(dataSource: RingtoneDataSource): GetMelodyListUseCase {
        return GetMelodyListUseCaseImpl(dataSource)
    }
}