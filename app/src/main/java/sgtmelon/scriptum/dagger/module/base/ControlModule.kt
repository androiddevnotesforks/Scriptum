package sgtmelon.scriptum.dagger.module.base

import android.content.Context
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.presentation.control.system.RingtoneControl
import sgtmelon.scriptum.presentation.control.system.callback.IRingtoneControl
import javax.inject.Singleton

/**
 * Module for provide control classes
 */
@Module
class ControlModule {

    @Provides
    @Singleton
    fun provideRingtoneControl(context: Context?): IRingtoneControl = RingtoneControl(context)

}