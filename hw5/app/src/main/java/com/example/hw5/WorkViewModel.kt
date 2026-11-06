package com.example.hw5

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkViewModel : ViewModel() {
    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> get() = _progress

    private val _status = MutableLiveData("Idle")
    val status: LiveData<String> get() = _status

    private val _resultStatus = MutableLiveData("Idle")
    val resultStatus: LiveData<String> get() = _resultStatus

    private val _isWorking = MutableLiveData(false)
    val isWorking: LiveData<Boolean> get() = _isWorking

    private var workerThread: Thread? = null
    private val handler = Handler(Looper.getMainLooper())
    private var isCanceled = false

    fun startWork() {
        if (_isWorking.value == true) return
        isCanceled = false
        _isWorking.value = true
        _progress.value = 0
        _status.value = "準備中..."
        _resultStatus.value = "準備中"

        workerThread = Thread {
            try {
                // Preparation phase (2 seconds)
                Thread.sleep(2000)
                if (isCanceled) return@Thread

                handler.post {
                    _status.value = "Working.. 0%"
                    _resultStatus.value = "工作中"
                }

                // Working phase (0 to 100)
                for (i in 1..100) {
                    if (isCanceled) return@Thread
                    Thread.sleep(100)
                    handler.post {
                        _progress.value = i
                        _status.value = "Working.. $i%"
                    }
                }

                if (!isCanceled) {
                    handler.post {
                        _status.value = "背景工作結束"
                        _resultStatus.value = "背景工作結束"
                        _isWorking.value = false
                    }
                }
            } catch (e: InterruptedException) {
                // Interrupted
            }
        }.apply { start() }
    }

    fun cancelWork() {
        isCanceled = true
        workerThread?.interrupt()
        workerThread = null
        handler.post {
            _status.value = "Canceled"
            _resultStatus.value = "Canceled"
            _isWorking.value = false
            _progress.value = 0
        }
    }
}
