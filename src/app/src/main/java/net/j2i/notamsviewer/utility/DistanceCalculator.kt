package net.j2i.notamsviewer.utility

import net.j2i.notamsviewer.models.coordinate

class DistanceCalculator {
     companion object {
         public val EarthRadiusInMiles = 3956.0;
         public val EarthRadiusInKilometers = 6367.0;
         public val EarthRadiusInMeters = EarthRadiusInKilometers*1000;
     }

     fun ToRadian(`val`: Double): Double {
         return `val` * (Math.PI / 180)
     }

     fun ToDegree(`val`: Double): Double {
         return `val` * 180 / Math.PI
     }

     fun DiffRadian(val1: Double, val2: Double): Double {
         return ToRadian(val2) - ToRadian(val1)
     }

     fun CalcDistance(p1: coordinate, p2: coordinate): Double {
         return CalcDistance(
             p1.latitude,
             p1.longitude,
             p2.latitude,
             p2.longitude,
             EarthRadiusInKilometers
         )
     }

     fun Bearing(p1: coordinate, p2: coordinate): Double? {
         return Bearing(p1.latitude, p1.longitude, p2.latitude, p2.longitude)
     }

    fun Bearing(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double? {
        run {
            val dLat = lat2 - lat2
            var dLon = lng2 - lng1
            val dPhi: Double = Math.log( Math.tan(lat2 / 2 + Math.PI / 4) / Math.tan(lat1 / 2 + Math.PI / 4) )
            val q: Double =
                if (Math.abs(dLat) > 0) dLat / dPhi else Math.cos(lat1)
            if (Math.abs(dLon) > Math.PI) {
                dLon = if (dLon > 0) -(2 * Math.PI - dLon) else 2 * Math.PI + dLon
            }
            //var d = Math.Sqrt(dLat * dLat + q * q * dLon * dLon) * R;
            return ToDegree(Math.atan2(dLon, dPhi))
        }
    }

    fun CalcDistance(
         lat1: Double,
         lng1: Double,
         lat2: Double,
         lng2: Double,
         radius: Double
     ): Double {
         return radius * 2 * Math.asin(
             Math.min(
                 1.0, Math.sqrt(
                     Math.pow(
                         Math.sin(
                             DiffRadian(lat1, lat2) / 2.0
                         ), 2.0
                     )
                             + Math.cos(ToRadian(lat1)) * Math.cos(ToRadian(lat2)) * Math.pow(
                         Math.sin(
                             DiffRadian(lng1, lng2) / 2.0
                         ), 2.0
                     )
                 )
             )
         )
     }
 }