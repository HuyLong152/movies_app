package com.example.moviesapp.UI

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.moviesapp.R
import com.example.moviesapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var controller: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        val fragment = HomeFragment() // tên fragment
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.homefm,fragment) // first_fm là id của framelayout
//        transaction.addToBackStack(null) // đặt tên cho transaction nếu muốn sử dụng popbackstack để quay lại màn hình này nếu không thì để null
//        transaction.commit()
        controller = findNavController(R.id.nav_host_fragment)
        binding.navBottom.setupWithNavController(controller)

    }
}