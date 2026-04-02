package ucne.edu.elitecut.data.syncworker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.repository.BarberoRepository

@HiltWorker
class BarberoSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val barberoRepository: BarberoRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return when (barberoRepository.syncBarberos()) {
            is Resource.Success -> Result.success()
            is Resource.Error -> Result.retry()
            else -> Result.retry()
        }
    }
}
