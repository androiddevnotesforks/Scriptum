package sgtmelon.scriptum.infrastructure.screen.main

import android.os.Bundle
import androidx.annotation.IdRes

interface MainViewModel {

    val isStartPage: Boolean

    fun onSaveData(bundle: Bundle)

    fun onSelectItem(@IdRes itemId: Int)

}