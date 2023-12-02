package com.example.moviesapp.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.moviesapp.Adapter.SearchAdapter
import com.example.moviesapp.Adapter.SearchNameAdapter
import com.example.moviesapp.Model.MovieByName
import com.example.moviesapp.Model.listGeners
import com.example.moviesapp.Network.MovieService
import com.example.moviesapp.R
import com.example.moviesapp.databinding.FragmentSearchBinding
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment(R.layout.fragment_search) {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    val movieService: MovieService by lazy {
        MovieService()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var receivedData: String? = null
        var textSearch: String? = null
        if (arguments?.getString("CategoryId") != null) {
            receivedData = arguments?.getString("CategoryId")
        }
        if (arguments?.getString("txtSearch") != null) {
            textSearch = arguments?.getString("txtSearch")
        }
        displaySearch(receivedData, textSearch)
        binding.iconSearch4.setOnClickListener {
            if (binding.txtTextSearch4.text.toString() != null) {
                var textSearchV2 = binding.txtTextSearch4.text.toString()
                displaySearch(receivedData,textSearchV2)
            }
        }
        binding.iconBack.setOnClickListener {
            val controller = findNavController()
            controller.popBackStack()
        }

    }

    private fun displaySearch(receivedData: String?,textSearch: String?) {
        if (receivedData != null) {
//            val receivedData = arguments?.getString("CategoryId")
            movieService.api.getListGenre(receivedData!!.toInt())
                .enqueue(object : Callback<listGeners> {
                    override fun onResponse(
                        call: Call<listGeners>,
                        response: Response<listGeners>
                    ) {
                        binding.rvSearch.visibility = View.VISIBLE
                        binding.emptyIcon.visibility = View.GONE
                        binding.emptyText1.visibility = View.GONE
                        binding.emptyText2.visibility = View.GONE
                        binding.iconSearch4.visibility = View.GONE
                        binding.txtTextSearch4.visibility = View.GONE
                        val listMovieByGener = response.body()
                        binding.rvSearch.adapter = SearchAdapter(listMovieByGener!!.results) {
                            val intent = Intent(activity, DetailAcivity::class.java)
                                .putExtra("MovieId", it.id)
                            startActivity(intent)
                        }
                    }

                    override fun onFailure(call: Call<listGeners>, t: Throwable) {
                    }
                })
        } else if (textSearch != null) {
//            val textSearch = arguments?.getString("txtSearch")
            movieService.api.getMovieByName(txtName = textSearch!!)
                .enqueue(object : Callback<MovieByName> {
                    override fun onResponse(
                        call: Call<MovieByName>,
                        response: Response<MovieByName>
                    ) {
                        if (response.isSuccessful) {
                            val rs = response.body()
                            if (rs!!.results.size > 0) {
                                binding.rvSearch.visibility = View.VISIBLE
                                binding.emptyIcon.visibility = View.GONE
                                binding.emptyText1.visibility = View.GONE
                                binding.emptyText2.visibility = View.GONE
                                binding.iconSearch4.visibility = View.GONE
                                binding.txtTextSearch4.visibility = View.GONE
                                binding.rvSearch.adapter = SearchNameAdapter(rs!!.results) {
                                    val intent = Intent(activity, DetailAcivity::class.java)
                                        .putExtra("MovieId", it.id)
                                    startActivity(intent)

                                }
                            } else {
                                binding.rvSearch.visibility = View.GONE
                                binding.emptyIcon.visibility = View.VISIBLE
                                binding.emptyText1.visibility = View.VISIBLE
                                binding.emptyText2.visibility = View.VISIBLE
                                binding.iconSearch4.visibility = View.VISIBLE
                                binding.txtTextSearch4.visibility = View.VISIBLE
//                                binding.iconSearch4.setOnClickListener {
//                                    if (binding.txtTextSearch4.text.toString() != null) {
//                                        var textSearchV2 = binding.txtTextSearch4.text.toString()
//                                        displaySearch(receivedData,textSearchV2)
//                                    }
//                                }
                            }
                        } else {
                            Toast.makeText(requireActivity(), "bug", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<MovieByName>, t: Throwable) {
                        Toast.makeText(requireActivity(), "bug", Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }
}