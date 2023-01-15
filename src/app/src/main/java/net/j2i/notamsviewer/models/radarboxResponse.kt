package net.j2i.notamsviewer.models
import kotlinx.serialization.Serializable

@Serializable
data class radarboxResponse (val cost:Int, val success:Boolean) {
}