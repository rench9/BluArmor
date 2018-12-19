package com.r4hu7.bluarmor.data.remote

interface OnAIActionReceived {
    fun captureImage(count: Int = 1, interval: Long = 5000)
    fun recordVideo(duration: Long = -1)
    fun getWeather(time: Long = 0)
    fun shareImage(caption: String, origin: String)
    fun unKnownInput()
    fun onWelcomeGreeting()
    fun fetchIntroduction()
    fun speak(msg: String)
}