package com.example.moviesapp.UI

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moviesapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragment = StartFragment() // tên fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment,fragment) // first_fm là id của framelayout
        transaction.addToBackStack(null) // đặt tên cho transaction nếu muốn sử dụng popbackstack để quay lại màn hình này nếu không thì để null
        transaction.commit()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}