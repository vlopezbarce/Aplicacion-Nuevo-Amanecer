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
import com.tec.nuevoamanecer.databinding.FragmentAlumnocategoriasBinding
import java.util.Locale

class AlumnoCategoriasFragment : Fragment(), TextToSpeech.OnInitListener {
    private var _binding: FragmentAlumnocategoriasBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var categoriasRef : DatabaseReference
    private lateinit var userUID: String

    private lateinit var recyclerViewCategorias: RecyclerView
    private val categoriasList = mutableListOf<Categoria>()
    private lateinit var categoriasAdapter: AlumnoCategoriaAdapter

    private lateinit var recyclerViewTTS: RecyclerView
    private val ttsList = mutableListOf<Imagen>()
    private lateinit var ttsAdapter: TTSAdapter

    private var tts: TextToSpeech? = null
    private var isFeminineVoice: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        userUID = arguments?.getString("userUID").orEmpty()
        categoriasRef = database.child("Usuarios").child("Tablero").child(userUID)
        isFeminineVoice = arguments?.getBoolean("isFeminineVoice") ?: true

        Log.d("isFeminineVoice", isFeminineVoice.toString())

        ttsList.clear()
        ttsList.addAll(arguments?.getParcelableArrayList<Imagen>("ttsList") ?: arrayListOf())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAlumnocategoriasBinding.inflate(inflater, container, false)

        tts = TextToSpeech(requireContext(), this)

        recyclerViewTTS = binding.recyclerViewTTS
        ttsAdapter = TTSAdapter(requireContext(), ttsList)
        recyclerViewTTS.adapter = ttsAdapter
        recyclerViewTTS.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        ttsAdapter.notifyDataSetChanged()

        recyclerViewCategorias = binding.recyclerViewCategorias
        categoriasAdapter = AlumnoCategoriaAdapter(requireContext(), userUID, categoriasList, ttsList, isFeminineVoice)
        recyclerViewCategorias.adapter = categoriasAdapter
        recyclerViewCategorias.layoutManager = GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)

        cargarCategorias()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegresar.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("userUID", userUID)
            Navigation.findNavController(view).navigate(R.id.action_alumnoCategoriasFragment_to_alumnoFragment4, bundle)
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

    private fun cargarCategorias() {
        categoriasRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                categoriasList.clear()

                for (categoriaSnapshot in dataSnapshot.children) {
                    categoriaSnapshot?.let {
                        val key = it.key
                        val categoria = Categoria(key.orEmpty())
                        categoriasList.add(categoria)
                    }
                }

                categoriasAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun updateVoice() {
        Log.d("voice", isFeminineVoice.toString())
        if (isFeminineVoice) {
            Log.d("voice", "cambiando pitch femenino")
            tts?.setPitch(2.0f) // Intenta con diferentes valores
        } else {
            Log.d("voice", "cambiando pitch masculino")
            tts?.setPitch(0.1f) // Intenta con diferentes valores
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("es", "MX"))

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