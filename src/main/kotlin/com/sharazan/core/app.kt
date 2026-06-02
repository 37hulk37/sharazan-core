package com.sharazan.core

import org.koin.core.Koin

fun AppBuilder.koin(koin: Koin) = apply {
    install(koin)
}