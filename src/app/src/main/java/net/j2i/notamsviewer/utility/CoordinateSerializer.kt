package net.j2i.notamsviewer.utility

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.j2i.notamsviewer.models.coordinate
import java.lang.Double.parseDouble
import java.util.*

@Serializer(forClass = DateAsLongSerializer::class)
object CoordinateSerializer: KSerializer<coordinate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("coordinate", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: coordinate) = encoder.encodeString("${value.latitude},${value.longitude}")
    override fun deserialize(input: Decoder): coordinate {
        var coordinateString = input.decodeString()
        var parts = coordinateString.split(',')
        val d1 = parseDouble(parts[0])
        val d2 = parseDouble(parts[1])
        val c = coordinate(d1,d2)
        return c;

    }

}