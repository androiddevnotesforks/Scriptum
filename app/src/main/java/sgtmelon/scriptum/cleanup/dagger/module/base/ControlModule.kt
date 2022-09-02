package sgtmelon.scriptum.cleanup.dagger.module.base

import android.content.Context
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.presentation.control.cipher.CipherControl
import sgtmelon.scriptum.cleanup.presentation.control.cipher.ICipherControl
import sgtmelon.scriptum.data.dataSource.FileDataSource
import sgtmelon.scriptum.infrastructure.files.dataSource.FileDataSourceImpl

/**
 * Module for provide control classes.
 */
@Module
class ControlModule {

    @Provides
    @ActivityScope
    fun provideFileControl(context: Context): FileDataSource = FileDataSourceImpl(context)

    @Provides
    @ActivityScope
    fun provideCipherControl(): ICipherControl = CipherControl()

}