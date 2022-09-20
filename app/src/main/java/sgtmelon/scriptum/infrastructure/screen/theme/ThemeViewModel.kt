package sgtmelon.scriptum.infrastructure.screen.theme

import sgtmelon.scriptum.infrastructure.model.key.Theme

interface ThemeViewModel {

    val theme: Theme

    fun isThemeChanged(): Boolean
}