package com.luqmanfajar.story_app.ui.add_story

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.luqmanfajar.story_app.createCustomTempFile
import com.luqmanfajar.story_app.data.preference.LoginPreferences
import com.luqmanfajar.story_app.data.preference.PreferencesFactory
import com.luqmanfajar.story_app.data.viewmodel.*

import com.luqmanfajar.story_app.dataStore
import com.luqmanfajar.story_app.databinding.ActivityAddStoryBinding
import com.luqmanfajar.story_app.reduceFileImage
import com.luqmanfajar.story_app.ui.story.StoryActivity
import com.luqmanfajar.story_app.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import com.luqmanfajar.story_app.utils.Result
import com.luqmanfajar.story_app.utils.UiUtils

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String



    private var getFile: File? = null

    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        const val EXTRA_STORY = "extra_token"
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS){
        if (!allPermissionsGranted()) {
            Toast.makeText(
                this,
                "Not getting a permission.",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            }
        }

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        val pref = LoginPreferences.getInstance(dataStore)

        val loginViewModel = ViewModelProvider(this, PreferencesFactory(pref)).get(
            AuthViewModel::class.java
        )

        loginViewModel.getAuthKey().observe(this
        ){
                authToken : String ->
            val auth = "Bearer $authToken"

            binding.buttonAdd.setOnClickListener{uploadStories(auth)}

        }


        binding.btnCamera.setOnClickListener{startTakePhoto()}
        binding.btnGallery.setOnClickListener{startGallery()}


    }
    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.luqmanfajar.story_app",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this@AddStoryActivity)

            getFile = myFile

            binding.prevImage.setImageURI(selectedImg)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.prevImage.setImageBitmap(result)
        }
    }


    private fun uploadStories(auth: String){

        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val txtDesc = binding.edAddDescription.text.toString()
            val description = txtDesc.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )


            viewModel.addStory(auth,imageMultipart,description).observe(this) { result ->
                when (result){
                    is Result.Loading ->{
                        UiUtils.show(this)
                    }
                    is Result.Success ->{
                        val i = Intent(this@AddStoryActivity, StoryActivity::class.java)
                        startActivity(i,ActivityOptionsCompat.makeSceneTransitionAnimation(this@AddStoryActivity).toBundle())

                        Toast.makeText(this@AddStoryActivity, "Upload Story Sukses", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is Result.Error ->{
                        Toast.makeText(this@AddStoryActivity, "Upload Gagal : ", Toast.LENGTH_SHORT).show()
                    }

                    }

                }

            }
        else {
            Toast.makeText(this@AddStoryActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
            }

        }

    }


