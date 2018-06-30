package com.grabber.widget.services

import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WidgetWorkerFactory {
    fun createPeriodic() {
        val myConstraints = Constraints.Builder()
                .setRequiresStorageNotLow(true)
                .build()

        val request = PeriodicWorkRequest
                .Builder(WidgetWorker::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(myConstraints)
                .addTag(WidgetWorker.PERIODIC_SYNC)
                .build()

        WorkManager.getInstance()?.cancelAllWorkByTag(WidgetWorker.PERIODIC_SYNC)
        WorkManager.getInstance()?.enqueue(request)
    }

    fun createOneTime() {
        val myConstraints = Constraints.Builder()
                .setRequiresStorageNotLow(true)
                .build()

        val request = OneTimeWorkRequest.Builder(WidgetWorker::class.java)
                .setConstraints(myConstraints)
                .addTag(WidgetWorker.ONETIME_SYNC)
                .build()

        WorkManager.getInstance()?.cancelAllWorkByTag(WidgetWorker.ONETIME_SYNC)
        WorkManager.getInstance()?.enqueue(request)
    }
}