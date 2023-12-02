package com.example.moviesapp.UI

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.moviesapp.Account.UserAccount
import com.example.moviesapp.Account.UserFavorite
import com.example.moviesapp.Adapter.CategoryAdapter
import com.example.moviesapp.Adapter.NotificationAdapter
import com.example.moviesapp.Adapter.NowPlayingAdapter
import com.example.moviesapp.Adapter.PopularAdapter
import com.example.moviesapp.Model.Category
import com.example.moviesapp.Model.DetailAMovie
import com.example.moviesapp.Model.FavoriteInfo
import com.example.moviesapp.Model.Genre
import com.example.moviesapp.Model.MovieNowPlaying
import com.example.moviesapp.Model.MoviePopular
import com.example.moviesapp.Network.MovieService
import com.example.moviesapp.R
import com.example.moviesapp.database.FavoriteMovie
import com.example.moviesapp.database.MovieViewModel
import com.example.moviesapp.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var CategoryList: List<Genre>? = null
    lateinit var auth: FirebaseAuth
    lateinit var databaseReference: DatabaseReference
    private val movieService: MovieService by lazy {
        MovieService()
    }

    private val movieViewModel: MovieViewModel by lazy {
        MovieViewModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        databaseReference =
            FirebaseDatabase.getInstance().getReference("userInfo").child(auth.currentUser!!.uid)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val currentUser = snapshot.getValue(UserAccount::class.java)
                    binding.txtNameUser.text = "Welcome Back ${currentUser!!.userName} 👋"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        movieViewModel.getmovie.observe(requireActivity()){
            binding.numberNotifi.text = it.size.toString()
        }
        loadPlayingMovie()
        loadCategory()
        loadPopular()
        searchMovieByName()
        clickIconNotification()
    }

    private fun clickIconNotification() {
        var isClickIcon = false
        binding.iconNotification.setOnClickListener{
            if(!isClickIcon){
                isClickIcon = true
                binding.layoutNotifi.visibility =View.VISIBLE
                val tempListFavorite = mutableListOf<FavoriteInfo>()
                databaseReference =FirebaseDatabase.getInstance().getReference("UserFavorite").child(auth.currentUser!!.uid)
                databaseReference.addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            for(i in snapshot.children){
                                val itemFavorite = i.getValue<UserFavorite>()
                                movieService.api.getDetailById(itemFavorite!!.idMovie).enqueue(object :Callback<DetailAMovie>{
                                    override fun onResponse(
                                        call: Call<DetailAMovie>,
                                        response: Response<DetailAMovie>
                                    ) {
                                        if(response.isSuccessful){
                                            val detailMovie = response.body()
                                            val newFMovie = FavoriteInfo(itemFavorite.idMovie,detailMovie!!.original_title,detailMovie!!.poster_path,itemFavorite.timeClick)
                                            tempListFavorite.add(newFMovie)
                                            if(tempListFavorite.size == snapshot.childrenCount.toInt()){
                                                binding.rvNotification.adapter = NotificationAdapter(tempListFavorite){
                                                    val intent = Intent(activity, DetailAcivity::class.java)
                                                        .putExtra("MovieId", it.movieId)
                                                    startActivity(intent)
                                                }
                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<DetailAMovie>, t: Throwable) {

                                    }

                                })

                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
            }else{
                isClickIcon = false
                binding.layoutNotifi.visibility =View.GONE
            }
        }
    }

    private fun searchMovieByName() {
        binding.iconSearch.setOnClickListener {

            val textSearch: String = binding.txtTextSearch.text.toString()
            if (textSearch != "") {
                val controller = findNavController()
                val bundle = Bundle()
                bundle.putString("txtSearch", textSearch)
                controller.navigate(R.id.action_homeFragment_to_searchFragment, bundle)
            }
        }
    }

    private fun loadPopular() {
        //dung call
//        movieService.api.getListCategory().enqueue(object : Callback<Category> {
//            override fun onResponse(call: Call<Category>, response: Response<Category>) {
//                if (response.isSuccessful) {
//                    val category = response.body()
//                    CategoryList = category!!.genres
//                    movieService.api.getListPopular().enqueue(object : Callback<MoviePopular> {
//                        override fun onResponse(
//                            call: Call<MoviePopular>,
//                            response: Response<MoviePopular>
//                        ) {
//                            if (response.isSuccessful) {
//                                val popular = response.body()
//                                if (popular != null) {
//                                    binding.rvPopular.adapter =
//                                        PopularAdapter(popular.results, CategoryList!!) {
//                                            val intent = Intent(activity, DetailAcivity::class.java)
//                                                .putExtra("MovieId", it.id)
//                                            startActivity(intent)
//                                        }
//                                }
//                            } else {
//                                Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT)
//                                    .show()
//                            }
//                        }
//
//                        override fun onFailure(call: Call<MoviePopular>, t: Throwable) {
//                            Log.wtf("error", t.message)
//                        }
//
//                    })
//                } else {
//                    Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<Category>, t: Throwable) {
//                Log.wtf("error", t.message)
//            }
//
//        })

        //dung viewModel và Call
        movieViewModel.listCategory.observe(viewLifecycleOwner){
            CategoryList = it!!.genres
            movieService.api.getListPopular().enqueue(object : Callback<MoviePopular> {
                override fun onResponse(
                    call: Call<MoviePopular>,
                    response: Response<MoviePopular>
                ) {
                    if (response.isSuccessful) {
                        val popular = response.body()
                        if (popular != null) {
                            binding.rvPopular.adapter =
                                PopularAdapter(popular.results, CategoryList!!) {
                                    val intent = Intent(activity, DetailAcivity::class.java)
                                        .putExtra("MovieId", it.id)
                                    startActivity(intent)
                                }
                        }
                    } else {
                        Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<MoviePopular>, t: Throwable) {
                    Log.wtf("error", t.message)
                }

            })
        }
    }

    private fun loadCategory() {
        //dung call
//        movieService.api.getListCategory().enqueue(object : Callback<Category> {
//            override fun onResponse(call: Call<Category>, response: Response<Category>) {
//                if (response.isSuccessful) {
//                    val category = response.body()
//                    binding.rvCategory.adapter = CategoryAdapter(category!!.genres) {
//                        val controller = findNavController()
//                        val bundle = Bundle()
//                        bundle.putString("CategoryId", it.id.toString())
//                        controller.navigate(R.id.action_homeFragment_to_searchFragment, bundle)
//                    }
//
//                } else {
//                    Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<Category>, t: Throwable) {
//                Log.wtf("error", t.message)
//            }
//
//        })

        //dung viewmodel
        movieViewModel.listCategory.observe(viewLifecycleOwner){
            category ->
            binding.rvCategory.adapter = CategoryAdapter(category!!.genres) {
                        val controller = findNavController()
                        val bundle = Bundle()
                        bundle.putString("CategoryId", it.id.toString())
                        controller.navigate(R.id.searchFragment, bundle)
                parentFragmentManager.popBackStack(R.id.HomeFragment,0)

                    }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun loadPlayingMovie() {
        //dung call
//        movieService.api.getMoviePlay().enqueue(object : Callback<MovieNowPlaying> {
//            override fun onResponse(
//                call: Call<MovieNowPlaying>,
//                response: Response<MovieNowPlaying>
//            ) {
//                if (response.isSuccessful) {
//                    val listPlaying = response.body()
//                    binding.rvPlaying.adapter = NowPlayingAdapter(listPlaying!!.results) {
//                        val intent = Intent(activity, DetailAcivity::class.java)
//                            .putExtra("MovieId", it.id)
//                        startActivity(intent)
//                    }
//                } else {
//                    Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<MovieNowPlaying>, t: Throwable) {
//                Log.wtf("error", t.message)
//            }
//        })

        //dung viewModel
        movieViewModel.listPlaying.observe(viewLifecycleOwner) {
            binding.rvPlaying.adapter = NowPlayingAdapter(it!!.results) {
                val intent = Intent(activity, DetailAcivity::class.java)
                    .putExtra("MovieId", it.id)
                    startActivity(intent)
            }
        }
    }
}