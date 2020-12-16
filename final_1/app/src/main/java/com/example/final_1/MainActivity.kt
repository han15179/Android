package com.example.final_1

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread
import com.google.gson.Gson
import kotlinx.android.synthetic.main.first_page_layout.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //监听底部导航栏
        but1.setOnClickListener{
            replaceFragment(First_Page_Fragment())
        }
        but2.setOnClickListener{
            replaceFragment(Second_Page_Fragment())
        }
        but3.setOnClickListener{
            replaceFragment(Third_Page_Fragment())
        }

        //进入时默认选择第一页
        replaceFragment(First_Page_Fragment())
    }

    //替换页面的函数
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager=supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.ly_content,fragment)
        transaction.commit()
    }
}