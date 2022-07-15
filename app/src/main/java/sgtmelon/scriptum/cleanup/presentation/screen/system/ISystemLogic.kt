package sgtmelon.scriptum.cleanup.presentation.screen.system

import android.content.Context

/**
 * Interface for [SystemLogic].
 */
interface ISystemLogic : ISystemBridge {

    fun onCreate(context: Context)

    fun onDestroy(context: Context)

}