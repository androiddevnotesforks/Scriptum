package sgtmelon.scriptum.infrastructure.converter

import sgtmelon.scriptum.infrastructure.model.key.SavePeriod

class SavePeriodConverter : ParentEnumConverter<SavePeriod>() {

    override fun toEnum(value: Int): SavePeriod? = SavePeriod.values().getOrNull(value)
}