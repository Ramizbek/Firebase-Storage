package ramizbek.aliyev.firebasestorage

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ramizbek.aliyev.firebasestorage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var reference: StorageReference
    private val TAG = "MainActivity"
    var imageUrl: String =
        "https://firebasestorage.googleapis.com/v0/b/storageexample-e347f.appspot.com/o/images%2F1659522205930?alt=media&token=45550870-2b39-4ea0-a6a1-2a3c3bce8d3b"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this).load(imageUrl).into(binding.imageView)
        firebaseStorage = FirebaseStorage.getInstance()
        reference = firebaseStorage.getReference("images")

        binding.imageView.setOnClickListener {
            getImageContent.launch("image/*")
        }
    }

    private var getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val m = System.currentTimeMillis()

            binding.progressbar.visibility = View.VISIBLE
            binding.imageView.visibility = View.VISIBLE

            val task = reference.child(m.toString()).putFile(uri)

            task.addOnSuccessListener {
                binding.imageView.setImageURI(uri)
                binding.progressbar.visibility = View.GONE

                if (it.task.isSuccessful) {
                    val downloadUrl = it.metadata?.reference?.downloadUrl
                    downloadUrl?.addOnSuccessListener { imageUri ->
                        imageUrl = imageUri.toString()
                        Log.d(TAG, "$imageUrl: ")
                    }
                }
            }

            task.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }

        }
}