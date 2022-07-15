package sgtmelon.scriptum.cleanup.dagger.module.base

import android.content.Context
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.presentation.control.cipher.CipherControl
import sgtmelon.scriptum.cleanup.presentation.control.cipher.ICipherControl
import sgtmelon.scriptum.cleanup.presentation.control.file.FileControl
import sgtmelon.scriptum.cleanup.presentation.control.file.IFileControl
import sgtmelon.scriptum.cleanup.presentation.control.system.RingtoneControl
import sgtmelon.scriptum.cleanup.presentation.control.system.callback.IRingtoneControl

/**
 * Module for provide control classes.
 */
@Module
class ControlModule {

    @Provides
    @ActivityScope
    fun provideRingtoneControl(context: Context): IRingtoneControl = RingtoneControl(context)

    @Provides
    @ActivityScope
    fun provideFileControl(context: Context): IFileControl = FileControl(context)

    @Provides
    @ActivityScope
    fun provideCipherControl(): ICipherControl = CipherControl()

}