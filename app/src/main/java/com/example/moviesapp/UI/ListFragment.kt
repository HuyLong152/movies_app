package com.example.moviesapp.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.moviesapp.Adapter.ListFavoriteAdapter
import com.example.moviesapp.Model.DetailAMovie
import com.example.moviesapp.Network.MovieService
import com.example.moviesapp.R
import com.example.moviesapp.database.MovieViewModel
import com.example.moviesapp.databinding.FragmentListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment(R.layout.fragment_list) {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    lateinit var movieViewModel: MovieViewModel
    var listMovie = ArrayList<DetailAMovie>()
    val movieService: MovieService by lazy {
        MovieService()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieViewModel = MovieViewModel(requireActivity())
        loadFavoriteMovies()
        clickBackIcon()
    }


    private fun clickBackIcon() {
        binding.iconListBack.setOnClickListener {
            val controller = findNavController()
            controller.popBackStack()
        }
    }

    private fun loadFavoriteMovies() {
        movieViewModel.getmovie.observe(requireActivity()) {
            if (it.isEmpty()) {
                binding.emptyListIcon.visibility = View.VISIBLE
                binding.emptyListText1.visibility = View.VISIBLE
                binding.emptyListText2.visibility = View.VISIBLE
                binding.rvListFavorite.visibility = View.GONE
            } else {
                listMovie.clear()
                for (i in it) {
                    Log.wtf("abc", it.size.toString())
                    movieService.api.getDetailById(i.IdMovie)
                        .enqueue(object : Callback<DetailAMovie> {
                            override fun onResponse(
                                call: Call<DetailAMovie>,
                                response: Response<DetailAMovie>
                            ) {
                                var aMovie = response.body()
                                listMovie.add(aMovie!!)

                                if (listMovie.size == it.size && listMovie.size >= 1) {
                                    binding.emptyListIcon.visibility = View.GONE
                                    binding.emptyListText1.visibility = View.GONE
                                    binding.emptyListText2.visibility = View.GONE
                                    binding.rvListFavorite.visibility = View.VISIBLE
                                    binding.rvListFavorite.adapter =
                                        ListFavoriteAdapter(listMovie) {
                                            val intent =
                                                Intent(activity, DetailAcivity::class.java)
                                                    .putExtra("MovieId", it.id)
                                            startActivity(intent)
                                        }
                                }
                            }
                            override fun onFailure(call: Call<DetailAMovie>, t: Throwable) {
                            }
                        })
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}