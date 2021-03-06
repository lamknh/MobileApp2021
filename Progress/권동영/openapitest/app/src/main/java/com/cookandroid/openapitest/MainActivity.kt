package com.cookandroid.openapitest

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.security.MessageDigest
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*

class MainActivity : AppCompatActivity() {
    lateinit var txtLoc : TextView
    lateinit var txtMenu : EditText
    lateinit var btnStart : Button

    private val GEOCODE_URL : String ="http://dapi.kakao.com/v2/local/search/address.json?query="
    private val GEOCODE_USER_INFO : String ="6869c9d359249f596849fc99c5ff98f5"
    private var getLongitude : Double = 0.0
    private var getLatitude : Double = 0.0

    lateinit var x : String
    lateinit var y : String

    lateinit var location : android.location.Address
    var admin : String? = null
    var subadmin : String? = null

    val j_food = intArrayOf(R.drawable.j_ramen, R.drawable.j_sasimi, R.drawable.j_susi, R.drawable.j_udon)
    val k_food = intArrayOf(R.drawable.k_bossam, R.drawable.k_samgyetang, R.drawable.k_tang, R.drawable.k_tteokbokki)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        getAppKeyHash()
        //getGeoCode("??????????????? ?????? ?????????2??? ?????????2??? 81")

        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled: Boolean = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled : Boolean = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        //?????? ??????
        if(Build.VERSION.SDK_INT >= 23 &&
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        } else {
            var networkResultChecker = false
            var gpsResultChecker = false
            if(isNetworkEnabled) {
                val location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) // ????????? ?????? ?????? ??????
                if(location != null) {
                    getLongitude = location?.longitude!!
                    getLatitude = location?.latitude!!
                    // mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(getLatitude, getLongitude), true)
                    Toast.makeText(this, "?????? ????????? ???????????????", Toast.LENGTH_SHORT).show()
                    println("????????????: " + getLatitude.toString() + "|" + getLongitude.toString())
                } else networkResultChecker = true
            }
            if (isGPSEnabled && networkResultChecker) {
                val location =
                    lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) // GPS ?????? ?????? ??????
                if(location != null) {
                    getLongitude = location?.longitude!!
                    getLatitude = location?.latitude!!
                    //mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(getLatitude, getLongitude), true )
                    Toast.makeText(this, "?????? ????????? ???????????????", Toast.LENGTH_SHORT).show()
                    println("GPS: " + getLatitude.toString() + "|" + getLongitude.toString())
                } else gpsResultChecker = true
            }
            if(gpsResultChecker) {
                Toast.makeText(this, "?????? ????????? ????????? ??? ????????????.", Toast.LENGTH_SHORT).show()
                // ?????? ?????? ?????????
            } else {
                var mGeoCoder = Geocoder(applicationContext, Locale.KOREAN)
                var mResultList: List<android.location.Address>? = null
                try {
                    mResultList = mGeoCoder.getFromLocation(getLatitude, getLongitude, 1)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                if(mResultList != null) {
                    location = mResultList[0]

                    var loca_arr = location.getAddressLine(0).split(" ")

                    admin = location.adminArea
                    if(admin == null) {
                        admin = loca_arr.get(1)
                    }

                    subadmin = location.subAdminArea
                    if(subadmin == null) {
                        subadmin = loca_arr.get(2)
                        if (!(subadmin!!.endsWith("???") || subadmin!!.endsWith("???"))) {
                            subadmin = null
                            Toast.makeText(this, "?????? ????????? ???????????????, ?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                            // ?????? ?????? ?????????
                        }
                    }
                } else {
                    Toast.makeText(this, "?????? ????????? ???????????????, ?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                    // ?????? ?????? ?????????
                }
            }
        }

        txtLoc = findViewById(R.id.txtLoc)
        txtLoc.setText("$admin $subadmin")
        txtMenu = findViewById(R.id.txtMenu)
        btnStart = findViewById(R.id.btnStart)

        btnStart.setOnClickListener {
            //var location = txtLoc.text.toString()
            var menu = txtMenu.text.toString()

            // getGeoCode()


            if(!(admin.equals("???????????????"))){
                var intent = Intent(applicationContext, FailActivity::class.java)
                startActivity(intent)
            } else if(!(subadmin.equals("?????????") || subadmin.equals("??????") || subadmin.equals("??????") || subadmin.equals("??????") ||
                        subadmin.equals("??????") || subadmin.equals("??????") || subadmin.equals("?????????") || subadmin.equals("?????????"))) {
                var intent = Intent(applicationContext, FailActivity::class.java)
                startActivity(intent)
            } else if(menu.isBlank()) {
                Toast.makeText(applicationContext, "???! ??? ??? ????????????!", Toast.LENGTH_SHORT).show()
            } else {
                // post
                val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()

                var url = "https://www.daegufood.go.kr/kor/api/tasty.html?mode=json&addr=" + subadmin
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(url)
                    .build()

                val response = client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        TODO("Not yet implemented")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        Thread {
                            var str = response.body?.string()
                            str = str!!.replace(", }", "#}\n") // json ???????????? ????????? ??????

                            str = str.replace("MNU\":\"\"", "MNU\":\"") // ?????? ????????? ??? ????????? ?????? ?????? ?????????

                            var rgx = ",\"SMPL_DESC[^#]*#".toRegex() // json ???????????? ????????? ?????? ????????? ???????????? replace
                            str = str.replace(rgx, "")

                            var restaurantDTO = Gson().fromJson(str, RestaurantDTO::class.java)

                            var resultAry = ArrayList<ResultDTO>()
                            for (i: Int in 0..(restaurantDTO.total - 1)) {
                                var searchInfo = txtMenu.text.toString().toRegex()
                                if (searchInfo.containsMatchIn(restaurantDTO.restaurantData!!.get(i).MNU!!)) {
                                    var temp = ResultDTO()
                                    temp.name = restaurantDTO.restaurantData!!.get(i).BZ_NM!!
                                    getGeoCode(restaurantDTO.restaurantData!!.get(i).GNG_CS!!)
                                    temp.x = x // longitude, ??????
                                    temp.y = y // latitude, ??????
                                    temp.distance = getDistance(y.toDouble(), x.toDouble(), getLatitude, getLongitude).toString()
                                    resultAry.add(temp)
                                }
                            }

                            if(resultAry.isEmpty()) {
                                var intent = Intent(applicationContext, FailActivity::class.java)
                                startActivity(intent)
                            } else {
                                var intent = Intent(applicationContext, ResultActivity::class.java)
                                intent.putExtra("result", resultAry)
                                intent.putExtra("getLongitude", getLongitude)
                                intent.putExtra("getLatitude", getLatitude)

                                startActivity(intent)
                            }
                        }.start()
                    }
                })
            }
        }
    }

    fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int { // ?????? ??????(?????? : ??????)
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
        val c = 2 * asin(sqrt(a))
        return (6372.8 * 1000 * c).toInt()
    }

    private fun getAppKeyHash() { // ??? ????????? ??????
        try {
            val info =
                packageManager.getPackageInfo("com.cookandroid.openapitest", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                var md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = String(Base64.encode(md.digest(), 0))
                Log.e("Hash key", something)
            }
        } catch (e: Exception) {
            Log.e("name not found", e.toString())
        }
    }

    private fun getGeoCode(address : String) {
        System.out.println("getting GeoCode")
        val obj : URL
        try{
            val address : String = URLEncoder.encode(address, "UTF-8")

            obj = URL(GEOCODE_URL+address)

            val con : HttpURLConnection = obj.openConnection() as HttpURLConnection

            con.setRequestMethod("GET")
            con.setRequestProperty("Authorization", "KakaoAK " + GEOCODE_USER_INFO)
            con.setRequestProperty("content-type", "application/json")
            con.setDoOutput(true)
            con.setUseCaches(false)
            con.setDefaultUseCaches(false)

            val data = con.inputStream.bufferedReader().readText()
            val dataList = "[$data]"
            val xy = Gson().fromJson(dataList, Array<Address>::class.java).toList()
            for(i in 0..xy.size-1){
                System.out.println("x: ${xy[i].documents[i].address.x}, y: ${xy[i].documents[i].address.y}")
            }

            x = xy[0].documents[0].address.x
            y = xy[0].documents[0].address.y

            //System.out.println(data)
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }
}