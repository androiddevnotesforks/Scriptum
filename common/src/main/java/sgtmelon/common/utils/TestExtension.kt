package sgtmelon.common.utils

import java.util.UUID
import sgtmelon.common.test.annotation.RunNone

@RunNone
fun nextString() = UUID.randomUUID().toString().substring(0, 16)

@RunNone
fun nextShortString() = nextString().substring(0, 4)