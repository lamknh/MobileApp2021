package com.example.what_food

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.what_food.DTO.Address
import com.example.what_food.DTO.RestaurantDTO
import com.example.what_food.DTO.ResultDTO
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*
import kotlin.math.*

class worldcup_fast_result: Activity() {
    private val GEOCODE_URL : String ="http://dapi.kakao.com/v2/local/search/address.json?query="
    private val GEOCODE_USER_INFO : String ="6869c9d359249f596849fc99c5ff98f5"
    private var getLongitude : Double = 0.0
    private var getLatitude : Double = 0.0

    lateinit var x : String
    lateinit var y : String

    lateinit var location : android.location.Address
    var admin : String? = null
    var subadmin : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.worldcup_fast_result)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        var btn_result =  findViewById<ImageButton>(R.id.btn_food_check)
        var btn_restart = findViewById<ImageButton>(R.id.btn_restart)

        var intent = intent
        var resultName = intent.getStringExtra("resultMenuName")
        var FoodImage = intent.getIntExtra("resultMenuImage",0)

        var foddresultname = findViewById<TextView>(R.id.food_result_name)
        var foodresultimage = findViewById<ImageView>(R.id.food_result)
        // 이미지랑 이름 세팅
        foodresultimage.setBackgroundResource(FoodImage!!)
        foddresultname.setText(resultName!!)

        /* GPS 삽입 부분 */
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled: Boolean = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled : Boolean = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        //권한 확인
        if(Build.VERSION.SDK_INT >= 23 &&
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        } else {
            var networkResultChecker = false
            var gpsResultChecker = false
            if(isNetworkEnabled) {
                val location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) // 인터넷 기반 위치 찾기
                getLongitude = 128.610
                getLatitude = 35.880
                if(location != null) {
                    getLongitude = location?.longitude!!
                    getLatitude = location?.latitude!!
                    // mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(getLatitude, getLongitude), true)
                    // Toast.makeText(this, "현재 위치를 불러옵니다", Toast.LENGTH_SHORT).show()
                    // println("네트워크: " + getLatitude.toString() + "|" + getLongitude.toString())
                } else networkResultChecker = false
            }
            if (isGPSEnabled && networkResultChecker) {
                val location =
                    lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) // GPS 기반 위치 찾기
                if(location != null) {
                    getLongitude = location?.longitude!!
                    getLatitude = location?.latitude!!
                    // mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(getLatitude, getLongitude), true )
                    // Toast.makeText(this, "현재 위치를 불러옵니다", Toast.LENGTH_SHORT).show()
                    // println("GPS: " + getLatitude.toString() + "|" + getLongitude.toString())
                } else gpsResultChecker = true
            }
            if(gpsResultChecker) {
                Toast.makeText(this, "현재 위치를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                btn_result.isEnabled = false
                // 버튼 사용 불가능
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
                        if (!(subadmin!!.endsWith("구") || subadmin!!.endsWith("군"))) {
                            subadmin = null
                            Toast.makeText(this, "현재 위치를 불러왔지만, 주소 문제가 발생했습니다.", Toast.LENGTH_SHORT).show()
                            btn_result.isEnabled = false
                            // 버튼 사용 불가능
                        }
                    }
                } else {
                    Toast.makeText(this, "현재 위치를 불러왔지만, 주소 문제가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    btn_result.isEnabled = false
                    // 버튼 사용 불가능
                }
            }
        }
        /* GPS 삽입 부분 종료 */

        //검색결과확인
        btn_result.setOnClickListener {
            Toast.makeText(applicationContext, resultName, Toast.LENGTH_SHORT).show()

            var menu = resultName

            if(!(admin.equals("대구광역시"))){
                var intent = Intent(applicationContext, FailActivity::class.java)
                startActivity(intent)
            } else if(!(subadmin.equals("달서구") || subadmin.equals("중구") || subadmin.equals("북구") || subadmin.equals("서구") ||
                        subadmin.equals("남구") || subadmin.equals("동구") || subadmin.equals("수성구") || subadmin.equals("달성군"))) {
                var intent = Intent(applicationContext, FailActivity::class.java)
                startActivity(intent)
            } else if(menu.isBlank()) {
                Toast.makeText(applicationContext, "마! 밥 안 무글끼가!", Toast.LENGTH_SHORT).show()
            } else {
                // post
                val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()

                var url = "https://www.daegufood.go.kr/kor/api/tasty.html?mode=json&addr=" + subadmin
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("test", "fail")
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        Thread {
                            var str = response.body?.string()
                            str = str!!.replace(", }", "#}\n") // json 포맷으로 맞추기 위함

                            str = str.replace("MNU\":\"\"", "MNU\":\"") // 서구 데이터 중 하나에 대한 에러 제거용

                            var rgx = ",\"SMPL_DESC[^#]*#".toRegex() // json 포맷으로 맞추기 위해 정규식 사용해서 replace
                            str = str.replace(rgx, "")

                            var restaurantDTO = Gson().fromJson(str, RestaurantDTO::class.java)

                            var resultAry = ArrayList<ResultDTO>()
                            for (i: Int in 0..(restaurantDTO.total - 1)) {
                                var searchInfo = menu.toString().toRegex()
                                if (searchInfo.containsMatchIn(restaurantDTO.restaurantData!!.get(i).MNU!!)) {
                                    var temp = ResultDTO()
                                    temp.name = restaurantDTO.restaurantData!!.get(i).BZ_NM!!
                                    getGeoCode(restaurantDTO.restaurantData!!.get(i).GNG_CS!!)
                                    if(x.toDouble() != 0.0 && y.toDouble() != 0.0){ // 없는 주소인 경우 Exception Handling
                                        temp.x = x // longitude, 경도
                                        temp.y = y // latitude, 위도
                                        temp.distance = getDistance(
                                            y.toDouble(),
                                            x.toDouble(),
                                            getLatitude,
                                            getLongitude
                                        ).toString()
                                        resultAry.add(temp)
                                    }
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

        //다시시작
        btn_restart.setOnClickListener {
            var goto_init = Intent(applicationContext, second::class.java)
            startActivity(goto_init)
            finish()
        }
    }

    private fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int { // 거리 계산(단위 : 미터)
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
        val c = 2 * asin(sqrt(a))
        return (6372.8 * 1000 * c).toInt()
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
            if(xy[0].documents.isEmpty()){ // 나형 : 없는 주소인 경우 Exception Handling
                x = "0"
                y = "0"
            } else {
                for(i in 0..xy.size-1){
                    System.out.println("x: ${xy[i].documents[i].address.x}, y: ${xy[i].documents[i].address.y}")
                }
                x = xy[0].documents[0].address.x
                y = xy[0].documents[0].address.y
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }
}