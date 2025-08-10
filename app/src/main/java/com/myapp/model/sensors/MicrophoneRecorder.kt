package com.myapp.model.sensors

import android.media.MediaRecorder
import android.util.Log

class MicrophoneRecorder(private val outputFilePath: String) {

    private var recorder: MediaRecorder? = null

    fun startRecording() {
        try {
            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setOutputFile(outputFilePath)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                prepare()
                start()
            }
        } catch (e: Exception) {
            Log.e("MicrophoneRecorder", "Start failed", e)
        }
    }

    fun stopRecording() {
        try {
            recorder?.apply {
                stop()
                release()
            }
        } catch (e: Exception) {
            Log.e("MicrophoneRecorder", "Stop failed", e)
        } finally {
            recorder = null
        }
    }
}