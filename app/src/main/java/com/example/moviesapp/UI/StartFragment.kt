package com.example.moviesapp.UI

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.moviesapp.R
import com.example.moviesapp.databinding.FragmentStartBinding
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 * Use the [StartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class StartFragment : Fragment(R.layout.fragment_start) {
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!! // chuyển thành dạng chỉ lấy được giá trị
    lateinit var auth: FirebaseAuth
    private var seeIcon = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        val view = binding.root
        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        binding.txtRegis.setOnClickListener{
            MoveFragment(RegisterFragment())
        }

        binding.btnSign.setOnClickListener {
            auth.signOut()
            var uP:String = binding.edtPass.text.toString()
            var uE:String = binding.edtEmail.text.toString()
            if(uP.isNotEmpty() && uE.isNotEmpty()){
                loginAccount(uP,uE)
            }else{
                Toast.makeText(requireActivity(), "Please enter full field", Toast.LENGTH_SHORT).show()
            }
        }
        if(auth.currentUser != null){
            val fragmentManage = requireActivity().supportFragmentManager
            var intent = Intent(requireActivity(), HomeActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        binding.iconSee.setOnClickListener{
            if(!seeIcon){
                binding.edtPass.inputType = InputType.TYPE_CLASS_TEXT
                seeIcon = true
            }else{
                binding.edtPass.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                seeIcon = false
            }
        }
        clickForgotPass()
    }

    private fun clickForgotPass() {
        binding.forgotPass.setOnClickListener{
            val alert = AlertDialog.Builder(requireActivity())
            if(binding.edtEmail.text.toString() != ""){
                alert.apply { setMessage("Confirm reset password")
                    setTitle("Alert")
                    setNegativeButton("Cancel"){ dialogInterface: DialogInterface, i: Int ->

                    }
                    setPositiveButton ("Yes"){ dialogInterface: DialogInterface, i: Int ->
                        auth.fetchSignInMethodsForEmail(binding.edtEmail.text.toString())
                            .addOnCompleteListener{
                                if(it.isSuccessful){
                                    val result = it.result?.signInMethods
                                    if(result != null && result.isNotEmpty()){
                                        val auth = FirebaseAuth.getInstance()
                                        auth.sendPasswordResetEmail(binding.edtEmail.text.toString())
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    Toast.makeText(requireContext(), "Please check your email", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        dialogInterface.dismiss()
                                    }else{
                                        Toast.makeText(requireContext(), "Your Email does not exits", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                    }
                }.show()

            }else{
                Toast.makeText(requireActivity(), "Please enter Email field", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun loginAccount(uP: String, uE: String) {
        auth.signInWithEmailAndPassword(uE,uP)
            .addOnSuccessListener{
//                val fragmentManage = requireActivity().supportFragmentManager
//                val transaction = fragmentManage.beginTransaction()
//                transaction.remove(this)
//                transaction.commit()
                var intent = Intent(requireActivity(), HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()

            }
            .addOnFailureListener{
                Toast.makeText(requireActivity(), "There is a problem with your account", Toast.LENGTH_SHORT).show()
            }
    }

    private fun MoveFragment(fragment : Fragment){
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.startFragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}