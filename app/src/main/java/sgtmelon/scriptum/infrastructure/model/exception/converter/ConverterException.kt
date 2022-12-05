package sgtmelon.scriptum.infrastructure.model.exception.converter

/**
 * Exception for detecting bad cases when convert one type into another. And can't determinate
 * value before converting (because it's dynamic id or null value).
 *
 * Data class needed mainly for testing.
 */
data class ConverterException(val desc: String) :
    Throwable("Invalid case, can't convert variable ($desc)")