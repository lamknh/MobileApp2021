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
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.what_food.Chinese.worldcup_ch1
import com.example.what_food.DTO.Address
import com.example.what_food.DTO.RestaurantDTO
import com.example.what_food.DTO.ResultDTO
import com.example.what_food.Dessert.worldcup_des1
import com.example.what_food.FastFood.worldcup_fast1
import com.example.what_food.Japanese.worldcup_jp1
import com.example.what_food.Korean.worldcup_ko1
import com.example.what_food.Snack.worldcup_snack1
import com.example.what_food.Western.worldcup_west1
import com.example.what_food.World.worldcup_world1
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*

class CategoryActivity : Activity() {
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
        setContentView(R.layout.select_food)

        var ko_food : ImageButton = findViewById<ImageButton>(R.id.ko_food)
        var jp_food : ImageButton = findViewById<ImageButton>(R.id.jp_food)
        var ch_food : ImageButton = findViewById<ImageButton>(R.id.ch_food)
        var fast_food : ImageButton = findViewById<ImageButton>(R.id.fast_food)
        var west_food : ImageButton = findViewById<ImageButton>(R.id.west_food)
        var world_food : ImageButton = findViewById<ImageButton>(R.id.world_food)
        var dessert : ImageButton = findViewById<ImageButton>(R.id.dessert)
        var snack : ImageButton = findViewById<ImageButton>(R.id.snack)
        var recommend : ImageButton = findViewById<ImageButton>(R.id.recommend_btn)
        var btnReturn = findViewById<ImageButton>(R.id.back)

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
                if(location != null) {
//                    getLongitude = location?.longitude!!
//                    getLatitude = location?.latitude!!
                    getLongitude = 128.6103
                    getLatitude = 35.8888
                    // mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(getLatitude, getLongitude), true)
                    // Toast.makeText(this, "현재 위치를 불러옵니다", Toast.LENGTH_SHORT).show()
                    // println("네트워크: " + getLatitude.toString() + "|" + getLongitude.toString())
                } else networkResultChecker = true
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
                recommend.isEnabled = false
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
                            recommend.isEnabled = false
                            // 버튼 사용 불가능
                        }
                    }
                } else {
                    Toast.makeText(this, "현재 위치를 불러왔지만, 주소 문제가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    recommend.isEnabled = false
                    // 버튼 사용 불가능
                }
            }
        }
        /* GPS 삽입 부분 종료 */

        //뒤로가기 버튼
        btnReturn.setOnClickListener{
            var goto_init = Intent(applicationContext, MainActivity::class.java)
            startActivity(goto_init)
            finish()
        }

        recommend.setOnClickListener{
            Toast.makeText(this, "메뉴추천", Toast.LENGTH_SHORT).show()
//            var goto_init = Intent(applicationContext, ResultActivity::class.java)
//            startActivity(goto_init)
//            finish()
        }

        ko_food.setOnClickListener{
            var goto_kr = Intent(applicationContext, worldcup_ko1::class.java)
            startActivity(goto_kr)
            finish()
        }

        jp_food.setOnClickListener{
            var goto_jp = Intent(applicationContext, worldcup_jp1::class.java)
            startActivity(goto_jp)
            finish()
        }

        ch_food.setOnClickListener{
            var goto_ch = Intent(applicationContext, worldcup_ch1::class.java)
            startActivity(goto_ch)
            finish()
        }

        fast_food.setOnClickListener{
            var goto_fast = Intent(applicationContext, worldcup_fast1::class.java)
            startActivity(goto_fast)
            finish()
        }

        west_food.setOnClickListener{
            var goto_west = Intent(applicationContext, worldcup_west1::class.java)
            startActivity(goto_west)
            finish()
        }

        world_food.setOnClickListener{
            var goto_world = Intent(applicationContext, worldcup_world1::class.java)
            startActivity(goto_world)
            finish()
        }

        snack.setOnClickListener{
            var goto_snack = Intent(applicationContext, worldcup_snack1::class.java)
            startActivity(goto_snack)
            finish()
        }

        dessert.setOnClickListener{
            var goto_dessert = Intent(applicationContext, worldcup_des1::class.java)
            startActivity(goto_dessert)
            finish()
        }

        recommend.setOnClickListener {
            Toast.makeText(applicationContext, "지도 로딩중...", Toast.LENGTH_SHORT).show()

            //subadmin = "북구"
            if(!(admin.equals("대구광역시"))){
                var intent = Intent(applicationContext, FailActivity::class.java)
                startActivity(intent)
            } else if(!(subadmin.equals("달서구") || subadmin.equals("중구") || subadmin.equals("북구") || subadmin.equals("서구") ||
                        subadmin.equals("남구") || subadmin.equals("동구") || subadmin.equals("수성구") || subadmin.equals("달성군"))) {
                var intent = Intent(applicationContext, FailActivity::class.java)
                startActivity(intent)
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
                        Log.d("test", "fail")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        Thread {
                            var str = response.body?.string()
                            str = str!!.replace(", }", "#}\n") // json 포맷으로 맞추기 위함

                            str = str.replace("MNU\":\"\"", "MNU\":\"") // 서구 데이터 중 하나에 대한 에러 제거용

                            var rgx = ",\"SMPL_DESC[^#]*#".toRegex() // json 포맷으로 맞추기 위해 정규식 사용해서 replace
                            str = str.replace(rgx, "")

                            var restaurantDTO = Gson().fromJson(str, RestaurantDTO::class.java)

                            var tempAry = ArrayList<ResultDTO>()
                            for (i: Int in 0..(restaurantDTO.total - 1)) {
                                var temp = ResultDTO()
                                temp.name = restaurantDTO.restaurantData!!.get(i).BZ_NM!!
                                if(!(restaurantDTO.restaurantData!!.get(i).FD_CS!!.isEmpty())) temp.name += ("/" + restaurantDTO.restaurantData!!.get(i).FD_CS!!)
                                println(temp.name)
                                var location = restaurantDTO.restaurantData!!.get(i).GNG_CS!!
                                getGeoCode(locationHandler((location)))
                                temp.x = x // longitude, 경도
                                temp.y = y // latitude, 위도
                                temp.distance = getDistance(y.toDouble(), x.toDouble(), getLatitude, getLongitude).toString()
                                tempAry.add(temp)
                            }
                            tempAry.sortBy{ it.distance }

                            var resultAry = ArrayList<ResultDTO>()
                            for(i in 0 .. 19 step 1) { // 20개만
                                resultAry.add(tempAry.get(i))
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

    private fun locationHandler(location: String): String {
        var temp = location;

        when(temp) {
            "대구광역시 북구 침산동 105-2" -> temp = "대구 북구 침산동 1757"
        }

        return temp
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