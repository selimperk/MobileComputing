package com.myapp.ui.screens

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.video.AudioConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.myapp.model.room.entities.Challenges
import com.myapp.viewmodel.ChallengesViewModel
import com.myapp.viewmodel.SensorViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ChallengeDetailScreen(
    challenge: Challenges,
    challengesViewModel: ChallengesViewModel? = null,
    sensorViewModel: SensorViewModel? = null
) {
    // ---- Kontext / Lifecycle ----
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Falls kein VM durchgereicht wurde, lokal beziehen
    val sensorVM = sensorViewModel ?: androidx.lifecycle.viewmodel.compose.viewModel<SensorViewModel>()

    // ---- Sichtbarkeiten ----
    var showCamera by remember { mutableStateOf(false) }
    var showAudio by remember { mutableStateOf(false) }
    var showMotion by remember { mutableStateOf(false) }

    // ---- CameraX Controller & States ----
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE)
        }
    }
    var lastMediaUri by remember { mutableStateOf<Uri?>(null) } // zuletzt aufgenommenes Medium
    var lastMediaIsVideo by remember { mutableStateOf(false) }  // Foto/Video unterscheiden
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) } // Anzeige unter Chips
    var cameraError by remember { mutableStateOf<String?>(null) }
    var isRecordingVideo by remember { mutableStateOf(false) }
    var activeRecording by remember { mutableStateOf<Recording?>(null) }

    fun timeStamp() = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        .format(System.currentTimeMillis())

    // ---- Permissions ----
    fun hasPermission(p: String) =
        ContextCompat.checkSelfPermission(context, p) == PackageManager.PERMISSION_GRANTED

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            try { cameraController.bindToLifecycle(lifecycleOwner) } catch (_: Exception) {}
        } else cameraError = "Kamerazugriff verweigert."
    }
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* Start erfolgt per Button */ }
    val activityRecPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* Start erfolgt per Button */ }

    fun requestCamera() {
        val perm = Manifest.permission.CAMERA
        if (hasPermission(perm)) {
            try { cameraController.bindToLifecycle(lifecycleOwner) } catch (_: Exception) {}
        } else {
            val act = context as? Activity
            if (act != null && ActivityCompat.shouldShowRequestPermissionRationale(act, perm)) {
                // optional: Rationale-Dialog
            }
            cameraPermissionLauncher.launch(perm)
        }
    }
    fun requestAudio() {
        val perm = Manifest.permission.RECORD_AUDIO
        if (!hasPermission(perm)) {
            val act = context as? Activity
            if (act != null && ActivityCompat.shouldShowRequestPermissionRationale(act, perm)) {
                // optional: Rationale-Dialog
            }
            audioPermissionLauncher.launch(perm)
        }
    }
    fun requestActivityRecognition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val perm = Manifest.permission.ACTIVITY_RECOGNITION
            if (!hasPermission(perm)) {
                activityRecPermissionLauncher.launch(perm)
            }
        }
    }

    // ---- Foto aufnehmen ----
    fun capturePhoto() {
        val picturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: return
        val photoFile = File(picturesDir, "IMG_${timeStamp()}.jpg")
        val output = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        cameraController.takePicture(
            output,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(result: ImageCapture.OutputFileResults) {
                    val uri = result.savedUri ?: Uri.fromFile(photoFile)
                    lastMediaUri = uri
                    lastMediaIsVideo = false
                    // Thumbnail laden
                    capturedBitmap = android.graphics.BitmapFactory.decodeFile(photoFile.absolutePath)
                    // Kamera deaktivieren & Sektion schließen
                    isRecordingVideo = false
                    activeRecording?.stop()
                    try { cameraController.unbind() } catch (_: Exception) {}
                    showCamera = false
                }
                override fun onError(exc: ImageCaptureException) {
                    cameraError = "Foto fehlgeschlagen: ${exc.message}"
                }
            }
        )
    }

    // ---- Video aufnehmen / stoppen ----
    fun toggleVideoRecording() {
        if (isRecordingVideo) {
            activeRecording?.stop()
            return
        }
        // Audio je nach Permission
        val audioConfig = AudioConfig.create(hasPermission(Manifest.permission.RECORD_AUDIO))
        val moviesDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES) ?: return
        val videoFile = File(moviesDir, "VID_${timeStamp()}.mp4")
        val outputOpts = FileOutputOptions.Builder(videoFile).build()

        try {
            val recording = cameraController.startRecording(
                outputOpts,
                audioConfig,
                ContextCompat.getMainExecutor(context)
            ) { event: VideoRecordEvent ->
                when (event) {
                    is VideoRecordEvent.Start -> {
                        isRecordingVideo = true
                        cameraError = null
                    }
                    is VideoRecordEvent.Finalize -> {
                        isRecordingVideo = false
                        activeRecording = null
                        val uri = event.outputResults.outputUri
                            .takeIf { it != Uri.EMPTY } ?: Uri.fromFile(videoFile)
                        lastMediaUri = uri
                        lastMediaIsVideo = true
                        // Thumbnail aus Video
                        capturedBitmap = makeVideoThumbnail(videoFile.absolutePath)
                        // Kamera deaktivieren & Sektion schließen
                        try { cameraController.unbind() } catch (_: Exception) {}
                        showCamera = false
                        if (event.hasError()) {
                            cameraError = "Video-Fehler: ${event.error}"
                        }
                    }
                    else -> Unit
                }
            }
            activeRecording = recording
        } catch (e: Exception) {
            cameraError = "Video fehlgeschlagen: ${e.message}"
        }
    }

    // ---- Audio UI/State (Recorder im ViewModel) + MiniPlayer ----
    val isRecording by sensorVM.isRecording.observeAsState(false)
    var lastAudioPath by remember { mutableStateOf<String?>(null) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var playPositionMs by remember { mutableStateOf(0) }

    fun startAudioRecording() {
        // neue Datei im Cache
        val outPath = "${context.cacheDir}/rec_${timeStamp()}.3gp"
        lastAudioPath = outPath
        sensorVM.startSensors(context, outPath)
        sensorVM.startRecording()
    }
    fun stopAudioRecording() {
        sensorVM.stopRecording()
        // MiniPlayer zurücksetzen
        isPlaying = false
        mediaPlayer?.release()
        mediaPlayer = null
    }
    fun togglePlayPause() {
        val path = lastAudioPath ?: return
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(path)
                prepare()
                setOnCompletionListener {
                    isPlaying = false
                    playPositionMs = 0
                }
            }
        }
        val mp = mediaPlayer!!
        if (isPlaying) {
            mp.pause()
            playPositionMs = mp.currentPosition
            isPlaying = false
        } else {
            if (playPositionMs > 0) mp.seekTo(playPositionMs)
            mp.start()
            isPlaying = true
        }
    }

    // ---- StepCounter (echter Sensor) ----
    val sensorManager = remember { context.getSystemService(android.content.Context.SENSOR_SERVICE) as SensorManager }
    var stepSensorAvailable by remember { mutableStateOf(false) }
    var baseSteps by remember { mutableStateOf<Int?>(null) }
    var currentSteps by remember { mutableStateOf<Int?>(null) }

    val stepListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val total = event.values.firstOrNull()?.toInt() ?: return
                if (baseSteps == null) baseSteps = total
                currentSteps = (total - (baseSteps ?: total)).coerceAtLeast(0)
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    fun startStepCounter() {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepSensorAvailable = sensor != null
        baseSteps = null
        currentSteps = 0
        if (sensor != null) {
            sensorManager.registerListener(stepListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
    fun stopStepCounter() {
        sensorManager.unregisterListener(stepListener)
    }

    // ---------- UI ----------
    Scaffold(topBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF6F1C))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.Black)
            Spacer(Modifier.weight(1f))
            Text("ChallengeDetails", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }) { insets ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF156082))
                .padding(insets),
            contentPadding = PaddingValues(bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Titel & Beschreibung
            item {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = challenge.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, bottom = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text("Beschreibung", fontStyle = FontStyle.Italic, color = Color.LightGray, fontSize = 14.sp)
                    Text(text = challenge.description ?: "", color = Color.White, fontSize = 16.sp, modifier = Modifier.padding(top = 2.dp))
                    Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(top = 8.dp))
                }
            }

            // Chip-Leiste
            item {
                SensorActionChips(
                    showCamera = showCamera,
                    showAudio = showAudio,
                    showMotion = showMotion,
                    onCameraClick = {
                        showCamera = !showCamera
                        if (showCamera) {
                            requestCamera()
                            try { cameraController.bindToLifecycle(lifecycleOwner) } catch (_: Exception) {}
                        }
                    },
                    onAudioClick = {
                        showAudio = !showAudio
                        if (showAudio) requestAudio()
                    },
                    onMotionClick = {
                        showMotion = !showMotion
                        if (showMotion) requestActivityRecognition()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            // Direkt unter Chips: Vorschauen (Foto/Video-Thumbnail + MiniPlayer)
            if (capturedBitmap != null) {
                item {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            bitmap = capturedBitmap!!.asImageBitmap(),
                            contentDescription = if (lastMediaIsVideo) "Video-Thumbnail" else "Foto",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(Color(0xFF0F4F69), shape = RoundedCornerShape(12.dp))
                        )
                        if (lastMediaIsVideo) {
                            Text("Video aufgenommen", color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(top = 6.dp))
                        }
                    }
                }
            }

            if (lastAudioPath != null) {
                item {
                    MiniAudioPlayer(
                        path = lastAudioPath!!,
                        isPlaying = isPlaying,
                        onToggle = { togglePlayPause() },
                        elapsedProvider = { mediaPlayer?.currentPosition ?: 0 }
                    )
                }
            }

            // Kamera-Sektion (Preview + Buttons)
            if (showCamera) {
                item {
                    LaunchedEffect(Unit) {
                        try { cameraController.bindToLifecycle(lifecycleOwner) } catch (_: Exception) {}
                    }
                    CameraSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        controller = cameraController,
                        isRecording = isRecordingVideo,
                        onCapturePhoto = { capturePhoto() },
                        onToggleVideo = {
                            if (!hasPermission(Manifest.permission.RECORD_AUDIO)) requestAudio()
                            toggleVideoRecording()
                        },
                        onClose = {
                            activeRecording?.stop()
                            try { cameraController.unbind() } catch (_: Exception) {}
                            showCamera = false
                        },
                        errorText = cameraError
                    )
                }
            }

            // Audio-Sektion (Record/Stop)
            if (showAudio) {
                item {
                    AudioSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        isRecording = isRecording,
                        onStart = { startAudioRecording() },
                        onStop = {
                            stopAudioRecording()
                            // Player auf Anfang vorbereiten
                            playPositionMs = 0
                        },
                        onSave = {
                            // Optional: aus Cache in Music/ verschieben – später
                        },
                        onPlayPause = { togglePlayPause() }
                    )
                }
            }

            // Bewegungs-Sektion (StepCounter)
            if (showMotion) {
                item {
                    MotionSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        steps = currentSteps,
                        onStart = { startStepCounter() },
                        onStop = { stopStepCounter() }
                    )
                    if (!stepSensorAvailable) {
                        Text(
                            "Schrittzähler nicht verfügbar (Fallback: kein Zählen).",
                            color = Color.LightGray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }

    // Cleanup Player beim Verlassen
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
            try { cameraController.unbind() } catch (_: Exception) {}
            stopStepCounter()
        }
    }
}

