package com.example.final_1

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.first_page_layout.*
import kotlinx.android.synthetic.main.second_page_layout.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class Second_Page_Fragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.second_page_layout,container,false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sendRequest()
    }

    fun sendRequest(){
        thread {
            var connection: HttpURLConnection?= null
            val responseStr = StringBuilder()
            val url = URL("http://apis.juhe.cn/simpleWeather/query?city=%E5%8C%97%E4%BA%AC&key=d640a8ff58a2c6798bdf615cb10a6cf4")
            connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 8000
            connection.readTimeout = 8000
            val input =  connection.inputStream
            val reader = BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine {
                    responseStr.append(it)
                }
            }

            val msg = Message()
            msg.what = updateText
            msg.obj=responseStr
            handler.sendMessage(msg)

        }
    }

    val updateText = 1

    val handler = object : Handler(){
        override fun handleMessage(msg: Message) {
            when(msg.what){
                updateText -> showResponse(msg.obj.toString())
            }
        }
    }

    private fun showResponse(responseStr: String){
            var response = Gson().fromJson(responseStr,weatherBean::class.java)
            var result =response.result
            var realtime = result.realtime
            var future = result.future

            city.text = result.city
            temperature.text = "温度:"+realtime.temperature+"℃"
            humidity.text = "湿度:"+realtime.humidity
            info.text = realtime.info
            direct.text = realtime.direct
            power.text = realtime.power
            aqi.text = "空气质量指数:"+realtime.aqi

            date1.text = future[0].date
            date2.text = future[1].date
            date3.text = future[2].date
            date4.text = future[3].date
            date5.text = future[4].date

            temperature1.text = future[0].temperature
            temperature2.text = future[1].temperature
            temperature3.text = future[2].temperature
            temperature4.text = future[3].temperature
            temperature5.text = future[4].temperature

            weather1.text = future[0].weather
            weather2.text = future[1].weather
            weather3.text = future[2].weather
            weather4.text = future[3].weather
            weather5.text = future[4].weather

            direct1.text = future[0].direct
            direct2.text = future[1].direct
            direct3.text = future[2].direct
            direct4.text = future[3].direct
            direct5.text = future[4].direct
    }

}