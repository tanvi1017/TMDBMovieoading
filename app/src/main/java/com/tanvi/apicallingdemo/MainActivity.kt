package com.tanvi.apicallingdemo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tanvi.apicallingdemo.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var recyclerView: RecyclerView
    lateinit var progress_bar: ProgressBar
    var rvAdapter = MoviesAdapter(mutableListOf())

    var isScrolling: Boolean = false
    var visibleItemCount:Int  =0
    var totalItemCount:Int = 0
    var pastVisibleItemCount:Int = 0

    // is variable m list store karenge
    // for eg agar first api call m we are getting 10 items, we will add in this
    // in scond api call we will get 10 more , total 200, we will again add in this. isse hamare recyclerview ko load karenge
     var resultList = mutableListOf<Result>()
    var page = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progress_bar = findViewById(R.id.progress_bar)
        recyclerView = findViewById(R.id.recyclerView)
        //dekho we wil simplyfy this make making a fucntion
        recyclerView.adapter = rvAdapter
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        callApi(page)

        setRvListeners()
        //agai startind s start kyu hota hai
    }
    // isliye pagenumber ko  yha parameter m dala h taki oper s jb function call kre tb send kre
    // dekho age smj m ayega
    // pagination chal ra h
    fun callApi(pageNumber:Int){
    Log.v("pnumber", pageNumber.toString())
        val request = ServiceBuilder.buildService(TmdbEndpoint::class.java)

        request.getMovies(
            getString(R.string.api_Key),
            pageNumber
        ).also {
            it.enqueue(object : Callback<PopularMovies>{
                override fun onResponse(call: Call<PopularMovies>, response: Response<PopularMovies>) {
                    if (response.isSuccessful){
                        resultList.addAll(response.body()!!.results)
                        Log.v("pnumber22", resultList.size.toString())
                        rvAdapter.updateMovieList(resultList)
                        progress_bar.visibility = View.GONE

                    }
                }
                override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    private  fun setRvListeners(){
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = recyclerView.layoutManager?.childCount ?:0
                totalItemCount= recyclerView.layoutManager?.itemCount ?:0
                pastVisibleItemCount=(recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                Log.v("countValue","vCount - $visibleItemCount, totalItemCount $totalItemCount, pastvis count $pastVisibleItemCount")
                if (isScrolling && visibleItemCount+pastVisibleItemCount==totalItemCount)
                {
                    isScrolling = false
                    progress_bar.visibility= View.VISIBLE
                    page++
                    callApi(page)


                }
            }
        })
    }

}


