package com.tec.nuevoamanecer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class Tablero : AppCompatActivity() {

    private val uidAlumno = "studentUID" // Replace with the actual student UID
    private val databaseRef =
        FirebaseDatabase.getInstance().getReference("Tablero").child(uidAlumno)
    private val storageRef = FirebaseStorage.getInstance().reference.child("images")
    private lateinit var gridLayout: GridLayout

    // Use a result launcher to handle the image picker result
    private val imagePickerResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    val selectedButton =
                        gridLayout.findViewWithTag<Button>(currentSelectedButtonTag)
                    uploadImageToFirebaseStorage(uri) { uid, imageUrl ->
                        // Image uploaded successfully, now update the database
                        val name = findViewById<EditText>(R.id.first_input).text.toString()
                        val description = findViewById<EditText>(R.id.second_input).text.toString()
                        storeImageMetadataToDatabase(
                            selectedButton.tag.toString(),
                            imageUrl,
                            name,
                            description
                        )
                    }
                }
            }
        }

    private var currentSelectedButtonTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_tablero)

        gridLayout = findViewById(R.id.button_grid)

        assignUidsToButtons()

        val submitButton = findViewById<ImageButton>(R.id.btnSiguiente)
        submitButton.setOnClickListener {
            submitData()
        }
    }

    private fun assignUidsToButtons() {
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val uids = dataSnapshot.children.mapNotNull { it.key }
                val buttonCount = gridLayout.childCount

                for (i in 0 until buttonCount) {
                    val button = gridLayout.getChildAt(i) as Button
                    val uid = if (i < uids.size) uids[i] else UUID.randomUUID().toString()
                    button.tag = uid
                    button.setOnClickListener {
                        currentSelectedButtonTag = uid
                        checkButtonData(button, uid)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun checkButtonData(button: Button, uidButton: String) {
        databaseRef.child(uidButton).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                // Button already has an image and data, populate the fields for editing
                val description = dataSnapshot.child("Description").value as String?
                val name = dataSnapshot.child("name").value as String?
                findViewById<EditText>(R.id.first_input).setText(name)
                findViewById<EditText>(R.id.second_input).setText(description)
            } else {
                // Button is empty, prompt to pick an image
                promptImageSelection()
            }
        }.addOnFailureListener {
            // Handle error
        }
    }

    private fun submitData() {
        val name = findViewById<EditText>(R.id.first_input).text.toString()
        val description = findViewById<EditText>(R.id.second_input).text.toString()
        val uidButton = currentSelectedButtonTag ?: return

        val buttonRef = databaseRef.child(uidButton)

        val data = mapOf(
            "name" to name,
            "Description" to description
            // Add other data fields if necessary
        )

        buttonRef.updateChildren(data).addOnSuccessListener {
            // Update UI to reflect that the data was submitted successfully
        }.addOnFailureListener {
            // Handle failure
        }
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri, onComplete: (String, String) -> Unit) {
        val uid = UUID.randomUUID().toString()
        val imageRef = storageRef.child("$uid.jpg")

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()
                    onComplete(uid, imageUrl)
                }
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    private fun storeImageMetadataToDatabase(
        uid: String,
        imageUrl: String,
        name: String,
        description: String
    ) {
        val imageData = mapOf(
            "name" to name,
            "Description" to description,
            "url" to imageUrl
        )

        databaseRef.child(uid).setValue(imageData).addOnSuccessListener {
            // Data saved successfully
        }.addOnFailureListener {
            // Handle failure
        }
    }

    private fun promptImageSelection() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerResultLauncher.launch(intent)
    }
}