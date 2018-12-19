package com.r4hu7.bluarmor.di.component

import ai.api.android.AIService
import com.r4hu7.bluarmor.di.modules.AiModule
import dagger.Component

@Component(modules = [AiModule::class])
interface AiServiceComponent {
    fun aiService(): AIService
}