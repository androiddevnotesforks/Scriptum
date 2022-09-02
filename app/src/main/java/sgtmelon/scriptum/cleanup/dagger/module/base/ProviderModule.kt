package sgtmelon.scriptum.cleanup.dagger.module.base

import android.content.res.Resources
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.infrastructure.provider.SummaryProvider
import sgtmelon.scriptum.infrastructure.provider.SummaryProviderImpl

/**
 * Module for provide providers classes.
 */
@Module
class ProviderModule {

    @Provides
    @Singleton
    fun provideSummaryProvider(resources: Resources): SummaryProvider {
        return SummaryProviderImpl(resources)
    }
}