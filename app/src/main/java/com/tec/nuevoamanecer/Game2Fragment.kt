package com.tec.nuevoamanecer

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
import com.tec.nuevoamanecer.databinding.FragmentGame2Binding

class Game2Fragment : Fragment() {

    private var _binding: FragmentGame2Binding? = null
    private val binding get() = _binding!!

    private lateinit var rotate: RotateAnimation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGame2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mpCorrecto = MediaPlayer.create(requireContext(), R.raw.correcto)
        val mpIncorrecto = MediaPlayer.create(requireContext(), R.raw.incorrecto)
        var textoAnimal = binding.textAnimal
        val chango = binding.Chango
        val rana = binding.Rana
        val vaca = binding.Vaca
        val tigre = binding.Tigre
        val cerdo = binding.Cerdo

        var animalesZoo = arrayOf(chango, rana, vaca, tigre, cerdo)
        var nombresAnimales = arrayOf("Chango", "Rana", "Vaca", "Tigre", "Cerdo")
        val animalesCompletado = arrayOf(chango, rana, vaca, tigre, cerdo)

        val random = java.util.Random()
        var seleccion = random.nextInt(animalesZoo.size)

        movimientoImagen(animalesZoo[seleccion])
        textoAnimal.text = nombresAnimales[seleccion]

        fun onAnimalButtonClick(button: ImageButton) {
            if (animalesZoo.isNotEmpty()) {
                if (animalesZoo[seleccion].id == button.id) {
                    val temporal = animalesZoo.sliceArray(0 until seleccion) + animalesZoo.sliceArray(
                        seleccion + 1 until animalesZoo.size
                    )
                    val temporalNombres =
                        nombresAnimales.sliceArray(0 until seleccion) + nombresAnimales.sliceArray(
                            seleccion + 1 until animalesZoo.size
                        )

                    animalesZoo = temporal
                    nombresAnimales = temporalNombres

                    stop(button)
                    mpCorrecto.start()
                    if (animalesZoo.isNotEmpty()) {
                        seleccion = random.nextInt(animalesZoo.size)
                        movimientoImagen(animalesZoo[seleccion])

                        Log.e("Animal anterior", textoAnimal.text.toString())
                        textoAnimal.text = nombresAnimales[seleccion].toString()
                        Log.e("Animal nuevo", textoAnimal.text.toString())
                    } else {
                        textoAnimal.visibility = View.INVISIBLE

                        for (completedAnimal in animalesCompletado) {
                            movimientoImagen(completedAnimal)
                        }
                    }

                } else {
                    mpIncorrecto.start()
                }
            }
        }

        chango.setOnClickListener { onAnimalButtonClick(chango) }

        rana.setOnClickListener { onAnimalButtonClick(rana) }

        vaca.setOnClickListener { onAnimalButtonClick(vaca) }

        tigre.setOnClickListener { onAnimalButtonClick(tigre) }

        cerdo.setOnClickListener { onAnimalButtonClick(cerdo) }

        binding.btnRegresar.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_game2Fragment_to_alumnoFragment)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}