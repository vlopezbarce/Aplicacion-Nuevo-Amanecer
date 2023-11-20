package com.tec.nuevoamanecer

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import kotlin.random.Random
import androidx.navigation.Navigation
import android.graphics.Color
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import android.view.DragEvent
import com.tec.nuevoamanecer.databinding.FragmentGame4Binding


class Game4Fragment : Fragment() {

    private var _binding : FragmentGame4Binding? = null
    private val binding get() = _binding!!

    private lateinit var progressBar: ProgressBar
    private lateinit var viewKonfetti: nl.dionsegijn.konfetti.KonfettiView
    private var conta = 0

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

        val trianguloRojo = binding.trianguloRojo
        val triangulo = binding.triangulo

        val cuadradoVerde = binding.cuadradoVerde
        val cuadrado = binding.cuadrado

        val circuloAzul = binding.circuloAzul
        val circulo = binding.circulo

        val contador = binding.textContador

        //val random = java.util.Random()

        val mpCorrecto = MediaPlayer.create(requireContext(), R.raw.correcto)
        val mpCheer = MediaPlayer.create(requireContext(), R.raw.cheer)

        trianguloRojo.setOnLongClickListener { view ->
            val dragData = View.DragShadowBuilder(view)
            view.startDragAndDrop(null, dragData, view, 0)
        }

        triangulo.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    if (event.localState == trianguloRojo) {
                        mpCorrecto.start()

                        triangulo.setImageResource(R.drawable.triangulorojo)

                        val temp = contador.text.toString().toInt()
                        contador.text = (temp + 1).toString()
                        trianguloRojo.visibility = View.GONE
                        if (conta < 99) {
                            conta += 33
                            if (conta == 99){
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

        cuadradoVerde.setOnLongClickListener { view ->
            val dragData = View.DragShadowBuilder(view)
            view.startDragAndDrop(null, dragData, view, 0) // Set the local state to the view being dragged
        }

        cuadrado.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    if (event.localState == cuadradoVerde) {
                        mpCorrecto.start()

                        cuadrado.setImageResource(R.drawable.cuadradoverde)

                        val temp = contador.text.toString().toInt()
                        contador.text = (temp + 1).toString()
                        cuadradoVerde.visibility = View.GONE
                        if (conta < 99) {
                            conta += 33
                            if (conta == 99){
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

        circuloAzul.setOnLongClickListener { view ->
            val dragData = View.DragShadowBuilder(view)
            view.startDragAndDrop(null, dragData, view, 0) // Set the local state to the view being dragged
        }

        circulo.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    if (event.localState == circuloAzul) {
                        mpCorrecto.start()

                        circulo.setImageResource(R.drawable.circuloazul)

                        val temp = contador.text.toString().toInt()
                        contador.text = (temp + 1).toString()
                        circuloAzul.visibility = View.GONE
                        if (conta < 99) {
                            conta += 33
                            if (conta == 99){
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

        binding.btnRegresar.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_game4Fragment_to_alumnoFragment2)
        }
    }
}