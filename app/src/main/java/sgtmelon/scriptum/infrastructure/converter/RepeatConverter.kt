package sgtmelon.scriptum.infrastructure.converter

import sgtmelon.scriptum.infrastructure.model.key.Repeat

class RepeatConverter : ParentEnumConverter<Repeat>() {

    override fun toEnum(value: Int): Repeat? = Repeat.values().getOrNull(value)
}