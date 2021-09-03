package com.example.whatsapp_clone.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.whatsapp_clone.databinding.ActivityProfileBinding
import com.example.whatsapp_clone.model.User
import com.example.whatsapp_clone.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val firebaseDB =  FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance().reference
    private val userID  = FirebaseAuth.getInstance().currentUser?.uid
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (userID.isNullOrEmpty()) {
            finish()
        }

        userInfo()

        binding.btnApply.setOnClickListener{ apply() }
        binding.btnDeleteAccount.setOnClickListener { delete() }
        binding.ivPhotoProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_PHOTO)
        }
    }

    private fun userInfo() {
        firebaseDB.collection(DATA_USERS)
            .document(userID!!)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                imageUrl = user?.imageUrl
                binding.etNameProfile.setText(user?.name)
                binding.etEmailProfile.setText(user?.email)
                binding.etPhoneProfile.setText(user?.phone)
                if (imageUrl != null) {
                    userImage(this, user?.imageUrl!!, binding.ivPhotoProfile)
                }
            }
    }

    fun apply() {
        val name = binding.etNameProfile.text.toString()
        val email = binding.etEmailProfile.text.toString()
        val phone = binding.etPhoneProfile.text.toString()

        val map = HashMap<String, Any>()
        map[DATA_USER_NAME] = name
        map[DATA_USER_EMAIL] = email
        map[DATA_USER_PHONE] = phone

        firebaseDB.collection(DATA_USERS)
            .document(userID!!)
            .update(map)
            .addOnSuccessListener {
                Toast.makeText(this, "Update Succesful", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
            }
    }

    fun delete() {
        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("This will delete your profile information. Are you sure?")
            .setPositiveButton("Yes") { dialog, which ->
                Toast.makeText(this, "Profile deleted", Toast.LENGTH_SHORT).show()
                firebaseDB.collection(DATA_USERS).document(userID!!).delete()
                finish()
            }
            .setNegativeButton("No") {dialog, which -> }
            .show()
    }

    private fun userImage(context: Context, uri: String, imageView: ImageView) {
        val option = RequestOptions().placeholder(progresDrawable(context))
        Glide.with(context)
            .load(uri)
            .apply(option)
            .into(imageView)
    }

    private fun progresDrawable(context: Context): CircularProgressDrawable {
        return CircularProgressDrawable(context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PHOTO) {
            storeImage(data?.data)
        }
    }

    private fun storeImage(imageUri: Uri?) {
        if (imageUri != null) {
            Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show()
            val filePath = firebaseStorage.child(DATA_IMAGES).child(userID!!)

            filePath.putFile(imageUri)
                .addOnSuccessListener {
                    filePath.downloadUrl
                        .addOnSuccessListener { taskSnapshot ->
                            val url = taskSnapshot.toString()
                            firebaseDB.collection(DATA_USERS)
                                .document(userID)
                                .update(DATA_USER_IMAGE_URL, url)
                                .addOnSuccessListener {
                                    imageUrl = url
                                    userImage(this, imageUrl!!, binding.ivPhotoProfile)
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Image Upload failed. Please try again later.", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Image Upload failed. Please try again later.", Toast.LENGTH_SHORT).show()
                }
        }
    }
}