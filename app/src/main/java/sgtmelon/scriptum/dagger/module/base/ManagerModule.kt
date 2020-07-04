package sgtmelon.scriptum.dagger.module.base

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.presentation.control.system.RingtoneControl
import sgtmelon.scriptum.presentation.control.system.callback.IRingtoneControl
import javax.inject.Singleton

/**
 * Module for provide android manager classes.
 */
@Module
class ManagerModule {

    // TODO remove module

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Singleton
    fun provideRingtoneControl(context: Context): IRingtoneControl = RingtoneControl(context)

}