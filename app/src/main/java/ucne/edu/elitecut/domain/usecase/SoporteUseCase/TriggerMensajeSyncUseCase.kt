package ucne.edu.elitecut.domain.usecase.SoporteUseCase

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import ucne.edu.elitecut.data.syncworker.MensajeSyncWorker

class TriggerMensajeSyncUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke() {
        val request = OneTimeWorkRequestBuilder<MensajeSyncWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}
