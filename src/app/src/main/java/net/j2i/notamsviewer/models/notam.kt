package net.j2i.notamsviewer.models

import android.os.Parcel
import android.os.Parcelable
import java.util.*
import kotlinx.serialization.Serializable
import net.j2i.notamsviewer.utility.DateAsLongSerializer
import net.j2i.notamsviewer.utility.DateSerializer
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class notam(
    val id:String? = null,
    val number:Int,
    val notamClass:String? = null,
    val affectedFir:String? = null,
    val year:String,
    val type:String? = null,
    @Serializable(with = DateSerializer::class) val effectiveStart: LocalDateTime? = null,
    //val effectiveStart:String,
    @Serializable(with = DateSerializer::class) val effectiveEnd:LocalDateTime? = null,
    val icaoLocation:String,
    @Serializable(with = DateSerializer::class) val issued:LocalDateTime,
    //val issued:String,
    val location:String,
    val text:String,
    val minimumFlightLevel:String? = null,
    val maximumFlightLevel:String? = null,
    val radius:String? = null,
    var translations:List<translation>
    )  {

}