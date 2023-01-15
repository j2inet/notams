package net.j2i.notamsviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.j2i.notamsviewer.listviewadapters.notamsListViewAdapter
import net.j2i.notamsviewer.models.airportCode
import net.j2i.notamsviewer.models.notam
import net.j2i.notamsviewer.models.notamResponse
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.util.Arrays.asList

class MainActivity : AppCompatActivity() {

        lateinit var notamLVA:notamsListViewAdapter
        var airportCodes:List<airportCode> = ArrayList<airportCode>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadAirportCodes()
        updateNotams("KATL")

    }

    fun loadAirportCodes() {
        var airportCodeString = loadRawString(R.raw.airport_codes_json);
        val airportCodeList = Json.decodeFromString<List<airportCode>>( airportCodeString)
        this.airportCodes = airportCodeList
    }


    fun showNotams(notamList:List<notam>) {
        notamLVA = notamsListViewAdapter(this, ArrayList(notamList))
        val notamLV = findViewById<ListView>(R.id.currentwarnings_notamlva)
        notamLV.adapter = notamLVA
    }

    fun updateNotams(airportCode:String) {
        val notamsString = getUpdatedNotams(airportCode);
        var notamsResponse = Json.decodeFromString(notamResponse.serializer(),notamsString)
        var now:LocalDateTime = LocalDateTime.now()
        var filteredNotams = notamsResponse.apiNotams.filter { i -> ((i.effectiveStart==null)||(i.effectiveStart<now))&&((i.effectiveEnd==null)||(i.effectiveEnd>now))  }
        showNotams(filteredNotams)
    }

    fun loadRawString(rawResourceID:Int):String {
        var inputStream = this.resources.openRawResource(rawResourceID)
        var fileContents:ByteArrayOutputStream =  ByteArrayOutputStream()
        var buffer = ByteArray(4096);
        var len:Int = 0;
        len = inputStream.read(buffer)
        try {
            while (len != -1) {
                fileContents.write(buffer, 0, len);
                len = inputStream.read(buffer)
            }
        } catch (exc:IOException) {
        }
        return fileContents.toString()
    }


    fun getUpdatedNotams( airportCode:String) : String {
        return loadRawString(R.raw.notams_sample)
    }
}