package sgtmelon.common.utils

import sgtmelon.common.test.annotation.RunNone
import java.util.*

@RunNone
fun nextString() = UUID.randomUUID().toString().substring(0, 16)

@RunNone
fun nextShortString() = nextString().substring(0, 4)