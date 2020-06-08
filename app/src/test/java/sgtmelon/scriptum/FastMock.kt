package sgtmelon.scriptum

import io.mockk.mockkStatic

object FastMock {

    fun timeExtension() = mockkStatic("sgtmelon.extension.TimeExtensionUtils")

    fun listExtension() = mockkStatic("sgtmelon.scriptum.extension.ListExtensionUtils")

}