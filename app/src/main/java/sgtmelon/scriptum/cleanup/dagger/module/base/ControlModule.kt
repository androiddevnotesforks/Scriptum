package sgtmelon.scriptum.cleanup.dagger.module.base

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.presentation.control.cipher.CipherControl
import sgtmelon.scriptum.cleanup.presentation.control.cipher.ICipherControl

/**
 * Module for provide control classes.
 */
@Module
class ControlModule {

    @Provides
    @ActivityScope
    fun provideCipherControl(): ICipherControl = CipherControl()

}