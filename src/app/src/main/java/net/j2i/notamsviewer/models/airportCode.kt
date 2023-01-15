package net.j2i.notamsviewer.models

import kotlinx.serialization.Serializable
import net.j2i.notamsviewer.utility.CoordinateSerializer
import net.j2i.notamsviewer.utility.DateSerializer

@Serializable
data class airportCode (val continent:String,
                        @Serializable(with = CoordinateSerializer::class) val coordinates:coordinate,
                        val elevation_ft:String? = null,
                        val gps_code:String? = null,
                        val iata_code:String? = null,
                        val ident:String,
                        val iso_country:String,
                        val iso_region:String,
                        val local_code:String? = null,
                        val municipality:String? = null,
                        val name:String,
                        val type:String
)                           {
}

