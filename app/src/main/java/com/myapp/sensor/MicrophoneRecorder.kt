val recorder = MediaRecorder().apply {
    setAudioSource(MediaRecorder.AudioSource.MIC)
    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
    setOutputFile("path_to_your_file.3gp")
    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
    prepare()
    start()
}