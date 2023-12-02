package com.example.moviesapp.UI

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.moviesapp.Account.UserAccount
import com.example.moviesapp.R
import com.example.moviesapp.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment(R.layout.fragment_register) {
    private var _binding : FragmentRegisterBinding?=null
    private val binding get() = _binding!!
    private var seeIcon = false
    lateinit var auth: FirebaseAuth
    lateinit var databaseReference: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        var view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.btnSign.setOnClickListener {
            var uN:String = binding.edtName.text.toString()
            var uP:String = binding.edtPass.text.toString()
            var uE:String = binding.edtEmail.text.toString()
            if(uN.isNotEmpty() && uP.isNotEmpty() && uE.isNotEmpty()){
                createAccount(uN,uP,uE)
            }else{
                Toast.makeText(requireContext(), "Please enter full field", Toast.LENGTH_SHORT).show()
            }
        }
        binding.iconSee.setOnClickListener{
            if(!seeIcon){
                binding.edtPass.inputType = InputType.TYPE_CLASS_TEXT
                seeIcon = true
            }else{
                binding.edtPass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                seeIcon = false
            }
        }
    }

    private fun createAccount(uN: String, uP: String, uE: String) {
        auth.createUserWithEmailAndPassword(uE,uP)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    var currentAcc = auth.currentUser
                    databaseReference = FirebaseDatabase.getInstance().getReference("userInfo").child(currentAcc!!.uid)
                    val newUser = UserAccount(uN, uE, "",uP)
                    databaseReference.setValue(newUser)
                        .addOnSuccessListener{
                            auth.signOut()
                            val fragmentManage = requireActivity().supportFragmentManager
                            val transaction = fragmentManage.beginTransaction()
                            transaction.remove(this)
                            transaction.commit()
                            Toast.makeText(requireActivity(), "Create account successful!!", Toast.LENGTH_SHORT).show()
                            MoveFragment(StartFragment())
                        }
                }
            }
            .addOnFailureListener{
                Toast.makeText(requireActivity(), "Create account fail!!", Toast.LENGTH_SHORT).show()
            }
    }
    private fun MoveFragment(fragment : Fragment){
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.startFragment, fragment)
        transaction.addToBackStack("")
        transaction.commit()
    }
}