package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCaseImpl
import sgtmelon.scriptum.infrastructure.provider.RingtoneProvider

@Module
class UseCaseModule {

    @Provides
    fun provideGetMelodyListUseCase(ringtoneProvider: RingtoneProvider): GetMelodyListUseCase {
        return GetMelodyListUseCaseImpl(ringtoneProvider)
    }
}