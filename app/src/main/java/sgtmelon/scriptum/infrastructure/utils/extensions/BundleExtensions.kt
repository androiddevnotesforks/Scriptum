package sgtmelon.scriptum.infrastructure.utils.extensions

import android.os.Bundle
import sgtmelon.scriptum.infrastructure.converter.key.ParentEnumConverter

fun <E : Enum<E>> Bundle.putEnum(key: String, converter: ParentEnumConverter<E>, value: E) {
    putInt(key, converter.toInt(value))
}

fun <E : Enum<E>> Bundle.getEnum(key: String, default: Int, converter: ParentEnumConverter<E>): E? {
    val ordinal = getInt(key, default).takeIf { it != default } ?: return null
    return converter.toEnum(ordinal)
}