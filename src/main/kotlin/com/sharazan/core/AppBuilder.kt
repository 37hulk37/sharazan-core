package com.sharazan.core

import com.sharazan.core.pipeline.Phase
import com.sharazan.core.pipeline.Pipeline
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

class AppBuilder(
    appModule: Module
) {

    private val modules = mutableListOf<Module>(appModule)

    fun addModule(module: Module) {
        modules.add(module)
    }


    fun build(): Application {
        val pipelineModule = module {
            single {
                Pipeline(getAll<Phase>())
            }
        }
        modules.add(pipelineModule)

        val koinApplication = startKoin {
            modules(modules)
        }
        return Application(koinApplication)
    }

}