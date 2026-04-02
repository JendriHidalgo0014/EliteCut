package ucne.edu.elitecut.data.remote

import retrofit2.Response
import retrofit2.http.*
import ucne.edu.elitecut.data.remote.dtos.ActualizarBarberoDto
import ucne.edu.elitecut.data.remote.dtos.ActualizarCitaDto
import ucne.edu.elitecut.data.remote.dtos.ApiResponseDto
import ucne.edu.elitecut.data.remote.dtos.AuthResponseDto
import ucne.edu.elitecut.data.remote.dtos.BarberoDetailDto
import ucne.edu.elitecut.data.remote.dtos.BarberoListDto
import ucne.edu.elitecut.data.remote.dtos.CambiarEstadoDto
import ucne.edu.elitecut.data.remote.dtos.CitaAdminDto
import ucne.edu.elitecut.data.remote.dtos.CitaResponseDto
import ucne.edu.elitecut.data.remote.dtos.ConfirmacionCitaDto
import ucne.edu.elitecut.data.remote.dtos.ConversacionDto
import ucne.edu.elitecut.data.remote.dtos.CrearBarberoDto
import ucne.edu.elitecut.data.remote.dtos.CrearCitaDto
import ucne.edu.elitecut.data.remote.dtos.DashboardStatsDto
import ucne.edu.elitecut.data.remote.dtos.EnviarMensajeDto
import ucne.edu.elitecut.data.remote.dtos.HorariosDisponiblesDto
import ucne.edu.elitecut.data.remote.dtos.ImagenCorteDto
import ucne.edu.elitecut.data.remote.dtos.LoginRequestDto
import ucne.edu.elitecut.data.remote.dtos.MensajeResponseDto
import ucne.edu.elitecut.data.remote.dtos.PagoEstablecimientoRequestDto
import ucne.edu.elitecut.data.remote.dtos.PagoResponseDto
import ucne.edu.elitecut.data.remote.dtos.PagoTarjetaRequestDto
import ucne.edu.elitecut.data.remote.dtos.RegisterRequestDto
import ucne.edu.elitecut.data.remote.dtos.ResponderMensajeDto
import ucne.edu.elitecut.data.remote.dtos.TicketSoporteDto
import ucne.edu.elitecut.data.remote.dtos.UsuarioDto
import ucne.edu.elitecut.data.remote.dtos.UsuarioListDto

interface EliteCutApi {

    // ========================
    // AUTH (4 endpoints)
    // ========================

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<ApiResponseDto<AuthResponseDto>>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<ApiResponseDto<AuthResponseDto>>

    @GET("api/auth/me")
    suspend fun getCurrentUser(): Response<ApiResponseDto<UsuarioDto>>

    @POST("api/auth/logout")
    suspend fun logout(): Response<ApiResponseDto<String>>

    // ========================
    // BARBEROS (6 endpoints)
    // ========================

    @GET("api/barberos")
    suspend fun getBarberos(): Response<ApiResponseDto<List<BarberoListDto>>>

    @GET("api/barberos/{id}")
    suspend fun getBarbero(@Path("id") id: Int): Response<ApiResponseDto<BarberoDetailDto>>

    @POST("api/barberos")
    suspend fun crearBarbero(@Body request: CrearBarberoDto): Response<ApiResponseDto<BarberoDetailDto>>

    @PUT("api/barberos/{id}")
    suspend fun actualizarBarbero(@Path("id") id: Int, @Body request: ActualizarBarberoDto): Response<ApiResponseDto<BarberoDetailDto>>

    @DELETE("api/barberos/{id}")
    suspend fun eliminarBarbero(@Path("id") id: Int): Response<ApiResponseDto<Boolean>>

    @GET("api/barberos/{id}/galeria")
    suspend fun getGaleria(@Path("id") barberoId: Int): Response<ApiResponseDto<List<ImagenCorteDto>>>

    // ========================
    // CITAS (7 endpoints)
    // ========================

    @POST("api/citas")
    suspend fun crearCita(@Body request: CrearCitaDto): Response<ApiResponseDto<ConfirmacionCitaDto>>

    @GET("api/citas/mis-citas")
    suspend fun getMisCitas(@Query("filtro") filtro: String? = null): Response<ApiResponseDto<List<CitaResponseDto>>>

    @GET("api/citas/{id}")
    suspend fun getCita(@Path("id") id: Int): Response<ApiResponseDto<CitaResponseDto>>

    @PUT("api/citas/{id}")
    suspend fun actualizarCita(@Path("id") id: Int, @Body request: ActualizarCitaDto): Response<ApiResponseDto<CitaResponseDto>>

    @PUT("api/citas/{id}/estado")
    suspend fun cambiarEstadoCita(@Path("id") id: Int, @Body request: CambiarEstadoDto): Response<ApiResponseDto<CitaResponseDto>>

    @GET("api/citas/todas")
    suspend fun getAllCitas(
        @Query("filtro") filtro: String? = null,
        @Query("barberoId") barberoId: Int? = null,
        @Query("estado") estado: String? = null
    ): Response<ApiResponseDto<List<CitaAdminDto>>>

    @GET("api/citas/horarios-disponibles")
    suspend fun getHorariosDisponibles(
        @Query("barberoId") barberoId: Int,
        @Query("fecha") fecha: String
    ): Response<ApiResponseDto<HorariosDisponiblesDto>>

    // ========================
    // PAGOS (2 endpoints)
    // ========================

    @POST("api/pagos/tarjeta")
    suspend fun pagoTarjeta(@Body request: PagoTarjetaRequestDto): Response<ApiResponseDto<PagoResponseDto>>

    @POST("api/pagos/establecimiento")
    suspend fun pagoEstablecimiento(@Body request: PagoEstablecimientoRequestDto): Response<ApiResponseDto<PagoResponseDto>>

    // ========================
    // SOPORTE (5 endpoints)
    // ========================

    @POST("api/soporte/mensajes")
    suspend fun enviarMensaje(@Body request: EnviarMensajeDto): Response<ApiResponseDto<MensajeResponseDto>>

    @GET("api/soporte/mis-mensajes")
    suspend fun getMisMensajes(): Response<ApiResponseDto<List<MensajeResponseDto>>>

    @GET("api/soporte/todos")
    suspend fun getTodosTickets(@Query("estado") estado: String? = null): Response<ApiResponseDto<List<TicketSoporteDto>>>

    @GET("api/soporte/conversacion/{usuarioId}")
    suspend fun getConversacion(@Path("usuarioId") usuarioId: Int): Response<ApiResponseDto<ConversacionDto>>

    @POST("api/soporte/responder/{mensajeId}")
    suspend fun responderMensaje(@Path("mensajeId") mensajeId: Int, @Body request: ResponderMensajeDto): Response<ApiResponseDto<MensajeResponseDto>>

    // ========================
    // USUARIOS / DASHBOARD (4 endpoints)
    // ========================

    @GET("api/usuarios")
    suspend fun getUsuarios(): Response<ApiResponseDto<List<UsuarioListDto>>>

    @GET("api/usuarios/{id}")
    suspend fun getUsuario(@Path("id") id: Int): Response<ApiResponseDto<UsuarioListDto>>

    @DELETE("api/usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Int): Response<ApiResponseDto<Boolean>>

    @GET("api/dashboard/stats")
    suspend fun getDashboardStats(): Response<ApiResponseDto<DashboardStatsDto>>
}
