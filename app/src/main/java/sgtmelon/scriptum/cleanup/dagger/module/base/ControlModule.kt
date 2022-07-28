package sgtmelon.scriptum.cleanup.dagger.module.base

import android.content.Context
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.presentation.control.cipher.CipherControl
import sgtmelon.scriptum.cleanup.presentation.control.cipher.ICipherControl
import sgtmelon.scriptum.cleanup.presentation.control.file.FileControl
import sgtmelon.scriptum.cleanup.presentation.control.file.IFileControl
import sgtmelon.scriptum.infrastructure.provider.RingtoneProvider
import sgtmelon.scriptum.infrastructure.provider.RingtoneProviderImpl

/**
 * Module for provide control classes.
 */
@Module
class ControlModule {

    @Provides
    @ActivityScope
    fun provideRingtoneControl(context: Context): RingtoneProvider = RingtoneProviderImpl(context)

    @Provides
    @ActivityScope
    fun provideFileControl(context: Context): IFileControl = FileControl(context)

    @Provides
    @ActivityScope
    fun provideCipherControl(): ICipherControl = CipherControl()

}