/** Chip-Leiste */
@Composable
private fun SensorActionChips(
    showCamera: Boolean,
    showAudio: Boolean,
    showMotion: Boolean,
    onCameraClick: () -> Unit,
    onAudioClick: () -> Unit,
    onMotionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AssistChip(
            onClick = onCameraClick,
            label = { Text(if (showCamera) "Kamera (aktiv)" else "Kamera") },
            leadingIcon = { Icon(Icons.Default.CameraAlt, null, tint = Color.Black) },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = if (showCamera) Color(0xFFFFE1CC) else Color(0xFFFFF3E9),
                labelColor = Color.Black
            )
        )
        AssistChip(
            onClick = onAudioClick,
            label = { Text(if (showAudio) "Audio (aktiv)" else "Audio") },
            leadingIcon = { Icon(Icons.Default.Mic, null, tint = Color.Black) },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = if (showAudio) Color(0xFFDDE6FF) else Color(0xFFF0F4FF),
                labelColor = Color.Black
            )
        )
        AssistChip(
            onClick = onMotionClick,
            label = { Text(if (showMotion) "Bewegung (aktiv)" else "Bewegung") },
            leadingIcon = { Text("🏃") },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = if (showMotion) Color(0xFFDBF7E3) else Color(0xFFEFFBF2),
                labelColor = Color.Black
            )
        )
    }
}

