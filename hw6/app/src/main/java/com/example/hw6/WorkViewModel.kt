package com.example.hw6

import android.app.Application
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

enum class WorkStatus {
    IDLE,
    PREPARING,
    WORKING,
    FINISHED,
    CANCELED
}

class WorkViewModel(application: Application) : AndroidViewModel(application) {

    private val _status = MutableLiveData(WorkStatus.IDLE)
    val status: LiveData<WorkStatus> get() = _status

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> get() = _progress

    private var workThread: Thread? = null
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())

    fun startWork() {
        stopWorkThread()
        stopMusic()

        _status.value = WorkStatus.PREPARING
        _progress.value = 0

        val thread = Thread {
            try {
                // Preparation stage
                Thread.sleep(1500)

                if (Thread.currentThread().isInterrupted) return@Thread

                handler.post {
                    _status.value = WorkStatus.WORKING
                    startMusic()
                }

                // Background working stage
                for (i in 1..100) {
                    if (Thread.currentThread().isInterrupted) return@Thread

                    Thread.sleep(50)

                    handler.post {
                        _progress.value = i
                    }
                }

                if (Thread.currentThread().isInterrupted) return@Thread

                handler.post {
                    _status.value = WorkStatus.FINISHED
                    _progress.value = 100
                    stopMusic()
                }
            } catch (_: InterruptedException) {
                handler.post {
                    _status.value = WorkStatus.CANCELED
                    stopMusic()
                }
            }
        }
        workThread = thread
        thread.start()
    }

    fun cancelWork() {
        if (workThread?.isAlive == true) {
            stopWorkThread()
        }
        _status.value = WorkStatus.CANCELED
        stopMusic()
    }

    private fun startMusic() {
        stopMusic()
        try {
            mediaPlayer = MediaPlayer.create(getApplication(), R.raw.background_music)?.apply {
                isLooping = true
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopMusic() {
        try {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()
                }
                player.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaPlayer = null
        }
    }

    private fun stopWorkThread() {
        workThread?.interrupt()
        workThread = null
    }

    override fun onCleared() {
        super.onCleared()
        stopWorkThread()
        stopMusic()
    }
}