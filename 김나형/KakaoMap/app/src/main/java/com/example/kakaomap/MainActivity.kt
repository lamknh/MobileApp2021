package com.example.kakaomap

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.security.MessageDigest
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.util.Base64
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import net.daum.mf.map.api.MapPOIItem
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import com.example.kakaomap.DAO.Address
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {
    private var getLongitude : Double = 0.0
    private var getLatitude : Double = 0.0

    //private lateinit var button : Button

    private lateinit var mapView : MapView

    private lateinit var restaurantAdapter: RestaurantAdapter

    private val GEOCODE_URL : String ="http://dapi.kakao.com/v2/local/search/address.json?query="
    private val GEOCODE_USER_INFO : String ="6869c9d359249f596849fc99c5ff98f5"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT > 9) {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        getGeoCode("대구광역시 중구 동성로2가 동성로2길 81")

        mapView = MapView(this)

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
//                        val location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) // 인터넷 기반 위치 찾기
//                        getLongitude = location?.longitude!!
//                        getLatitude = location?.latitude!!
                    getLongitude = 128.610
                    getLatitude = 35.880
                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(getLatitude, getLongitude), true)
                    Toast.makeText(this, "현재 위치를 불러옵니다", Toast.LENGTH_SHORT).show()
                }
                isGPSEnabled -> {
                    val location =
                        lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) // 인터넷 기반 위치 찾기
                    getLongitude = location?.longitude!!
                    getLatitude = location?.latitude!!
                    mapView.setMapCenterPoint(
                        MapPoint.mapPointWithGeoCoord(
                            getLatitude,
                            getLongitude
                        ), true
                    )
                    Toast.makeText(this, "현재 위치를 불러옵니다", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //button = findViewById(R.id.button)

//        button?.setOnClickListener{
//            val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//            val isGPSEnabled: Boolean = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
//            val isNetworkEnabled : Boolean = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//            //권한 확인
//            if(Build.VERSION.SDK_INT >= 23 &&
//                ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
//            } else {
//                when { //provider 제공자 활성화 여부 체크
//                    isNetworkEnabled -> {
////                        val location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) // 인터넷 기반 위치 찾기
////                        getLongitude = location?.longitude!!
////                        getLatitude = location?.latitude!!
//                        getLongitude = 128.6103
//                        getLatitude = 35.8888
//                        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(getLatitude, getLongitude), true)
//                        Toast.makeText(this, "현재 위치를 불러옵니다", Toast.LENGTH_SHORT).show()
//                    }
//                    isGPSEnabled -> {
//                        val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) // 인터넷 기반 위치 찾기
//                        getLongitude = location?.longitude!!
//                        getLatitude = location?.latitude!!
//                        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(getLatitude, getLongitude), true)
//                        Toast.makeText(this, "현재 위치를 불러옵니다", Toast.LENGTH_SHORT).show()
//                    }
//                }
                //주기적 업데이트
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1F, gpsLocationListener)
//            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1F, gpsLocationListener)
//            lm.removeUpdates(gpsLocationListener) //해제부분
//            }

//            val gpsLocationListener = LocationListener { location ->
//                val provider : String = location.provider
//                val longitude : Double = location.longitude
//                val latitude : Double = location.latitude
//                val altitude : Double = location.altitude
//            }
//        }

        mapView.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))  // 커스텀 말풍선 등록

        // 중심점 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(getLatitude, getLongitude), true)
        // 중심점 변경 + 줌 레벨 변경
        //mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(33.41, 126.52), 9, true)
        // 줌 인
        mapView.zoomIn(true);

        // 줌 아웃
        mapView.zoomOut(true);


        //test Class
        val testGIO1 = TestGIO("test1", "description1",35.8888, 128.6103)
        val testGIO2 = TestGIO("test2", "description2", 35.89302375678098, 128.60956210965904)
        val testGIO3 = TestGIO("test3", "description3", 35.89201591394873, 128.6112128398153)

        val testList = ArrayList<TestGIO>()
        testList.add(testGIO1)
        testList.add(testGIO2)
        testList.add(testGIO3)

        for( i in 0 .. testList.size - 1){
            val marker = MapPOIItem()

            marker.apply {
                itemName = testList[i].name   // 마커 이름
                mapPoint = MapPoint.mapPointWithGeoCoord(testList[i].latitude, testList[i].longitude)   // 좌표
                markerType = MapPOIItem.MarkerType.BluePin          // 마커 모양 (커스텀)
                //customImageResourceId = R.drawable.이미지               // 커스텀 마커 이미지
                selectedMarkerType = MapPOIItem.MarkerType.RedPin  // 클릭 시 마커 모양 (커스텀)
                //customSelectedImageResourceId = R.drawable.이미지       // 클릭 시 커스텀 마커 이미지
                isCustomImageAutoscale = false      // 커스텀 마커 이미지 크기 자동 조정
                setCustomImageAnchor(0.5f, 1.0f)    // 마커 이미지 기준점
            }
            mapView.addPOIItem(marker)
        }

        val mapViewContainer = findViewById<View>(R.id.map_view) as ViewGroup
        mapViewContainer.addView(mapView)

        restaurantAdapter = RestaurantAdapter()

        restaurantAdapter.setItemClickListener(object: RestaurantAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                // 클릭 시 이벤트 작성
                Toast.makeText(this@MainActivity, position.toString() + " Clicked", Toast.LENGTH_SHORT).show()
            }
        })

        this.findViewById<RecyclerView>(R.id.in_num_list).apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL,false)
            adapter = restaurantAdapter
        }
    }

    // 커스텀 말풍선 클래스
    class CustomBalloonAdapter(inflater: LayoutInflater): CalloutBalloonAdapter {
        val mCalloutBalloon: View = inflater.inflate(R.layout.balloon_layout, null)
        val name: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_name)
        //val address: TextView = mCalloutBalloon.findViewById(R.id.ball_tv_address)

        override fun getCalloutBalloon(poiItem: MapPOIItem?): View {
            // 마커 클릭 시 나오는 말풍선
            name.text = poiItem?.itemName   // 해당 마커의 정보 이용 가능
            //address.text = "설명"
            return mCalloutBalloon
        }

        override fun getPressedCalloutBalloon(poiItem: MapPOIItem?): View {
            // 말풍선 클릭 시
            return mCalloutBalloon
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

            //System.out.println(data)
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    private fun getAppKeyHash() { // 앱 해시값 얻기
        try {
            val info =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
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
}

class TestGIO (name: String, description : String, latitude: Double, longitude : Double) {
    var name: String = name
    var description: String = description
    var latitude: Double = latitude
    var longitude : Double = longitude
}