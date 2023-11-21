package com.tec.nuevoamanecer

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import android.graphics.Color
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import android.view.DragEvent
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tec.nuevoamanecer.databinding.FragmentGame4Binding

class Game4Fragment : Fragment() {

    private var _binding : FragmentGame4Binding? = null
    private val binding get() = _binding!!

    private lateinit var progressBar: ProgressBar
    private lateinit var viewKonfetti: nl.dionsegijn.konfetti.KonfettiView
    private lateinit var contador: TextView
    private var conta = 0
    private var nivel: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = FirebaseDatabase.getInstance().reference
        val auth = FirebaseAuth.getInstance()
        val userUID = auth.currentUser?.uid.orEmpty()
        val nivelRef = database.child("Usuarios").child("Alumnos").child(userUID).child("nivel")

        nivelRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nivelValue = snapshot.getValue(String::class.java)?.toInt()
                nivel = nivelValue
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGame4Binding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar
        viewKonfetti = binding.viewKonfetti
        contador = binding.textContador

        val mpCorrecto = MediaPlayer.create(requireContext(), R.raw.correcto)
        val mpCheer = MediaPlayer.create(requireContext(), R.raw.cheer)

        data class Figura(
            val color: ImageView,
            val sombra: ImageView,
            val imagenCompleta: Int
        )

        val figuras = listOf(
            Figura(binding.trianguloRojo, binding.triangulo, R.drawable.triangulorojo),
            Figura(binding.cuadradoVerde, binding.cuadrado, R.drawable.cuadradoverde),
            Figura(binding.circuloAzul, binding.circulo, R.drawable.circuloazul)
        )

        figuras.forEach { (color, sombra, imagenCompleta) ->
            color.setOnLongClickListener { view ->
                val dragData = View.DragShadowBuilder(view)
                view.startDragAndDrop(null, dragData, view, 0)
            }

            sombra.setOnDragListener { _, event ->
                when (event.action) {
                    DragEvent.ACTION_DROP -> {
                        if (event.localState == color) {
                            mpCorrecto.start()

                            sombra.setImageResource(imagenCompleta)

                            val temp = contador.text.toString().toInt()
                            contador.text = (temp + 1).toString()
                            color.visibility = View.GONE
                            if (conta < 99) {
                                conta += 33
                                if (conta == 99) {
                                    conta = 100
                                    mpCheer.start()
                                    viewKonfetti.build()
                                        .addColors(Color.YELLOW, Color.BLUE, Color.GREEN, Color.MAGENTA)
                                        .setDirection(0.0, 359.0)
                                        .setSpeed(1f, 5f)
                                        .setFadeOutEnabled(true)
                                        .setTimeToLive(2000L)
                                        .addShapes(Shape.Square, Shape.Circle)
                                        .addSizes(Size(12))
                                        .setPosition(-50f, viewKonfetti.width + 50f, -50f, viewKonfetti.height + 50f)
                                        .streamFor(300, 3000L)
                                }
                                progressBar.progress = conta
                            }
                        }
                    }
                }
                true
            }
        }

        binding.btnRegresar.setOnClickListener{
            when (nivel) {
                2 -> Navigation.findNavController(binding.root)
                    .navigate(R.id.action_game4Fragment_to_alumnoFragment2)
                3 -> Navigation.findNavController(binding.root)
                    .navigate(R.id.action_game4Fragment_to_alumnoFragment3)
                4 -> Navigation.findNavController(binding.root)
                    .navigate(R.id.action_game4Fragment_to_alumnoFragment4)
            }
        }
    }
}