package com.gulshansutey.mycomposeplayground.ultis

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import com.gulshansutey.mycomposeplayground.BuildConfig

class Ref(var value: Int)

@Composable
inline fun LogCompositions(tag: String) {
    val ref = remember { Ref(0) }
    SideEffect { ref.value++ }
    println("$tag Compositions: ${ref.value}")
}
