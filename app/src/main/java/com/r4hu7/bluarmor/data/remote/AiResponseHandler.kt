package com.r4hu7.bluarmor.data.remote

import ai.api.model.AIResponse
import com.google.gson.JsonElement
import java.lang.ref.WeakReference
import java.util.*

class AiResponseHandler(onAIActionReceivedListener: OnAIActionReceived) {
    private var onAIActionReceived = WeakReference<OnAIActionReceived>(onAIActionReceivedListener)

    fun processAction(result: AIResponse) {
        when (result.result.action) {
            AiIntents.WELCOME -> {
                onAIActionReceived.get()?.onWelcomeGreeting()
            }
            AiIntents.UNKNOWN -> {
                onAIActionReceived.get()?.unKnownInput()
            }
            AiIntents.INTRODUCTION -> {
                onAIActionReceived.get()?.fetchIntroduction()
            }
            AiIntents.DISTANCE -> {
                onAIActionReceived.get()?.speak(getDistance())
            }
            AiIntents.CAPTURE_SNAP -> {
                captureImage(result)
            }
            AiIntents.SHARE_MEDIA -> {
                shareImage(result)
            }
        }
    }

    private fun getDistance(): String {
        /*TODO
        * Replace dummy data with actual data*/

        return "You have travelled ${Random().nextInt(100)} kilometers"
    }

    private fun captureImage(result: AIResponse) {
        var count = 1
        var interval = 5000L
        val param = result.result.parameters
        if (param.containsKey(AiParameters.NUMBER)) {
            val js: JsonElement? = param[AiParameters.NUMBER]
            count = js!!.asInt
        }
        if (param.containsKey(AiParameters.INTERVAL)) {
            val js: JsonElement? = param[AiParameters.INTERVAL]
            interval = js!!.asLong
        }
        onAIActionReceived.get()?.captureImage(count, interval)
    }

    private fun shareImage(result: AIResponse) {
        var dest = "unknown"
        var caption = "No caption given"
        val param = result.result.parameters
        if (param.containsKey(AiParameters.SHARE_DEST)) {
            val js: JsonElement? = param[AiParameters.SHARE_DEST]
            dest = js!!.asString
        }
        if (param.containsKey(AiParameters.CAPTION)) {
            caption = result.result.resolvedQuery.split("caption")[1]
        }
        onAIActionReceived.get()?.shareImage(
            caption, dest
        )
    }
}