/** Kamera-Section: Preview + Foto/Video + schließen */
@Composable
private fun CameraSection(
    modifier: Modifier = Modifier,
    controller: LifecycleCameraController,
    isRecording: Boolean,
    onCapturePhoto: () -> Unit,
    onToggleVideo: () -> Unit,
    onClose: () -> Unit,
    errorText: String?
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(Color(0xFF0F4F69), shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        this.controller = controller
                        this.scaleType = PreviewView.ScaleType.FILL_CENTER
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        errorText?.let {
            Text(text = it, color = Color(0xFFFFC0CB), fontSize = 12.sp)
            Spacer(Modifier.height(6.dp))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = onCapturePhoto,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E63CE))
            ) { Text("Foto aufnehmen", color = Color.White) }

            OutlinedButton(
                onClick = onToggleVideo,
                shape = RoundedCornerShape(10.dp)
            ) { Text(if (isRecording) "Video stoppen" else "Video aufnehmen") }

            OutlinedButton(
                onClick = onClose,
                shape = RoundedCornerShape(10.dp)
            ) { Text("Schließen") }
        }
    }
}

/** Audio-Section: Record / Stop / Mini-Player */
@Composable
private fun AudioSection(
    modifier: Modifier = Modifier,
    isRecording: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onSave: () -> Unit,
    onPlayPause: () -> Unit
) {
    Column(modifier = modifier) {
        Text("Audioaufnahme", color = Color.White, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (isRecording) {
                Button(
                    onClick = onStop,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350))
                ) { Text("Stop", color = Color.White) }
            } else {
                Button(
                    onClick = onStart,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8E63CE))
                ) { Text("Aufnahme starten", color = Color.White) }
            }
            OutlinedButton(onClick = onPlayPause, shape = RoundedCornerShape(10.dp)) {
                Text("Abspielen / Pause")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDD82D4))
        ) { Text("Audio speichern", color = Color.White, fontSize = 18.sp) }
    }
}

