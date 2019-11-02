package com.canblack.mallapp.view

import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.canblack.mallapp.R
import com.canblack.mallapp.model.Mall
import com.canblack.mallapp.model.Shop
import com.canblack.mallapp.network.Service
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_mall.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import io.reactivex.schedulers.Schedulers
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.google.gson.GsonBuilder
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView


class MallAct : AppCompatActivity() {
    private var locationManager : LocationManager? = null
    private lateinit var mallAdapter : MallAdapter
    private lateinit var shopAdapter: ShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mall)
        val sliderView = findViewById<SliderView>(R.id.imageSlider)
        mallAdapter = MallAdapter()
        shopAdapter = ShopAdapter()

        sliderView.sliderAdapter = SliderAdapter(this)
        sliderView.scrollTimeInSec = 4
        sliderView.startAutoCycle()
        sliderView.setIndicatorAnimation(IndicatorAnimations.SLIDE)
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)

        re_mall.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true)
        re_mall.adapter = mallAdapter

        re_shop.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        re_shop.adapter = shopAdapter

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("http://cerberusxx.000webhostapp.com/avmmaps/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        val apiMall = retrofit.create(Service::class.java)
        val apiShop = retrofit.create(Service::class.java)

        apiMall.getMallsRx()
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{mallAdapter.setMall(it.bla)}

        apiShop.getShopRx()
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({shopAdapter.setMall(it.shop)})

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
    }

    inner class MallAdapter:
        RecyclerView.Adapter<MallAdapter.ViewHolder>(){
        private val rxmallList : MutableList<Mall> = mutableListOf()

        fun setMall(data: List<Mall>) {
            rxmallList.addAll(data)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(layoutInflater.inflate(R.layout.re_row,parent,false))
        }

        override fun getItemCount(): Int = rxmallList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.MallbindTo(rxmallList[position])
        }
        inner class ViewHolder (itemView:View): RecyclerView.ViewHolder(itemView) {
            private val imgmall by lazy{itemView.findViewById<ImageView> (R.id.img_mall) }
            private val txtmall by lazy{itemView.findViewById<TextView> (R.id.txt_mall) }
            private val txtuzaklik by lazy{itemView.findViewById<TextView> (R.id.txt_uzaklik) }
            private val mall_layout by lazy { itemView.findViewById<LinearLayout> (R.id.mall_layout) }

            fun MallbindTo(mallDto: Mall){
                mall_layout.setOnClickListener {
                    val intent = Intent(itemView.context, AvmAct::class.java)
                    itemView.context.startActivity(intent)
                }
                txtmall.text = mallDto.name
                txtuzaklik.text = mallDto.lang.toString()
                Glide.with(itemView.context).load(mallDto.photo).apply(RequestOptions.circleCropTransform())
                    .into(imgmall)
            }
        }
    }

    inner class ShopAdapter:RecyclerView.Adapter<ShopAdapter.ShopViewHolder>(){

        private val rxshopList : MutableList<Shop> = mutableListOf()

        fun setMall(data: List<Shop>) {
            rxshopList.addAll(data)
            notifyDataSetChanged()
        }
        override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
            holder.shopbindTo(rxshopList[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
            return ShopViewHolder(layoutInflater.inflate(R.layout.re_shop,parent,false))
        }

        override fun getItemCount(): Int = rxshopList.size

        inner class ShopViewHolder (itemView: View):RecyclerView.ViewHolder(
        itemView) {

            private val imgshop by lazy { itemView.findViewById<ImageView>(R.id.img_shop) }
            private val txtshop by lazy { itemView.findViewById<TextView>(R.id.txt_shop) }

            fun shopbindTo(shopDto: Shop) {
                txtshop.text = shopDto.name
                Glide.with(itemView.context).load(shopDto.photo).apply(RequestOptions.circleCropTransform())
                    .into(imgshop)
            }

        }
    }

}













