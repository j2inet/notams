package net.j2i.notamsviewer.models
import kotlinx.serialization.Serializable


@Serializable
data class notamResponse(val cost:Int, val success:Boolean, val apiNotams: List<notam> ){
}