package sgtmelon.scriptum.infrastructure.converter

import sgtmelon.scriptum.infrastructure.model.key.Color

class ColorConverter : ParentEnumConverter<Color>() {

    override fun toEnum(value: Int): Color? = Color.values().getOrNull(value)
}