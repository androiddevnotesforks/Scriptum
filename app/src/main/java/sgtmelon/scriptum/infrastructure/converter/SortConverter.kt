package sgtmelon.scriptum.infrastructure.converter

import sgtmelon.scriptum.infrastructure.model.key.Sort

class SortConverter : ParentEnumConverter<Sort>() {

    override fun toEnum(value: Int): Sort? = Sort.values().getOrNull(value)
}