package sgtmelon.scriptum.cleanup.dagger.module.infrastructure

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListStorageImpl
import kotlin.time.measureTime

@Module
class ListModule {

    @Provides
    fun <T> provideListStorage(): ListStorageImpl<T> = ListStorageImpl()

}