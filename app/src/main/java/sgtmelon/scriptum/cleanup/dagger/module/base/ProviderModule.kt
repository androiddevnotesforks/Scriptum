package sgtmelon.scriptum.cleanup.dagger.module.base

import android.content.res.Resources
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.infrastructure.system.dataSource.SummaryDataSourceImpl

/**
 * Module for provide providers classes.
 */
@Module
class ProviderModule {

    @Provides
    @Singleton
    fun provideSummaryProvider(resources: Resources): SummaryDataSource {
        return SummaryDataSourceImpl(resources)
    }
}