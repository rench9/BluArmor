package com.r4hu7.bluarmor.ui.landing

import ai.api.android.AIService
import ai.api.model.AIError
import ai.api.model.AIResponse
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.r4hu7.bluarmor.R
import com.r4hu7.bluarmor.data.remote.AiResponseHandler
import com.r4hu7.bluarmor.data.remote.OnAIActionReceived
import com.r4hu7.bluarmor.di.component.DaggerAiServiceComponent
import com.r4hu7.bluarmor.di.modules.ContextModule
import com.r4hu7.bluarmor.ui.adapter.ChatAdapter
import kotlinx.android.synthetic.main.activity_landing.*
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import java.io.File
import java.util.*


class LandingActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks, ai.api.AIListener,
    OnAIActionReceived {

    private lateinit var tempCameraImageUri: Uri
    private lateinit var aiService: AIService
    private lateinit var aiResponseHandler: AiResponseHandler

    companion object {
        const val PERMISSIONS_REQUEST_RECORD_AUDIO = 1001
        const val CAPTURE_IMAGE = 901
    }

    private lateinit var tt: TextToSpeech

    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        aiService =
                DaggerAiServiceComponent.builder().contextModule(ContextModule(applicationContext)).build().aiService()
        aiService.setListener(this)

        initRecyclerView()
        initTextToSpeech()
        initAiResponseHandler()
        btnAction.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    seekMicPermission()
                    return@setOnTouchListener false
                }
                MotionEvent.ACTION_UP -> {
                    aiService.stopListening()
                    return@setOnTouchListener false
                }
                else -> false
            }
        }


    }

    private fun initAiResponseHandler() {
        aiResponseHandler = AiResponseHandler(this)
    }

    private fun initTextToSpeech() {
        tt = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { })
        tt.language = Locale.ENGLISH
    }

    private fun initRecyclerView() {
        this.adapter = ChatAdapter()
        rvContainer.adapter = adapter
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                rvContainer.smoothScrollToPosition(positionStart)
            }
        })
    }

    private fun initInput() {
        aiService.startListening()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(applicationContext, "Can't use voice commands, try again!", Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        initInput()
    }

    private fun seekMicPermission() {
        EasyPermissions.requestPermissions(
            PermissionRequest.Builder(
                this,
                PERMISSIONS_REQUEST_RECORD_AUDIO,
                Manifest.permission.RECORD_AUDIO
            )
                .setRationale("Mic permission is necessary for Audio Input")
                .setPositiveButtonText("Ok")
                .setNegativeButtonText("Cancel")
                .build()
        )
    }

    override fun onResult(result: AIResponse?) {
        adapter.addItem(ChatAdapter.Feed(result!!.result.resolvedQuery, R.drawable.shape_circle, true))
        Log.e("onResult", "onResult ${result.result.action}")
        aiResponseHandler.processAction(result)
    }

    override fun onListeningStarted() {
        Log.e("RESULT", "onListeningStarted")
    }

    override fun onAudioLevel(level: Float) {
    }

    override fun onError(error: AIError?) {
        Log.e("RESULT", "onError ${error!!.message}")
        speak("Didn't get that, try again")
    }

    override fun onListeningCanceled() {
        Log.e("RESULT", "onListeningCanceled")
    }

    override fun onListeningFinished() {
        Log.e("RESULT", "onListeningFinished")
    }

    override fun captureImage(count: Int, interval: Long) {
        if (count > 1)
            speak("Will capture $count images every ${interval / 1000} seconds")
        for (i in 1..count) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                /* temp Image path*/
                val file =
                    File.createTempFile("image-${System.currentTimeMillis()}", ".jpg", applicationContext.cacheDir)
                this.tempCameraImageUri =
                        FileProvider.getUriForFile(applicationContext, "$packageName.fileprovider", file)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempCameraImageUri)
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE)

            } else
                Toast.makeText(applicationContext, "No default camera app found!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun recordVideo(duration: Long) {
    }

    override fun getWeather(time: Long) {
    }

    override fun shareImage(caption: String, origin: String) {

        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here")
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, caption)
        startActivity(Intent.createChooser(sharingIntent, "Share via"))

    }

    override fun unKnownInput() {
        speak("Didn't get that, try again")
    }

    override fun onWelcomeGreeting() {
        speak("hello")
    }

    override fun fetchIntroduction() {
        speak(
            "Try some commands like\n\n" +
                    "How long i am driving\n" +
                    "Capture 3 images\n" +
                    "Share my recent image"
        )
    }

    override fun speak(msg: String) {
        tt.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null)
        adapter.addItem(ChatAdapter.Feed(msg, R.drawable.shape_circle, false))
    }

}
