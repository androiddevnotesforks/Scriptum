package sgtmelon.scriptum.dagger.module.base

import android.content.Context
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.other.ActivityScope
import sgtmelon.scriptum.presentation.control.cipher.CipherControl
import sgtmelon.scriptum.presentation.control.cipher.ICipherControl
import sgtmelon.scriptum.presentation.control.file.FileControl
import sgtmelon.scriptum.presentation.control.file.IFileControl
import sgtmelon.scriptum.presentation.control.system.RingtoneControl
import sgtmelon.scriptum.presentation.control.system.callback.IRingtoneControl

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