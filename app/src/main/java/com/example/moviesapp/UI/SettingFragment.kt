package com.example.moviesapp.UI

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.moviesapp.Account.UserAccount
import com.example.moviesapp.R
import com.example.moviesapp.Utils.Constrain
import com.example.moviesapp.databinding.FragmentHomeBinding
import com.example.moviesapp.databinding.FragmentSettingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment(R.layout.fragment_setting) {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val view = _binding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickBackIcon()
        displayInfo()
        logOut()
        clickListFavorite()
        changePassword()
        editProfile()
    }

    private fun editProfile() {
        binding.editProfile.setOnClickListener{
            val intent = Intent(requireActivity(),EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun changePassword() {
        binding.changePass.setOnClickListener{
            val intent = Intent(requireActivity(),ChangePassActivity::class.java)
            startActivity(intent)
        }


    }

    private fun clickListFavorite() {
        binding.yourfavorite.setOnClickListener {
            val controller = findNavController()
            controller.popBackStack(R.id.settingFragment, false)
            controller.navigate(R.id.action_settingFragment_to_listFragment)
        }
    }

    private fun logOut() {
        binding.logOut.setOnClickListener {
            val alert = AlertDialog.Builder(requireActivity())
            alert.apply { setMessage("Do you want to exit app ?")
                setTitle("Alert")
                setNegativeButton("Cancel"){ dialogInterface: DialogInterface, i: Int ->

                }
                setPositiveButton ("Yes"){ dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                    auth.signOut()
                    activity?.finish()
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                }
            }.show()

        }
    }

    private fun displayInfo() {
        val curUser = auth.currentUser
        databaseReference =
            FirebaseDatabase.getInstance().getReference("userInfo").child(curUser!!.uid)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(UserAccount::class.java)
                    binding.textName.text = user!!.userName
                    binding.textEmail.text = user!!.userEmail
                    if(user.userImage != ""){
                        Glide.with(binding.profileImage)
                            .load(user.userImage)
                            .into(binding.profileImage)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

    private fun clickBackIcon() {
        binding.iconSettingBack.setOnClickListener {
            val controller = findNavController()
            controller.popBackStack()
        }
    }
}