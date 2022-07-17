package sgtmelon.scriptum.infrastructure.converter

abstract class ParentEnumConverter<E : Enum<E>> {

    fun toInt(value: E): Int = value.ordinal

    abstract fun toEnum(value: Int): E?
}