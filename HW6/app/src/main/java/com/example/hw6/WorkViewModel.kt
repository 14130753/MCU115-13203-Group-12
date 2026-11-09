package com.example.hw6

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class WorkStatus {
    IDLE,
    PREPARING,
    WORKING,
    FINISHED,
    CANCELED
}

data class WorkUiState(
    val status: WorkStatus = WorkStatus.IDLE,
    val progress: Int = 0,
    val progressText: String = "Idle",
    val resultText: String = "Idle"
)

class WorkViewModel : ViewModel() {

    private val _uiState = MutableLiveData(WorkUiState())
    val uiState: LiveData<WorkUiState> = _uiState

    private val handler = Handler(Looper.getMainLooper())
    private var workerThread: Thread? = null
    private var mediaPlayer: MediaPlayer? = null

    @Synchronized
    fun startWork(context: Context) {
        cancelWorkInternal()

        _uiState.postValue(
            WorkUiState(
                status = WorkStatus.PREPARING,
                progress = 0,
                progressText = "Preparing...",
                resultText = "Preparing..."
            )
        )

        val appContext = context.applicationContext

        workerThread = Thread {
            try {
                Thread.sleep(1000)
                if (Thread.currentThread().isInterrupted) return@Thread

                handler.post {
                    startMusic(appContext)
                }

                for (p in 1..100) {
                    if (Thread.currentThread().isInterrupted) {
                        break
                    }
                    Thread.sleep(80)

                    _uiState.postValue(
                        WorkUiState(
                            status = WorkStatus.WORKING,
                            progress = p,
                            progressText = "$p%",
                            resultText = "Working..."
                        )
                    )
                }

                if (Thread.currentThread().isInterrupted) return@Thread

                handler.post {
                    stopMusic()
                }
                _uiState.postValue(
                    WorkUiState(
                        status = WorkStatus.FINISHED,
                        progress = 100,
                        progressText = "背景工作結束!",
                        resultText = "背景工作結束!"
                    )
                )

            } catch (e: InterruptedException) {
                handler.post {
                    stopMusic()
                }
                _uiState.postValue(
                    WorkUiState(
                        status = WorkStatus.CANCELED,
                        progress = 0,
                        progressText = "Canceled",
                        resultText = "Canceled"
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                handler.post {
                    stopMusic()
                }
                _uiState.postValue(
                    WorkUiState(
                        status = WorkStatus.FINISHED,
                        progress = 100,
                        progressText = "背景工作結束!",
                        resultText = "背景工作結束!"
                    )
                )
            }
        }.apply { start() }
    }

    fun cancelWork() {
        cancelWorkInternal()
        _uiState.postValue(
            WorkUiState(
                status = WorkStatus.CANCELED,
                progress = 0,
                progressText = "Canceled",
                resultText = "Canceled"
            )
        )
    }

    private fun cancelWorkInternal() {
        workerThread?.interrupt()
        workerThread = null
        handler.post {
            stopMusic()
        }
    }

    private fun startMusic(context: Context) {
        stopMusic()
        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.background_music)?.apply {
                isLooping = true
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopMusic() {
        try {
            mediaPlayer?.let { mp ->
                if (mp.isPlaying) {
                    mp.stop()
                }
                mp.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaPlayer = null
        }
    }

    override fun onCleared() {
        cancelWorkInternal()
    }
}
