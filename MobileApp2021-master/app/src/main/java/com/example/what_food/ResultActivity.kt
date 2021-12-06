package com.example.what_food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.what_food.DTO.ResultDTO
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class ResultActivity : AppCompatActivity() {
    private lateinit var mapView : MapView
    private lateinit var restaurantAdapter: RestaurantAdapter
    //private lateinit var markerArray : ArrayList<MapPOIItem>
    private lateinit var restTitle : TextView
    private lateinit var homeButton : ImageButton

    var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kakaomap)

        restTitle = findViewById(R.id.restTitle)
        homeButton = findViewById(R.id.homeButton)

        var intent = intent

        homeButton.setOnClickListener {
            finish()
        }

        val list: ArrayList<ResultDTO>? =
            getIntent().getSerializableExtra("result") as ArrayList<ResultDTO>?
        val getLongitude = intent.getDoubleExtra("getLongitude", 0.0)
        val getLatitude = getIntent().getDoubleExtra("getLatitude", 0.0)

        mapView = MapView(this)
        mapView.setCalloutBalloonAdapter(CustomBalloonAdapter(layoutInflater))  // 커스텀 말풍선 등록
        mapView.setMapCenterPoint(
            MapPoint.mapPointWithGeoCoord(getLatitude, getLongitude),
            true
        ) // 중심점 변경
        mapView.zoomIn(true) // 줌 인
        mapView.zoomOut(true) // 줌 아웃

        if (list != null) {
            for (i in 0..list.size - 1) {
                val marker = MapPOIItem()
                System.out.println(list[i].name)
                System.out.println(list[i].x)
                System.out.println(list[i].y)

                marker.apply {
                    itemName = list[i].name   // 마커 이름
                    mapPoint = MapPoint.mapPointWithGeoCoord(
                        list[i].y.toDouble(),
                        list[i].x.toDouble()
                    ) // 좌표
                    markerType = MapPOIItem.MarkerType.BluePin          // 마커 모양 (커스텀)
                    selectedMarkerType = MapPOIItem.MarkerType.RedPin  // 클릭 시 마커 모양 (커스텀)
                    isCustomImageAutoscale = false      // 커스텀 마커 이미지 크기 자동 조정
                    setCustomImageAnchor(0.5f, 1.0f)    // 마커 이미지 기준점
                }
                System.out.println(marker.toString())
                mapView.addPOIItem(marker)
            }
        }

        val mapViewContainer = findViewById<View>(R.id.map_view) as ViewGroup
        mapViewContainer.addView(mapView)

        restaurantAdapter = list?.let { RestaurantAdapter(it) }!!

        restaurantAdapter.setItemClickListener(object : RestaurantAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                restTitle.setText(list[position].name)
                mapView.setMapCenterPoint(
                    MapPoint.mapPointWithGeoCoord(
                        list[position].y.toDouble(),
                        list[position].x.toDouble()
                    ), true
                )
//                Toast.makeText(
//                    this@ResultActivity,
//                    position.toString() + " Clicked",
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        })

        this.findViewById<RecyclerView>(R.id.in_num_list).apply {
            layoutManager =
                LinearLayoutManager(this@ResultActivity, LinearLayoutManager.VERTICAL, false)
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
}