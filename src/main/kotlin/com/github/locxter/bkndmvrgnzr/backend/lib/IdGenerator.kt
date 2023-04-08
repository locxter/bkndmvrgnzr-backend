package com.github.locxter.bkndmvrgnzr.backend.lib

import java.util.*

object IdGenerator {
    fun next(): String = UUID.randomUUID().toString().replace("-", "")
}