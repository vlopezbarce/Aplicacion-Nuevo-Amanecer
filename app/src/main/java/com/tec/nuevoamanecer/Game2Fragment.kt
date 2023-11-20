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
import com.tec.nuevoamanecer.databinding.FragmentGame2Binding
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class Game2Fragment : Fragment() {

    private var _binding: FragmentGame2Binding? = null
    private val binding get() = _binding!!

    private lateinit var rotate: RotateAnimation
    private lateinit var viewKonfetti: nl.dionsegijn.konfetti.KonfettiView

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

        val mpIncorrecto = MediaPlayer.create(requireContext(), R.raw.incorrecto)
        val mpCheer = MediaPlayer.create(requireContext(), R.raw.cheer)
        val mpCerdo = MediaPlayer.create(requireContext(), R.raw.cerdo)
        val mpChango = MediaPlayer.create(requireContext(), R.raw.chango)
        val mpTigre = MediaPlayer.create(requireContext(), R.raw.tigre)
        val mpRana = MediaPlayer.create(requireContext(), R.raw.rana)
        val mpVaca = MediaPlayer.create(requireContext(), R.raw.vaca)

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

        chango.setOnClickListener {
            if (animalesZoo.isNotEmpty()) {
                if (animalesZoo[seleccion].id == chango.id) {
                    val temporal = animalesZoo.sliceArray(0 until seleccion) + animalesZoo.sliceArray(
                        seleccion + 1 until animalesZoo.size)
                    val temporalNombres = nombresAnimales.sliceArray(0 until seleccion) + nombresAnimales.sliceArray(
                        seleccion + 1 until animalesZoo.size)

                    animalesZoo = temporal
                    nombresAnimales = temporalNombres

                    stop(chango)
                    mpChango.start()
                    if (animalesZoo.isNotEmpty()) {
                        seleccion = random.nextInt(animalesZoo.size)
                        movimientoImagen(animalesZoo[seleccion])

                        Log.e("Animal anterior", textoAnimal.text.toString())
                        textoAnimal.text = nombresAnimales[seleccion].toString()
                        Log.e("Animal nuevo", textoAnimal.text.toString())
                    }
                    else {
                        textoAnimal.visibility = View.INVISIBLE

                        movimientoImagen(animalesCompletado[0])
                        movimientoImagen(animalesCompletado[1])
                        movimientoImagen(animalesCompletado[2])
                        movimientoImagen(animalesCompletado[3])
                        movimientoImagen(animalesCompletado[4])

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

        rana.setOnClickListener {
            if (animalesZoo.isNotEmpty()) {
                if (animalesZoo[seleccion].id == rana.id) {
                    val temporal = animalesZoo.sliceArray(0 until seleccion) + animalesZoo.sliceArray(
                        seleccion + 1 until animalesZoo.size)
                    val temporalNombres = nombresAnimales.sliceArray(0 until seleccion) + nombresAnimales.sliceArray(
                        seleccion + 1 until animalesZoo.size)

                    animalesZoo = temporal
                    nombresAnimales = temporalNombres

                    stop(rana)
                    mpRana.start()
                    if (animalesZoo.isNotEmpty()) {
                        seleccion = random.nextInt(animalesZoo.size)
                        movimientoImagen(animalesZoo[seleccion])

                        Log.e("Animal anterior", textoAnimal.text.toString())
                        textoAnimal.text = nombresAnimales[seleccion].toString()
                        Log.e("Animal nuevo", textoAnimal.text.toString())
                    }
                    else {
                        textoAnimal.visibility = View.INVISIBLE

                        movimientoImagen(animalesCompletado[0])
                        movimientoImagen(animalesCompletado[1])
                        movimientoImagen(animalesCompletado[2])
                        movimientoImagen(animalesCompletado[3])
                        movimientoImagen(animalesCompletado[4])

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

        vaca.setOnClickListener {
            if (animalesZoo.isNotEmpty()) {
                if (animalesZoo[seleccion].id == vaca.id) {
                    val temporal = animalesZoo.sliceArray(0 until seleccion) + animalesZoo.sliceArray(
                        seleccion + 1 until animalesZoo.size)
                    val temporalNombres = nombresAnimales.sliceArray(0 until seleccion) + nombresAnimales.sliceArray(
                        seleccion + 1 until animalesZoo.size)

                    animalesZoo = temporal
                    nombresAnimales = temporalNombres

                    stop(vaca)
                    mpVaca.start()
                    if (animalesZoo.isNotEmpty()) {
                        seleccion = random.nextInt(animalesZoo.size)
                        movimientoImagen(animalesZoo[seleccion])

                        Log.e("Animal anterior", textoAnimal.text.toString())
                        textoAnimal.text = nombresAnimales[seleccion].toString()
                        Log.e("Animal nuevo", textoAnimal.text.toString())
                    }
                    else {
                        textoAnimal.visibility = View.INVISIBLE

                        movimientoImagen(animalesCompletado[0])
                        movimientoImagen(animalesCompletado[1])
                        movimientoImagen(animalesCompletado[2])
                        movimientoImagen(animalesCompletado[3])
                        movimientoImagen(animalesCompletado[4])

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

        tigre.setOnClickListener {
            if (animalesZoo.isNotEmpty()) {
                if (animalesZoo[seleccion].id == tigre.id) {
                    val temporal = animalesZoo.sliceArray(0 until seleccion) + animalesZoo.sliceArray(
                        seleccion + 1 until animalesZoo.size)
                    val temporalNombres = nombresAnimales.sliceArray(0 until seleccion) + nombresAnimales.sliceArray(
                        seleccion + 1 until animalesZoo.size)

                    animalesZoo = temporal
                    nombresAnimales = temporalNombres

                    stop(tigre)
                    mpTigre.start()
                    if (animalesZoo.isNotEmpty()) {
                        seleccion = random.nextInt(animalesZoo.size)
                        movimientoImagen(animalesZoo[seleccion])

                        Log.e("Animal anterior", textoAnimal.text.toString())
                        textoAnimal.text = nombresAnimales[seleccion].toString()
                        Log.e("Animal nuevo", textoAnimal.text.toString())
                    }
                    else {
                        textoAnimal.visibility = View.INVISIBLE

                        movimientoImagen(animalesCompletado[0])
                        movimientoImagen(animalesCompletado[1])
                        movimientoImagen(animalesCompletado[2])
                        movimientoImagen(animalesCompletado[3])
                        movimientoImagen(animalesCompletado[4])

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

        cerdo.setOnClickListener {
            if (animalesZoo.isNotEmpty()) {
                if (animalesZoo[seleccion].id == cerdo.id) {
                    val temporal = animalesZoo.sliceArray(0 until seleccion) + animalesZoo.sliceArray(
                        seleccion + 1 until animalesZoo.size)
                    val temporalNombres = nombresAnimales.sliceArray(0 until seleccion) + nombresAnimales.sliceArray(
                        seleccion + 1 until animalesZoo.size)

                    animalesZoo = temporal
                    nombresAnimales = temporalNombres

                    stop(cerdo)
                    mpCerdo.start()
                    if (animalesZoo.isNotEmpty()) {
                        seleccion = random.nextInt(animalesZoo.size)
                        movimientoImagen(animalesZoo[seleccion])

                        Log.e("Animal anterior", textoAnimal.text.toString())
                        textoAnimal.text = nombresAnimales[seleccion].toString()
                        Log.e("Animal nuevo", textoAnimal.text.toString())
                    }
                    else {
                        textoAnimal.visibility = View.INVISIBLE

                        movimientoImagen(animalesCompletado[0])
                        movimientoImagen(animalesCompletado[1])
                        movimientoImagen(animalesCompletado[2])
                        movimientoImagen(animalesCompletado[3])
                        movimientoImagen(animalesCompletado[4])

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