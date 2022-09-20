package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback

import sgtmelon.scriptum.infrastructure.model.key.Theme

interface IAppViewModel {

    val theme: Theme

    fun isThemeChanged(): Boolean
}