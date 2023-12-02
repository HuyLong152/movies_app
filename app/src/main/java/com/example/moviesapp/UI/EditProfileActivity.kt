package com.example.moviesapp.UI

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.moviesapp.Account.UserAccount
import com.example.moviesapp.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class EditProfileActivity : AppCompatActivity() {
    lateinit var binding:ActivityEditProfileBinding
    lateinit var auth: FirebaseAuth
    lateinit var databaseReference: DatabaseReference
    private val PICK_IMAGE_REQUEST = 1
    private var seeIcon = false
    private var avtUri:Uri? = null
    lateinit var storage:FirebaseStorage
    lateinit var storageRef:StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        clickBack()
        displayInfo()
        clickIconSee()
        clickEditImg()
        clickSaveChange()
    }

    private fun clickSaveChange() {
       binding.saveChange.setOnClickListener {
           val curUser = auth.currentUser
           databaseReference = FirebaseDatabase.getInstance().getReference("userInfo").child(curUser!!.uid)
           val newUName = binding.editName.text.toString()


           databaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {
                   if(snapshot.exists()){
                       val infoUser = snapshot.getValue(UserAccount::class.java)
                       if(avtUri != null && newUName != infoUser!!.userName){
//                           databaseReference.removeValue()
                           UploadImageToFireBase(avtUri)
                           databaseReference.child("userName").setValue(newUName)
                               .addOnSuccessListener{
                                   Toast.makeText(this@EditProfileActivity, "Update Information Successful", Toast.LENGTH_SHORT).show()
                                   finish()
                               }
                               .addOnFailureListener{
                                   Toast.makeText(this@EditProfileActivity, "Update Information Failure", Toast.LENGTH_SHORT).show()
                                   finish()
                               }

                       }else if(avtUri != null){
                           UploadImageToFireBase(avtUri)
                           Toast.makeText(this@EditProfileActivity, "Update Avatar Successful", Toast.LENGTH_SHORT).show()
                           finish()
                       }else if(newUName != infoUser!!.userName){
//                           databaseReference.removeValue()
                           databaseReference.child("userName").setValue(newUName)
                               .addOnSuccessListener{
                                   Toast.makeText(this@EditProfileActivity, "Update Name Successful", Toast.LENGTH_SHORT).show()
                                   finish()
                               }
                               .addOnFailureListener{
                                   Toast.makeText(this@EditProfileActivity, "Update Name Failure", Toast.LENGTH_SHORT).show()
                                   finish()
                               }
                       }else{
                           Toast.makeText(this@EditProfileActivity, "The Information does not change", Toast.LENGTH_SHORT).show()
                       }
                   }
               }

               override fun onCancelled(error: DatabaseError) {

               }

           })
       }
    }

    private fun clickEditImg() {
        binding.editImg.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    private fun UploadImageToFireBase(selectedImage: Uri?) {
        val imageRef =storageRef.child("profileImages/${auth.currentUser!!.uid}")
        val upLoadTask :UploadTask = imageRef.putFile(selectedImage!!)
        upLoadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener{
                val imageUrl = it.toString()
                val uid = auth.currentUser!!.uid
                if(uid != null){
                    databaseReference.child("userImage").setValue(imageUrl)
                }
            }
        }
            .addOnFailureListener{
                Log.e("Error","Upload Failed")
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data
            try{
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,selectedImage)
                binding.profileImage.setImageBitmap(bitmap)
                avtUri = selectedImage!!
            }catch (e :Exception){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun clickIconSee() {
        binding.iconSee.setOnClickListener{
            if(!seeIcon){
                binding.editPass.inputType = InputType.TYPE_CLASS_TEXT
                seeIcon = true
            }else{
                binding.editPass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                seeIcon = false
            }
        }
    }

    private fun displayInfo() {
        val curUser = auth.currentUser
        databaseReference = FirebaseDatabase.getInstance().getReference("userInfo").child(curUser!!.uid)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val valueUser = snapshot.getValue(UserAccount::class.java)
                    binding.editName.setText(valueUser!!.userName)
                    binding.editPass.setText(valueUser!!.userPass)
                    if(valueUser.userImage != "") {
                        Glide.with(binding.profileImage)
                            .load(valueUser.userImage)
                            .into(binding.profileImage)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        binding.editEmail.setText(curUser!!.email)
    }

    private fun clickBack() {
        binding.iconSettingBack.setOnClickListener{
            finish()
        }
    }
}