package ucne.edu.elitecut.domain.usecase.CitaUseCase

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import ucne.edu.elitecut.data.syncworker.CitaSyncWorker

class TriggerCitaSyncUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke() {
        val request = OneTimeWorkRequestBuilder<CitaSyncWorker>().build()
        WorkManager.getInstance(context).enqueue(request)
    }
}
