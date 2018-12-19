package com.r4hu7.bluarmor.di.modules

import ai.api.android.AIConfiguration
import ai.api.android.AIService
import android.content.Context
import dagger.Module
import dagger.Provides

@Module(includes = [ContextModule::class])
class AiModule {
    @Provides
    fun aIConfiguration(key: String): AIConfiguration {
        return AIConfiguration(
            key,
            ai.api.AIConfiguration.SupportedLanguages.English,
            AIConfiguration.RecognitionEngine.System
        )
    }

    @Provides
    fun aiService(context: Context, config: AIConfiguration): AIService {
        return AIService.getService(context, config)
    }

    @Provides
    fun key(): String {
        /*TODO
        * place in some more secure place*/
        return "47d0418473a7466ea1cabdb05b9e3110"
    }


}