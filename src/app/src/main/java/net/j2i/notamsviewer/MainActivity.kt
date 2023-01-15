package net.j2i.notamsviewer

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.LocationRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.j2i.notamsviewer.listviewadapters.notamsListViewAdapter
import net.j2i.notamsviewer.models.airportCode
import net.j2i.notamsviewer.models.coordinate
import net.j2i.notamsviewer.models.notam
import net.j2i.notamsviewer.models.notamResponse
import net.j2i.notamsviewer.utility.DistanceCalculator
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL
import java.time.LocalDateTime
import java.util.Arrays.asList
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient as HttpClient

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    val COURSE_LOCATION_REQUEST = 0x8001
    lateinit var fusedLocationProviderClient:FusedLocationProviderClient
    var latitude:Double = 90.0
    var longitude:Double = 0.0
    var RADARBOX_TOKEN = "__"

        lateinit var notamLVA:notamsListViewAdapter
        var airportCodes:List<airportCode> = ArrayList<airportCode>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        loadAirportCodes()
        updateNotams("KATL")
        getLocationPermission()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

    }

    fun getLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toast.makeText(this, "version> = marshmallow", Toast.LENGTH_SHORT).show();
            if (checkSelfPermission( Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "location turned off", Toast.LENGTH_SHORT).show();
                var s = arrayOf<String>(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                this.requestPermissions(s, COURSE_LOCATION_REQUEST);
            } else {
                Toast.makeText(this, "location turned on", Toast.LENGTH_SHORT).show();
                getLocation()
            }
        } else {
            Toast.makeText(this, "scanning", Toast.LENGTH_SHORT).show();
            getLocation();
        }
    }

    fun loadAirportCodes() {
        var airportCodeString = loadRawString(R.raw.airport_codes_json);
        val airportCodeList = Json.decodeFromString<List<airportCode>>( airportCodeString)
        this.airportCodes = airportCodeList
    }

    fun findClosestAirport(latitude:Double, longitude:Double):airportCode? {
        var distance = DistanceCalculator.EarthRadiusInMeters
        var ac:airportCode? = null
        val d = DistanceCalculator()

        airportCodes.forEach {
            var newDistance = d.CalcDistance(
                this.latitude, this.longitude,
                it.coordinates.latitude, it.coordinates.longitude,
                DistanceCalculator.EarthRadiusInMeters
            );
            if(newDistance < distance &&  it.iata_code != null) {
                distance = newDistance
                ac = it
            }
        }

        return ac;
    }


    @SuppressLint("MissingPermission")
    fun getLocation() {
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener {
            latitude = it.latitude
            longitude = it.longitude
            val closestAirport = findClosestAirport(latitude, longitude)
            if(closestAirport != null) {
                updateNotams(closestAirport!!.iata_code!!)
            }
        }
    }


    fun showNotams(notamList:List<notam>) {
        notamLVA = notamsListViewAdapter(this, ArrayList(notamList))
        val notamLV = findViewById<ListView>(R.id.currentwarnings_notamlva)
        notamLV.adapter = notamLVA
    }

    fun updateNotamsFromRadarbox(airportCode:String) {
        val vallUrl = "https://radarbox.com/airspace/${airportCode}/notams"
        val url = URL(vallUrl)
        val httpClient = HttpClient.newBuilder().build();

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