package com.tec.nuevoamanecer

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tec.nuevoamanecer.databinding.FragmentAlumnotableroBinding
import java.util.Locale

class AlumnoTableroFragment : Fragment(), TextToSpeech.OnInitListener {
    private var _binding: FragmentAlumnotableroBinding? = null

    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var storageRef : StorageReference
    private lateinit var imagenesRef : DatabaseReference
    private lateinit var userUID: String

    private lateinit var categoria: String
    private lateinit var recyclerViewImagenes: RecyclerView
    private val imagenesList = mutableListOf<Imagen>()
    private lateinit var imagenAdapter: ImagenAdapter

    private lateinit var recyclerViewTTS: RecyclerView
    private val ttsList = mutableListOf<Imagen>()
    private lateinit var ttsAdapter: TTSAdapter

    private var tts: TextToSpeech? = null
    private var isFeminineVoice: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        storageRef = FirebaseStorage.getInstance().reference
        userUID = arguments?.getString("userUID").orEmpty()
        categoria = arguments?.getString("categoria").orEmpty()
        imagenesRef = database.child("Usuarios").child("Tablero").child(userUID).child(categoria)
        isFeminineVoice = arguments?.getBoolean("isFeminineVoice") ?: true

        ttsList.clear()
        ttsList.addAll(arguments?.getParcelableArrayList<Imagen>("ttsList") ?: arrayListOf())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlumnotableroBinding.inflate(inflater, container, false)

        tts = TextToSpeech(requireContext(), this)

        recyclerViewTTS = binding.recyclerViewTTS
        ttsAdapter = TTSAdapter(requireContext(), ttsList)
        recyclerViewTTS.adapter = ttsAdapter
        recyclerViewTTS.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        ttsAdapter.notifyDataSetChanged()

        recyclerViewImagenes = binding.recyclerViewImagenes
        imagenAdapter = ImagenAdapter(requireContext(), imagenesList, ttsList, ttsAdapter)
        recyclerViewImagenes.adapter = imagenAdapter
        recyclerViewImagenes.layoutManager = GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)

        cargarImagenes()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegresar.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("userUID", userUID)
            bundle.putParcelableArrayList("ttsList", ArrayList(ttsList))
            bundle.putBoolean("isFeminineVoice", isFeminineVoice)
            Navigation.findNavController(view).navigate(R.id.action_alumnoTableroFragment_to_alumnoCategoriasFragment, bundle)
        }

        binding.btnBackspace.setOnClickListener {
            if (ttsList.isNotEmpty()) {
                ttsList.removeAt(ttsList.size - 1)
                ttsAdapter.notifyItemRemoved(ttsList.size)
            }
        }

        binding.btnBorrar.setOnClickListener {
            ttsList.clear()
            ttsAdapter.notifyDataSetChanged()
        }

        binding.btnSpeak.setOnClickListener {
            val textToSpeak = StringBuilder()
            ttsList.forEach { imagen ->
                textToSpeak.append("${imagen.nombre} ")
            }

            val text = textToSpeak.toString()
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        }

        binding.btnSwitch.setOnClickListener {
            isFeminineVoice = !isFeminineVoice
            updateVoice()
        }
    }

    private fun cargarImagenes() {
        val imagenesRef = database.child("Usuarios").child("Tablero").child(userUID).child(categoria)

        imagenesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                imagenesList.clear()

                for (imagenSnapshot in dataSnapshot.children) {
                    imagenSnapshot?.let {
                        val id = it.child("id").getValue(String::class.java) ?: ""
                        val nombre = it.child("nombre").getValue(String::class.java) ?: ""
                        val path = it.child("url").getValue(String::class.java) ?: ""

                        if (it.key != "categoria") {
                            val imagen = Imagen(id, nombre, path)
                            imagenesList.add(imagen)
                        }
                    }
                }

                imagenAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun updateVoice() {
        if (isFeminineVoice) {
            tts?.setPitch(1.0f)
        } else {
            tts?.setPitch(0.4f)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale("es", "MX"))

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "El idioma especificado no es compatible")
            } else {
                updateVoice()
            }
        } else {
            Log.e("TTS", "Error de inicialización")
        }
    }
}