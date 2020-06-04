package sgtmelon.scriptum.dagger.module.base

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.control.note.input.InputControl

/**
 * Module for provide control classes
 */
@Module
class ControlModule {

    @Provides
    @ActivityScope
    fun provideInputControl(): IInputControl = InputControl()

}