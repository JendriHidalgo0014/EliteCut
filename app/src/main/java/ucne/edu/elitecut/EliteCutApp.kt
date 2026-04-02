package ucne.edu.elitecut

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import ucne.edu.elitecut.data.MyWorkerFactory
import javax.inject.Inject

@HiltAndroidApp
class EliteCutApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: MyWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}