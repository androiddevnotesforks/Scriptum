package sgtmelon.scriptum.infrastructure.converter

import sgtmelon.scriptum.infrastructure.model.key.Theme

class ThemeConverter : ParentEnumConverter<Theme>() {

    override fun toEnum(value: Int): Theme? = Theme.values().getOrNull(value)
}