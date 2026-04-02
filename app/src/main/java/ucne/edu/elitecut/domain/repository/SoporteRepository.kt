package ucne.edu.elitecut.domain.repository

import kotlinx.coroutines.flow.Flow
import ucne.edu.elitecut.data.remote.Resource
import ucne.edu.elitecut.domain.model.Conversacion
import ucne.edu.elitecut.domain.model.MensajeSoporte
import ucne.edu.elitecut.domain.model.TicketSoporte

interface SoporteRepository {
    fun observeMisMensajes(userId: String): Flow<List<MensajeSoporte>>
    suspend fun enviarMensaje(contenido: String): Resource<MensajeSoporte>
    suspend fun getTodosTickets(estado: String?): Resource<List<TicketSoporte>>
    suspend fun getConversacion(usuarioId: Int): Resource<Conversacion>
    suspend fun responderMensaje(mensajeId: Int, contenido: String): Resource<MensajeSoporte>
    suspend fun postPendingMensajes(): Resource<Unit>
    suspend fun syncMisMensajes(): Resource<Unit>
}
