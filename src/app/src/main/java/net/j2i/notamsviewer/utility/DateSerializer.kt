package net.j2i.notamsviewer.utility

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.internal.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Serializer(forClass = DateSerializer::class)
object DateSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("DateSerializer", PrimitiveKind.STRING)

    var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd'T'HH:mm:ss[.[SSSSSSSSS][SSSSSSSS][SSSSSSS][SSSSSS][SSSSS][SSSS][SSS][SS][S]]",
        Locale.ENGLISH
    )
    override fun serialize(output: Encoder, obj: LocalDateTime) {
        output.encodeString(obj.toString())
    }

    override fun deserialize(input: Decoder): LocalDateTime {
        val s = input.decodeString()
        var sparts = s.split('.')
        val dt = LocalDateTime.parse(sparts[0], formatter)
        return dt
    }
}