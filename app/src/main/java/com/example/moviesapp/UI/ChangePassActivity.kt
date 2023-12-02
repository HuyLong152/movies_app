package com.example.moviesapp.UI

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.text.InputType
import android.widget.Toast
import com.example.moviesapp.R
import com.example.moviesapp.databinding.ActivityChangePassBinding
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePassActivity : AppCompatActivity() {
    lateinit var binding: ActivityChangePassBinding
    private var seeIcon = false
    private var seeIcon2 = false
    private var seeIcon4 = false
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangePassBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)
        clickSeePass()
        clickChangePass()
    }

    private fun clickChangePass() {
        var curUser = auth.currentUser
        binding.changePass.setOnClickListener {
            if (checkEnterFullField()) {
                val credential = EmailAuthProvider.getCredential(
                    curUser!!.email.toString(),
                    binding.edtPass.text.toString()
                )
                curUser?.reauthenticate(credential)
                    ?.addOnCompleteListener {
                        if(it.isSuccessful){
                            val pw = binding.edtPass2.text.toString()
                            curUser!!.updatePassword(pw)
                                .addOnCompleteListener {ud ->
                                    if (ud.isSuccessful) {
                                        Toast.makeText(this,"Update Password Successful !!",Toast.LENGTH_SHORT).show()
                                    }
                                    finish()
                                }
                        }else{
                            Toast.makeText(this, "The password is incorrect", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }
    }

    @SuppressLint("ResourceAsColor")
    private fun checkEnterFullField(): Boolean {
        if (binding.edtPass.text.toString() == "") {
            binding.edtPass.setHint("Enter Full Field !!!")
            val colorValue = resources.getColor(R.color.error, null)
            binding.edtPass.setHintTextColor(colorValue)
            return false
        }
        if (binding.edtPass2.text.toString() == "") {
            binding.edtPass2.setHint("Enter Full Field !!!")
            val colorValue = resources.getColor(R.color.error, null)
            binding.edtPass2.setHintTextColor(colorValue)
            return false
        }
        if (binding.edtPass4.text.toString() == "") {
            binding.edtPass4.setHint("Enter Full Field !!!")
            val colorValue = resources.getColor(R.color.error, null)
            binding.edtPass4.setHintTextColor(colorValue)
            return false
        }
        if (binding.edtPass2.text.toString() != binding.edtPass4.text.toString()) {
            Toast.makeText(this, "New password don't match", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun clickSeePass() {
        binding.iconSee.setOnClickListener {
            if (!seeIcon) {
                binding.edtPass.inputType = InputType.TYPE_CLASS_TEXT
                seeIcon = true
            } else {
                binding.edtPass.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                seeIcon = false
            }
        }
        binding.iconSee2.setOnClickListener {
            if (!seeIcon2) {
                binding.edtPass2.inputType = InputType.TYPE_CLASS_TEXT
                seeIcon2 = true
            } else {
                binding.edtPass2.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                seeIcon2 = false
            }
        }
        binding.iconSee4.setOnClickListener {
            if (!seeIcon4) {
                binding.edtPass4.inputType = InputType.TYPE_CLASS_TEXT
                seeIcon4 = true
            } else {
                binding.edtPass4.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                seeIcon4 = false
            }
        }
    }
}