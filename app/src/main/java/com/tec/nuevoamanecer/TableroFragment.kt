package com.tec.nuevoamanecer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.bumptech.glide.Glide
import java.util.*
import android.os.Handler
import android.os.Looper


class TableroFragment : Fragment() {
    private var currentStartIndex = 0
    private val uidList = mutableListOf<String>()
    private lateinit var uidAlumno: String
    private lateinit var gridLayout: GridLayout
    private var currentSelectedButtonTag: String? = null

    private val databaseRef by lazy {
        FirebaseDatabase.getInstance().getReference("Usuarios").child("Tablero").child(uidAlumno)
    }
    private val storageRef = FirebaseStorage.getInstance().reference.child("images")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uidAlumno = arguments?.getString("userUID").orEmpty()
        Toast.makeText(requireContext(), uidAlumno, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tablero, container, false)

        gridLayout = view.findViewById(R.id.button_grid)
        assignUidsToButtons(view)

        // Programmatically trigger a click event for each button
        view.post {
            for (i in 0 until gridLayout.childCount) {
                val childView = gridLayout.getChildAt(i)
                if (childView is Button || childView is ImageButton) {
                    childView.performClick()
                    Toast.makeText(requireContext(), "click...", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val submitButton = view.findViewById<ImageButton>(R.id.btnSiguiente)
        submitButton.setOnClickListener {
            submitData(view)
        }



        return view
    }




    private fun assignUidsToButtons(view: View) {
        Toast.makeText(requireContext(), "Cargando...", Toast.LENGTH_SHORT).show()
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val uids = dataSnapshot.children.mapNotNull { it.key }
                val buttonCount = gridLayout.childCount

                for (i in 0 until buttonCount) {
                    val childView = gridLayout.getChildAt(i)
                    val uid = uids.getOrNull(i) ?: UUID.randomUUID().toString()

                    childView.tag = uid
                    if (childView is ImageButton) {
                        loadImageForButton(childView, uid)
                    }
                }
                Toast.makeText(requireContext(), "Cargado", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }
    private fun checkButtonData(view: View, uidButton: String) {
        Toast.makeText(requireContext(), "Cargando...", Toast.LENGTH_SHORT).show()
        databaseRef.child(uidButton).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val description = dataSnapshot.child("Description").value as String?
                val name = dataSnapshot.child("name").value as String?
                val imageUrl = dataSnapshot.child("url").value as String?

                this.view?.findViewById<EditText>(R.id.first_input)?.setText(name)
                this.view?.findViewById<EditText>(R.id.second_input)?.setText(description)

                if (view is ImageButton && imageUrl != null) {
                    loadImageIntoImageButton(view, imageUrl)
                } else if (view is Button) {
                    // If it's a regular Button, you may want to perform some other action
                }
            } else {
                // If no data exists, prompt to pick an image
                promptImageSelection()
            }
        }.addOnFailureListener {
            // Handle error
        }
        Toast.makeText(requireContext(), "Cargado", Toast.LENGTH_SHORT).show()
    }
    private fun loadImageForButton(imageButton: ImageButton, uid: String) {
        databaseRef.child(uid).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val imageUrl = dataSnapshot.child("url").value as String?
                if (imageUrl != null) {
                    Glide.with(this)
                        .load(imageUrl)
                        .centerCrop()
                        .into(imageButton)
                }
            }
        }.addOnFailureListener {
            // Optionally handle failure (e.g., set a default image)
        }
    }

    private fun loadImageIntoImageButton(imageButton: ImageButton, imageUrl: String) {
        Toast.makeText(requireContext(), "Cargando imagen...", Toast.LENGTH_SHORT).show()
        Glide.with(this)
            .load(imageUrl)
            .centerCrop()
            .into(imageButton)
        Toast.makeText(requireContext(), "Imagen cargada", Toast.LENGTH_SHORT).show()
    }


    private val imagePickerResultLauncher =

        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    val selectedView = gridLayout.findViewWithTag<View>(currentSelectedButtonTag)
                    if (selectedView is ImageButton) {
                        uploadImageToFirebaseStorage(uri) { uid, imageUrl ->
                            val name = view?.findViewById<EditText>(R.id.first_input)?.text.toString()
                            val description = view?.findViewById<EditText>(R.id.second_input)?.text.toString()
                            storeImageMetadataToDatabase(
                                selectedView.tag.toString(),
                                imageUrl,
                                name,
                                description
                            )
                        }
                    }
                }
            }
        }


    private fun updateButtonData() {
        Toast.makeText(requireContext(), "Actualizando...", Toast.LENGTH_SHORT).show()
        for (i in 0 until gridLayout.childCount) {
            val childView = gridLayout.getChildAt(i)
            val uidButton = childView.tag as? String ?: continue

            databaseRef.child(uidButton).get().addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val imageUrl = dataSnapshot.child("url").value as String?
                    //load image data into button

                    if (childView is ImageButton && imageUrl != null) {
                        Glide.with(this)
                            .load(imageUrl)
                            .centerCrop()
                            .into(childView)
                    }
                }
            }.addOnFailureListener {
                // Optionally handle failure (e.g., set a default image)
            }
        }
        Toast.makeText(requireContext(), "Actualizado", Toast.LENGTH_SHORT).show()
    }
    private fun submitData(view: View) {
        Toast.makeText(requireContext(), "Guardando...", Toast.LENGTH_SHORT).show()
        val name = view.findViewById<EditText>(R.id.first_input).text.toString()
        val description = view.findViewById<EditText>(R.id.second_input).text.toString()
        val uidButton = currentSelectedButtonTag ?: return

        val buttonRef = databaseRef.child(uidButton)
        Toast.makeText(requireContext(), uidButton, Toast.LENGTH_SHORT).show()
        val data = mapOf(
            "name" to name,
            "Description" to description
        )

        buttonRef.updateChildren(data).addOnSuccessListener {
            Handler(Looper.getMainLooper()).postDelayed({

            updateButtonData()
            }, 3000)  // Delay of 3 seconds
        }.addOnFailureListener {
            // Handle failure
        }
        Toast.makeText(requireContext(), "Guardado", Toast.LENGTH_SHORT).show()
    }
    private fun refreshGridLayout() {
        // Clear and reassign UIDs to buttons
        gridLayout.removeAllViews()
        assignUidsToButtons(requireView())
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
        uidButton: String,
        imageUrl: String,
        name: String,
        description: String
    ) {
        val imageData = mapOf(
            "name" to name,
            "Description" to description,
            "url" to imageUrl
        )

        databaseRef.child(uidButton).setValue(imageData).addOnSuccessListener {
            // Data saved successfully
        }.addOnFailureListener {
            // Handle failure
        }
    }

    private fun promptImageSelection() {
        Toast.makeText(requireContext(), "Selecciona una imagen", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerResultLauncher.launch(intent)
    }
}
