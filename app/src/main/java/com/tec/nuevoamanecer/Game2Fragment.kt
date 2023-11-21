package com.tec.nuevoamanecer

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tec.nuevoamanecer.databinding.FragmentGame2Binding
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class Game2Fragment : Fragment() {

    private var _binding: FragmentGame2Binding? = null
    private val binding get() = _binding!!

    private lateinit var rotate: RotateAnimation
    private lateinit var viewKonfetti: nl.dionsegijn.konfetti.KonfettiView
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
        _binding = FragmentGame2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewKonfetti = binding.viewKonfetti
        var textoAnimal = binding.textAnimal

        val mpIncorrecto = MediaPlayer.create(requireContext(), R.raw.incorrecto)
        val mpCheer = MediaPlayer.create(requireContext(), R.raw.cheer)

        data class Animal(
            val nombre: String,
            val animal: ImageButton,
            val sonido: Int
        )

        var animales = mutableListOf<Animal>(
            Animal("Chango", binding.Chango, R.raw.chango),
            Animal("Rana", binding.Rana, R.raw.rana),
            Animal("Vaca", binding.Vaca, R.raw.vaca),
            Animal("Tigre", binding.Tigre, R.raw.tigre),
            Animal("Cerdo", binding.Cerdo, R.raw.cerdo)
        )

        val random = java.util.Random()
        var seleccion = random.nextInt(animales.size)
        movimientoImagen(animales[seleccion].animal)
        textoAnimal.text = animales[seleccion].nombre

        animales.forEach { (_, animal, sonido) ->
            animal.setOnClickListener {
                if (animales.isNotEmpty()) {
                    if (animales[seleccion].animal.id == animal.id) {
                        animales.removeAt(seleccion)
                        stop(animal)
                        MediaPlayer.create(requireContext(), sonido).start()

                        if (animales.isNotEmpty()) {
                            seleccion = random.nextInt(animales.size)
                            movimientoImagen(animales[seleccion].animal)

                            Log.d("Animal anterior", textoAnimal.text.toString())
                            Log.d("Animal nuevo", animales[seleccion].nombre)
                            textoAnimal.text = animales[seleccion].nombre
                        } else {
                            textoAnimal.visibility = View.INVISIBLE

                            animales.forEach { (_, animal, _) ->
                                movimientoImagen(animal)
                            }

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
                    } else {
                        mpIncorrecto.start()
                    }
                }
            }
        }

        binding.btnRegresar.setOnClickListener{
            when (nivel) {
                1 -> Navigation.findNavController(binding.root)
                    .navigate(R.id.action_game2Fragment_to_alumnoFragment)
                2 -> Navigation.findNavController(binding.root)
                    .navigate(R.id.action_game2Fragment_to_alumnoFragment2)
                3 -> Navigation.findNavController(binding.root)
                    .navigate(R.id.action_game2Fragment_to_alumnoFragment3)
                4 -> Navigation.findNavController(binding.root)
                    .navigate(R.id.action_game2Fragment_to_alumnoFragment4)
            }
        }
    }

    private fun movimientoImagen(view: View) {
        rotate = RotateAnimation(-10f, 10f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)

        rotate.duration = 2000
        rotate.repeatCount = Animation.INFINITE
        rotate.repeatMode = Animation.REVERSE

        view.startAnimation(rotate)
    }

    private fun stop(view: View) {
        rotate = RotateAnimation(0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)

        view.startAnimation(rotate)
    }
}