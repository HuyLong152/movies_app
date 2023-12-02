package com.example.moviesapp.UI

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.moviesapp.Account.UserFavorite
import com.example.moviesapp.Model.Casts
import com.example.moviesapp.Model.DetailAMovie
import com.example.moviesapp.Model.Videos
import com.example.moviesapp.Network.MovieService
import com.example.moviesapp.Utils.Constrain
import com.example.moviesapp.database.FavoriteMovie
import com.example.moviesapp.database.MovieViewModel
import com.example.moviesapp.databinding.ActivityDetailAcivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.Date

class DetailAcivity : AppCompatActivity() {
    lateinit var movieViewModel: MovieViewModel
    private val movieSevice: MovieService by lazy {
        MovieService()
    }
    lateinit var auth:FirebaseAuth
    lateinit var databaseReference:DatabaseReference
    lateinit var binding: ActivityDetailAcivityBinding
    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAcivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieViewModel = MovieViewModel(this)
        auth = FirebaseAuth.getInstance()
        enableImmersiveMode()
        val MovieId = intent.getIntExtra("MovieId", 0)
        val listCasts = mutableListOf<String>()
        movieSevice.api.getCastById(MovieId).enqueue(object : Callback<Casts> {
            override fun onResponse(call: Call<Casts>, response: Response<Casts>) {
                val casts = response.body()
                for (i in casts!!.cast) {
                    listCasts.add(i.name)
                }
                binding.txtCast.text = listCasts.joinToString(separator = " , ")
            }

            override fun onFailure(call: Call<Casts>, t: Throwable) {
            }
        })

        movieSevice.api.getDetailById(MovieId).enqueue(object : Callback<DetailAMovie> {
            override fun onResponse(call: Call<DetailAMovie>, response: Response<DetailAMovie>) {
                val DetailMovie = response.body()
                binding.imgDetail
                Glide.with(binding.imageView)
                    .load(Constrain.BASE_URL_IMG + DetailMovie!!.poster_path)
                    .into(binding.imgDetail)
                binding.txtName.text = DetailMovie.title
                val listCar = mutableListOf<String>()
                for (i in DetailMovie.genres) {
                    listCar.add(i.name)
                }
                val txtCate = listCar.joinToString(separator = " . ")
                binding.txtCategory.text = txtCate
                binding.txtRate.text = "${DetailMovie.vote_average} / 10"
                binding.txtDescript.text = DetailMovie.overview
                val moviH = DetailMovie.runtime / 60
                val moviM = DecimalFormat("#.###").format(DetailMovie.runtime % 60)
                binding.txtTime.text = "${moviH}h ${moviM}mis"
            }

            override fun onFailure(call: Call<DetailAMovie>, t: Throwable) {
            }
        })
        binding.iconback.setOnClickListener {
            finish()
        }
        addFavoriteMovie(MovieId)

        getMovieTrailer(MovieId)
    }

    private fun clickWatchVideo(keyMovie:String = "null") {

        binding.buttonTrailer.setOnClickListener {
//            binding.videoTrailer.rotation = 90.0f
//            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            binding.videoTrailer.visibility = View.VISIBLE
            binding.iconCloseVideo.visibility = View.VISIBLE
            binding.iconCloseVideo.setOnClickListener {
                binding.videoTrailer.visibility = View.GONE
                binding.iconCloseVideo.visibility = View.GONE
                val webView = WebView(this)
                binding.videoTrailer.loadUrl("about:blank")
            }
            val webSettings: WebSettings = binding.videoTrailer.settings
            webSettings.javaScriptEnabled = true

            // Xử lý các sự kiện và cho phép video được chơi trong WebView
            binding.videoTrailer.webViewClient = WebViewClient()
            binding.videoTrailer.webChromeClient = WebChromeClient()

            var videoId: String = keyMovie
            val html = """
    <html>
        <head>
            <style>
                body { margin: 0; padding: 0; }
                iframe { border: none; }
            </style>
        </head>
        <body>
            <iframe width="100%" height="100%" src="https://www.youtube.com/embed/$videoId" frameborder="0" allowfullscreen></iframe>
        </body>
    </html>
"""
            binding.videoTrailer.loadData(html, "text/html", "utf-8")

        }
    }

    private fun getMovieTrailer(idMovie: Int){
        movieSevice.api.getVideoById(idMovie).enqueue(object:Callback<Videos>{
            override fun onResponse(call: Call<Videos>, response: Response<Videos>) {
                if(response.isSuccessful){
                    val responseVideos = response.body()
                    for(i in responseVideos!!.results){
                        if(i.type.equals("Trailer",ignoreCase = true))
                            clickWatchVideo(i.key)
                    }

                }
            }

            override fun onFailure(call: Call<Videos>, t: Throwable) {
            }

        })
    }

    private fun addFavoriteMovie(IdMovie: Int) {
        var resul: Int = 0
        movieViewModel.isExistId(IdMovie).observe(this, Observer {
            resul = it
            if (resul > 0) {
                binding.iconNoFavorite.visibility = View.GONE
                binding.iconFavorite.visibility = View.VISIBLE
            } else {
                binding.iconNoFavorite.visibility = View.VISIBLE
                binding.iconFavorite.visibility = View.GONE
            }
        })
        binding.iconNoFavorite.setOnClickListener {
            binding.iconFavorite.visibility = View.VISIBLE
            binding.iconNoFavorite.visibility = View.GONE
            var newMovie = FavoriteMovie(0, IdMovie)
            movieViewModel.insertMovie(newMovie)

            /////favorites
            val date = Date()
            databaseReference = FirebaseDatabase.getInstance().getReference("UserFavorite").child(auth.currentUser!!.uid)
            databaseReference.push()
                .setValue(UserFavorite(IdMovie,date.time))
            /////
            Toast.makeText(this, "Add favorite movie successful ^^", Toast.LENGTH_SHORT).show()
        }
        binding.iconFavorite.setOnClickListener {
            binding.iconFavorite.visibility = View.GONE
            binding.iconNoFavorite.visibility = View.VISIBLE
            GlobalScope.launch {
                movieViewModel.removeMovieById(IdMovie)
            }
            /////favorites
            databaseReference = FirebaseDatabase.getInstance().getReference("UserFavorite").child(auth.currentUser!!.uid)
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            val itemFavorite = i.getValue(UserFavorite::class.java)
                            if (itemFavorite?.idMovie == IdMovie) {
                                // Kiểm tra điều kiện và xóa nếu đúng
                                i.ref.removeValue()
                                break
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Xử lý khi có lỗi
                }
            })
            /////
            Toast.makeText(this, "Remove favorite movie successful @@", Toast.LENGTH_SHORT).show()
        }

    }

    fun enableImmersiveMode() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            val decorView = window.decorView
//            decorView.systemUiVisibility = (
//                    View.SYSTEM_UI_FLAG_IMMERSIVE
//                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            or View.SYSTEM_UI_FLAG_FULLSCREEN)
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val decorView = window.decorView
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

}