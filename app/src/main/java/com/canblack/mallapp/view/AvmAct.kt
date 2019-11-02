package com.canblack.mallapp.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.canblack.mallapp.R
import com.canblack.mallapp.model.Shop
import com.canblack.mallapp.network.Service
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_avm.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class AvmAct : AppCompatActivity() {
    private lateinit var shopmallAdapter : ShopMallAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avm)
        shopmallAdapter = ShopMallAdapter()

        ree_avm_shop.layoutManager = LinearLayoutManager(this)
        ree_avm_shop.adapter = shopmallAdapter

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("http://cerberusxx.000webhostapp.com/avmmaps/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        val apiShopMall = retrofit.create(Service::class.java)

        apiShopMall.gettShopRx()
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({shopmallAdapter.setMall(it.shop)},
                {
                    Log.v("Durum",it.message)
                })

        http_layout.setOnClickListener {
            val id = 2
            val intent = Intent(this, Web::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }

        kampanya_layout.setOnClickListener {
            val id = 3
            val intent = Intent(this, Web::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }

        konum_layout.setOnClickListener {

        }
    }

    inner class ShopMallAdapter:
        RecyclerView.Adapter<ShopMallAdapter.ShopMallVH>(){

        private val rxshopmallList : MutableList<Shop> = mutableListOf()

        fun setMall(data: List<Shop>) {
            rxshopmallList.addAll(data)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopMallVH {
            return ShopMallVH(layoutInflater
                .inflate(R.layout.re_avm_shop,parent,false))
        }

        override fun getItemCount(): Int = rxshopmallList.size

        override fun onBindViewHolder(holder: ShopMallVH, position: Int) {
            holder.ShopMallbindTo(rxshopmallList[position])
        }

        inner class ShopMallVH (itemView: View): RecyclerView.ViewHolder(
            itemView){

            private val imgshopmall by lazy { itemView.findViewById<ImageView>(R.id.img_avm_mall)}
            private val txtshopmall by lazy { itemView.findViewById<TextView>(R.id.txt_avm_mall)}
            private val txtshoptel by lazy { itemView.findViewById<TextView>(R.id.txt_avm_tel)}
            private val txtshopkat by lazy { itemView.findViewById<TextView>(R.id.txt_avm_map)}

            fun ShopMallbindTo(shopMall : Shop){
                txtshopmall.text = shopMall.name
                txtshoptel.text = shopMall.telefon

                txtshoptel.setOnClickListener {
                    val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:"+shopMall.telefon))

                    if (ActivityCompat.checkSelfPermission(itemView.context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(itemView.context, "permission not granted", Toast.LENGTH_SHORT).show()
                    }else{
                        itemView.context.startActivity(callIntent)
                    }
                }

                txtshopkat.setOnClickListener {
                    val id = 2
                    val intent = Intent(itemView.context, Web::class.java)
                    intent.putExtra("id",id)
                    intent.putExtra("web",shopMall.kat)
                    itemView.context.startActivity(intent)
                }

                Glide.with(itemView.context).load(shopMall.photo)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgshopmall)
            }
        }
    }

}
