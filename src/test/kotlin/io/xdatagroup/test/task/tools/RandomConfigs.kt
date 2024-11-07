package io.xdatagroup.test.task.tools

import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters

val easyRandom = EasyRandom(
    EasyRandomParameters()
        .overrideDefaultInitialization(true)
        .collectionSizeRange(1, 2)
        .objectPoolSize(10)
)