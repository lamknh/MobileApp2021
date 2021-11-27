package com.cookandroid.openapitest

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
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

class MainActivity : AppCompatActivity() {
    lateinit var txtLoc : EditText
    lateinit var txtMenu : EditText
    lateinit var btnStart : Button

    private val GEOCODE_URL : String ="http://dapi.kakao.com/v2/local/search/address.json?query="
    private val GEOCODE_USER_INFO : String ="6869c9d359249f596849fc99c5ff98f5"
    private var getLongitude : Double = 0.0
    private var getLatitude : Double = 0.0

    lateinit var x : String
    lateinit var y : String

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
        getGeoCode("대구광역시 중구 동성로2가 동성로2길 81")

        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled: Boolean = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled : Boolean = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        //권한 확인
        if(Build.VERSION.SDK_INT >= 23 &&
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        } else {
            when { //provider 제공자 활성화 여부 체크
                isNetworkEnabled -> {
                    val location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) // 인터넷 기반 위치 찾기
                    getLongitude = location?.longitude!!
                    getLatitude = location?.latitude!!
                    // mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(getLatitude, getLongitude), true)
                    Toast.makeText(this, "현재 위치를 불러옵니다", Toast.LENGTH_SHORT).show()
                    println("네트워크: " + getLatitude.toString() + "|" + getLongitude.toString())
                }
                isGPSEnabled -> {
                    val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) // 인터넷 기반 위치 찾기
                    getLongitude = location?.longitude!!
                    getLatitude = location?.latitude!!
                    //mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(getLatitude, getLongitude), true )
                    Toast.makeText(this, "현재 위치를 불러옵니다", Toast.LENGTH_SHORT).show()
                    println("GPS: " + getLatitude.toString() + "|" + getLongitude.toString())
                }
            }
        }

        txtLoc = findViewById(R.id.txtLoc)
        txtMenu = findViewById(R.id.txtMenu)
        btnStart = findViewById(R.id.btnStart)

        btnStart.setOnClickListener {
            var location = txtLoc.text.toString()
            var menu = txtMenu.text.toString()

            // getGeoCode()


            if(!(location.equals("달서구") || location.equals("중구") || location.equals("북구") || location.equals("서구") ||
                        location.equals("남구") || location.equals("동구") || location.equals("수성구") || location.equals("달성군"))) {
                var intent = Intent(applicationContext, FailActivity::class.java)
                startActivity(intent)
            } else if(menu.isBlank()) {
                Toast.makeText(applicationContext, "마! 밥 안 무글끼가!", Toast.LENGTH_SHORT).show()
            } else {
                // post
                val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()

                var url = "https://www.daegufood.go.kr/kor/api/tasty.html?mode=json&addr=" + location
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
                            str = str!!.replace(", }", "#}\n") // json 포맷으로 맞추기 위함

                            str = str.replace("MNU\":\"\"", "MNU\":\"") // 서구 데이터 중 하나에 대한 에러 제거용

                            var rgx = ",\"SMPL_DESC[^#]*#".toRegex() // json 포맷으로 맞추기 위해 정규식 사용해서 replace
                            str = str.replace(rgx, "")

                            var restaurantDTO = Gson().fromJson(str, RestaurantDTO::class.java)

                            var resultAry = ArrayList<ResultDTO>()
                            for (i: Int in 0..(restaurantDTO.total - 1)) {
                                var searchInfo = txtMenu.text.toString().toRegex()
                                if (searchInfo.containsMatchIn(restaurantDTO.restaurantData!!.get(i).MNU!!)) {
                                    var temp = ResultDTO()
                                    temp.name = restaurantDTO.restaurantData!!.get(i).BZ_NM!!
                                    getGeoCode(restaurantDTO.restaurantData!!.get(i).GNG_CS!!)
                                    temp.x = x
                                    temp.y = y
                                    temp.distance = (i + 1).toString()
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
                                intent.putExtra("getLatitude", getLongitude)

                                startActivity(intent)
                            }
                        }.start()
                    }
                })
            }
        }
    }

    private fun getAppKeyHash() { // 앱 해시값 얻기
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