package com.tec.nuevoamanecer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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

class TableroFragment : Fragment() {
    private lateinit var uidAlumno: String
    private lateinit var gridLayout: GridLayout
    private var currentSelectedButtonTag: String? = null
    private var currentCategory: String = "Familia"

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

        // ... existing code ...

        val descriptionSpinner = view.findViewById<Spinner>(R.id.description_spinner)
        val categories = arrayOf("Familia", "Horario", "Escuela", "Dias de La semana", "Colores", "Clima", "Animales")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        descriptionSpinner.adapter = adapter
        gridLayout = view.findViewById(R.id.button_grid)
        assignUidsToButtons(view)

        descriptionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                currentCategory = parent.getItemAtPosition(position).toString()
                filterButtonsByCategory(currentCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val submitButton = view.findViewById<ImageButton>(R.id.btnSiguiente)
        submitButton.setOnClickListener {
            // Pass the currentCategory to the submitData function to maintain the category
            submitData(view, currentCategory)
        }

        return view
    }
    private fun filterButtonsByCategory(category: String) {
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear existing tags
                for (i in 0 until gridLayout.childCount) {
                    val childView = gridLayout.getChildAt(i)
                    if (childView is ImageButton) {
                        childView.tag = null
                        childView.setImageResource(R.drawable.placeholder_image) // Default image
                    }
                }

                // Filter and assign new UIDs
                var emptyButtonIndex = 0
                dataSnapshot.children.forEach { childSnapshot ->
                    if (childSnapshot.child("Description").value as String? == category) {
                        val imageUrl = childSnapshot.child("url").value as String?
                        val uid = childSnapshot.key ?: return@forEach

                        if (emptyButtonIndex < gridLayout.childCount) {
                            val childView = gridLayout.getChildAt(emptyButtonIndex++) as? ImageButton
                            childView?.tag = uid
                            if (imageUrl != null && childView != null) {
                                Glide.with(this@TableroFragment).load(imageUrl).centerCrop().into(childView)
                            }
                        }
                    }
                }

                // Assign new UIDs to remaining empty buttons
                for (i in emptyButtonIndex until gridLayout.childCount) {
                    val childView = gridLayout.getChildAt(i) as? ImageButton
                    childView?.tag = UUID.randomUUID().toString()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun assignUidsToButtons(view: View) {
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
                    childView.setOnClickListener {
                        currentSelectedButtonTag = uid
                        editButtonData(childView, uid)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun editButtonData(view: View, uidButton: String) {
        val selectedCategory = requireView().findViewById<Spinner>(R.id.description_spinner).selectedItem.toString()

        databaseRef.child(uidButton).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val description = dataSnapshot.child("Description").value as String?
                val name = dataSnapshot.child("name").value as String?

                // Set the text fields with existing data if it matches the selected category
                requireView().findViewById<EditText>(R.id.first_input)?.setText(name)
                if (description == selectedCategory) {
                    // If the button already has data for this category
                    val descriptionSpinner = requireView().findViewById<Spinner>(R.id.description_spinner)
                    val spinnerPosition = (descriptionSpinner.adapter as ArrayAdapter<String>).getPosition(currentCategory)
                    descriptionSpinner.setSelection(spinnerPosition)
                } else {
                    // Clear the fields if the existing data does not match the selected category
                    requireView().findViewById<EditText>(R.id.first_input)?.setText("")
                    val descriptionSpinner = requireView().findViewById<Spinner>(R.id.description_spinner)
                    descriptionSpinner.setSelection(0) // Default to the first item in the spinner
                }
            } else {
                // If the button is empty, prepare it for new data in the selected category
                requireView().findViewById<EditText>(R.id.first_input)?.setText("")
                val descriptionSpinner = requireView().findViewById<Spinner>(R.id.description_spinner)
                val spinnerPosition = (descriptionSpinner.adapter as ArrayAdapter<String>).getPosition(selectedCategory)
                descriptionSpinner.setSelection(spinnerPosition)
            }
        }.addOnFailureListener {
            // Handle error
            Toast.makeText(requireContext(), "Error loading data", Toast.LENGTH_SHORT).show()
        }

        // Always allow image selection and updating
        promptImageSelection()
    }

    private fun checkButtonData(view: View, uidButton: String) {
        databaseRef.child(uidButton).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val description = dataSnapshot.child("Description").value as String?
                val name = dataSnapshot.child("name").value as String?
                val imageUrl = dataSnapshot.child("url").value as String?

                requireView().findViewById<EditText>(R.id.first_input)?.setText(name)
                requireView().findViewById<EditText>(R.id.description_spinner)?.setText(description)

                if (view is ImageButton && imageUrl != null) {
                    loadImageIntoImageButton(view, imageUrl)
                }

                // Allow image editing when button is clicked
                view.setOnClickListener {
                    currentSelectedButtonTag = uidButton
                    promptImageSelection()
                }
            }
        }.addOnFailureListener {
            // Handle error
        }
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
        Glide.with(this)
            .load(imageUrl)
            .centerCrop()
            .into(imageButton)
    }

    private val imagePickerResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                val selectedView = gridLayout.findViewWithTag<View>(currentSelectedButtonTag)
                if (selectedView is ImageButton) {
                    uploadImageToFirebaseStorage(uri) { uid, imageUrl ->
                        val name = requireView().findViewById<EditText>(R.id.first_input)?.text.toString()
                        val descriptionSpinner = requireView().findViewById<Spinner>(R.id.description_spinner)
                        val description = descriptionSpinner.selectedItem.toString()
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

    private fun submitData(view: View, category: String) {
        val name = view.findViewById<EditText>(R.id.first_input).text.toString()
        val uidButton = currentSelectedButtonTag ?: return

        val buttonRef = databaseRef.child(uidButton)
        val data = mapOf(
            "name" to name,
            "Description" to category  // Use the selected category here
        )

        buttonRef.updateChildren(data).addOnSuccessListener {
            Handler(Looper.getMainLooper()).postDelayed({
                val selectedCategory = view.findViewById<Spinner>(R.id.description_spinner).selectedItem.toString()
                filterButtonsByCategory(selectedCategory)  // Refresh the buttons based on the selected category
            }, 4000) // 4 seconds delay

        }.addOnFailureListener {
            // Handle failure
            Toast.makeText(requireContext(), "Error al guardar los datos", Toast.LENGTH_SHORT).show()
        }
    }


    private fun refreshLayout() {
        // Reload the button data
        for (i in 0 until gridLayout.childCount) {
            val childView = gridLayout.getChildAt(i)
            if (childView.tag != null) {
                val uidButton = childView.tag.toString()
                if (childView is ImageButton) {
                    loadImageForButton(childView, uidButton)
                }
                // If needed, update text or other properties for other types of views
            }
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
                    Log.d("UploadImage", "Image uploaded successfully: $imageUrl")
                }.addOnFailureListener { exception ->
                    Log.e("UploadImage", "Error getting download URL", exception)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("UploadImage", "Error uploading image", exception)
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
            Toast.makeText(requireContext(), "Image Data saved", Toast.LENGTH_SHORT).show()
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
