package com.example.final_1

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.first_page_layout.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class First_Page_Fragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.first_page_layout,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        sendRequest()
    }

    fun setNews(NewsList: List<News>){
        val adapter = NewsAdapter(NewsList)
        recyclerView.adapter = adapter
    }

    val updateText = 1

    val handler = object : Handler(){
        override fun handleMessage(msg: Message) {
            when(msg.what){
                updateText -> setNews(msg.obj as List<News>)
            }
        }
    }

    fun sendRequest(){
        thread {
            var connection: HttpURLConnection?= null
            val responseStr = StringBuilder()
            val url = URL("http://v.juhe.cn/toutiao/index?type=&key=d1155312fafb22f7f6b13299f70f2f83")
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

            val response = Gson().fromJson(responseStr.toString(),NewsBean::class.java)
            val result =response.result
            val data = result.data

            val newsList = ArrayList<News>()
            for(i in data){
                val image1=getHttpBitmap(i.thumbnail_pic_s)
                val image2=getHttpBitmap(i.thumbnail_pic_s02)
                val image3=getHttpBitmap(i.thumbnail_pic_s03)
                newsList.add(News(i.title,i.url, image1!!,image2,image3))
            }

            val msg = Message()
            msg.what = updateText
            msg.obj=newsList
            handler.sendMessage(msg)
        }
    }


    fun getHttpBitmap(url: String?): Bitmap? {
        if (url!=null){
            val myurl = URL(url)
            val conn = myurl.openConnection()
            conn.connectTimeout = 6000
            conn.doInput = true
            conn.useCaches = false
            conn.connect()
            val imgbit = conn.getInputStream()
            val bmp = BitmapFactory.decodeStream(imgbit)
            imgbit.close()
            return bmp
        }else{
            return null
        }
    }

    inner class NewsAdapter(val NewsList: List<News>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
            val newTitle : TextView = view.findViewById(R.id.newsTitle)
            val newImage1 : ImageView = view.findViewById(R.id.newsImage1)
            val newImage2 : ImageView = view.findViewById(R.id.newsImage2)
            val newImage3 : ImageView = view.findViewById(R.id.newsImage3)
            val url : TextView = view.findViewById(R.id.url)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item,parent,false)
            val viewHolder = ViewHolder(view)
            viewHolder.itemView.setOnClickListener {
                val intent = Intent(context,web::class.java)
                val url = viewHolder.url.text
                intent.putExtra("extra_data",url.toString())
                startActivity(intent)
            }
            return viewHolder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val new = NewsList[position]
            holder.newTitle.text = new.title
            holder.newImage1.setImageBitmap(new.imageSrc1)
            holder.newImage2.setImageBitmap(new.imageSrc2)
            holder.newImage3.setImageBitmap(new.imageSrc3)
            holder.url.text = new.url
        }

        override fun getItemCount() = NewsList.size


    }
}