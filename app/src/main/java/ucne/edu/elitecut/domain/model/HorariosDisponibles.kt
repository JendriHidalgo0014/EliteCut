package ucne.edu.elitecut.domain.model

data class HorariosDisponibles(
    val fecha: String = "",
    val barberoId: Int = 0,
    val nombreBarbero: String = "",
    val horariosDisponibles: List<String> = emptyList()
)
