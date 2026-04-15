package ucne.edu.elitecut.presentation.utils

import ucne.edu.elitecut.domain.model.ImagenCorte
import ucne.edu.elitecut.presentation.tareas.administradores.nuevoBarbero.GaleriaSlot

fun updateGaleriaUrl(slots: List<GaleriaSlot>, index: Int, url: String): List<GaleriaSlot> {
    val mutable = slots.toMutableList()
    mutable[index] = mutable[index].copy(url = url)
    return mutable
}

fun updateGaleriaNombre(slots: List<GaleriaSlot>, index: Int, nombre: String): List<GaleriaSlot> {
    val mutable = slots.toMutableList()
    mutable[index] = mutable[index].copy(nombreEstilo = nombre)
    return mutable
}

fun mapGaleriaToImagenCorte(slots: List<GaleriaSlot>): List<ImagenCorte> =
    slots.filter { it.url.isNotBlank() }
        .mapIndexed { index, slot ->
            ImagenCorte(
                imagenUrl = slot.url,
                nombreEstilo = slot.nombreEstilo.ifBlank { null },
                orden = index
            )
        }
