package com.sharazan.core

import java.io.Closeable

interface Lifecycle: Closeable {

    fun started()

}