/** Mini-Player unter den Chips (einfache Version) */
@Composable
private fun MiniAudioPlayer(
    path: String,
    isPlaying: Boolean,
    onToggle: () -> Unit,
    elapsedProvider: () -> Int
) {
    val elapsed by rememberUpdatedState(newValue = elapsedProvider())
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)) {
        Text("Letzte Aufnahme", color = Color.White, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F4F69), shape = RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatMs(elapsed),
                color = Color.White
            )
            OutlinedButton(onClick = onToggle, shape = RoundedCornerShape(10.dp)) {
                Text(if (isPlaying) "Pause" else "Play")
            }
        }
    }
}

/** Bewegungs-Section (StepCounter) */
@Composable
private fun MotionSection(
    modifier: Modifier = Modifier,
    steps: Int?,
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    Column(modifier = modifier) {
        Text("Bewegung / Schritte", color = Color.White, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Schritte: ${steps ?: 0}",
                    color = Color.White,
                    fontSize = 18.sp
                )
                Text(
                    text = "Zählt echte Schritte (TYPE_STEP_COUNTER)",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onStart, shape = RoundedCornerShape(10.dp)) { Text("Start") }
                Button(
                    onClick = onStop,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350))
                ) { Text("Stop", color = Color.White) }
            }
        }
    }
}

/* ---------- Helpers ---------- */

private fun formatMs(ms: Int): String {
    val totalSec = ms / 1000
    val min = totalSec / 60
    val sec = totalSec % 60
    return "%02d:%02d".format(min, sec)
}

private fun makeVideoThumbnail(path: String): Bitmap? {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(path)
        retriever.frameAtTime?.copy(Bitmap.Config.ARGB_8888, false)
    } catch (_: Exception) {
        null
    } finally {
        runCatching { retriever.release() }
    }
}
