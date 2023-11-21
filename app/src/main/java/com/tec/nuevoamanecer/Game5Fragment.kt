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
import android.widget.ImageButton
import android.widget.TextView
import com.tec.nuevoamanecer.databinding.FragmentGame5Binding


class Game5Fragment : Fragment() {

    private var _binding : FragmentGame5Binding? = null
    private val binding get() = _binding!!

    private lateinit var progressBar: ProgressBar
    private lateinit var viewKonfetti: nl.dionsegijn.konfetti.KonfettiView
    private var conta = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGame5Binding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar
        viewKonfetti = binding.viewKonfetti

        val manzana = binding.manzana
        manzana.visibility = View.INVISIBLE
        val canastamanzana = binding.canastamanzana

        val naranja = binding.naranja
        naranja.visibility = View.INVISIBLE
        val canastanaranja = binding.canastanaranja

        val platano = binding.platano
        platano.visibility = View.INVISIBLE
        val canastaplatano = binding.canastaplatano

        val uva = binding.uva
        uva.visibility = View.INVISIBLE
        val canastauva = binding.canastauva

        val contador = binding.textContador

        val random = java.util.Random()

        val indice = 0
        val opciones = arrayOf(manzana, naranja, platano, uva)

        val seleccion = random.nextInt(5)
        opciones[seleccion].visibility = View.VISIBLE

        val mpCorrecto = MediaPlayer.create(requireContext(), R.raw.correcto)
        val mpCheer = MediaPlayer.create(requireContext(), R.raw.cheer)
        val mpInstrucicones = MediaPlayer.create(requireContext(), R.raw.instrucciones)
        mpInstrucicones.start()

        manzana.setOnLongClickListener { view ->
            val dragData = View.DragShadowBuilder(view)
            view.startDragAndDrop(null, dragData, view, 0)
        }

        canastamanzana.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    if (event.localState == manzana) {
                        mpCorrecto.start()

                        val temp = contador.text.toString().toInt()
                        contador.text = (temp + 1).toString()
                        manzana.visibility = View.INVISIBLE
                        if (conta < 100) {
                            conta += 10
                            progressBar.progress = conta
                            val seleccion = random.nextInt(3)
                            opciones[seleccion].visibility = View.VISIBLE
                            if (conta == 100){
                                mpCheer.start()
                                opciones[seleccion].visibility = View.INVISIBLE
                                viewKonfetti.build()
                                    .addColors(Color.YELLOW, Color.BLUE,Color.GREEN,Color.MAGENTA)
                                    .setDirection(0.0, 359.0)
                                    .setSpeed(1f, 5f)
                                    .setFadeOutEnabled(true)
                                    .setTimeToLive(2000L)
                                    .addShapes(Shape.Square, Shape.Circle)
                                    .addSizes(Size(12))
                                    .setPosition(-50f, viewKonfetti.width + 50f, -50f, viewKonfetti.height + 50f)
                                    .streamFor(300, 3000L)
                            }
                        }

                    }
                }
            }
            true
        }

        naranja.setOnLongClickListener { view ->
            val dragData = View.DragShadowBuilder(view)
            view.startDragAndDrop(null, dragData, view, 0)
        }

        canastanaranja.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    if (event.localState == naranja) {
                        mpCorrecto.start()

                        val temp = contador.text.toString().toInt()
                        contador.text = (temp + 1).toString()
                        naranja.visibility = View.INVISIBLE
                        if (conta < 100) {
                            conta += 10
                            progressBar.progress = conta
                            val seleccion = random.nextInt(3)
                            opciones[seleccion].visibility = View.VISIBLE
                            if (conta == 100){
                                mpCheer.start()
                                opciones[seleccion].visibility = View.INVISIBLE
                                viewKonfetti.build()
                                    .addColors(Color.YELLOW, Color.BLUE,Color.GREEN,Color.MAGENTA)
                                    .setDirection(0.0, 359.0)
                                    .setSpeed(1f, 5f)
                                    .setFadeOutEnabled(true)
                                    .setTimeToLive(2000L)
                                    .addShapes(Shape.Square, Shape.Circle)
                                    .addSizes(Size(12))
                                    .setPosition(-50f, viewKonfetti.width + 50f, -50f, viewKonfetti.height + 50f)
                                    .streamFor(300, 3000L)
                            }
                        }

                    }
                }
            }
            true
        }

        platano.setOnLongClickListener { view ->
            val dragData = View.DragShadowBuilder(view)
            view.startDragAndDrop(null, dragData, view, 0)
        }

        canastaplatano.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    if (event.localState == platano) {
                        mpCorrecto.start()

                        val temp = contador.text.toString().toInt()
                        contador.text = (temp + 1).toString()
                        platano.visibility = View.INVISIBLE
                        if (conta < 100) {
                            conta += 10
                            progressBar.progress = conta
                            val seleccion = random.nextInt(3)
                            opciones[seleccion].visibility = View.VISIBLE
                            if (conta == 100){
                                mpCheer.start()
                                opciones[seleccion].visibility = View.INVISIBLE
                                viewKonfetti.build()
                                    .addColors(Color.YELLOW, Color.BLUE,Color.GREEN,Color.MAGENTA)
                                    .setDirection(0.0, 359.0)
                                    .setSpeed(1f, 5f)
                                    .setFadeOutEnabled(true)
                                    .setTimeToLive(2000L)
                                    .addShapes(Shape.Square, Shape.Circle)
                                    .addSizes(Size(12))
                                    .setPosition(-50f, viewKonfetti.width + 50f, -50f, viewKonfetti.height + 50f)
                                    .streamFor(300, 3000L)
                            }
                        }

                    }
                }
            }
            true
        }

        uva.setOnLongClickListener { view ->
            val dragData = View.DragShadowBuilder(view)
            view.startDragAndDrop(null, dragData, view, 0)
        }

        canastauva.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    if (event.localState == uva) {
                        mpCorrecto.start()

                        val temp = contador.text.toString().toInt()
                        contador.text = (temp + 1).toString()
                        uva.visibility = View.INVISIBLE
                        if (conta < 100) {
                            conta += 10
                            progressBar.progress = conta
                            val seleccion = random.nextInt(3)
                            opciones[seleccion].visibility = View.VISIBLE
                            if (conta == 100){
                                mpCheer.start()
                                opciones[seleccion].visibility = View.INVISIBLE
                                viewKonfetti.build()
                                    .addColors(Color.YELLOW, Color.BLUE,Color.GREEN,Color.MAGENTA)
                                    .setDirection(0.0, 359.0)
                                    .setSpeed(1f, 5f)
                                    .setFadeOutEnabled(true)
                                    .setTimeToLive(2000L)
                                    .addShapes(Shape.Square, Shape.Circle)
                                    .addSizes(Size(12))
                                    .setPosition(-50f, viewKonfetti.width + 50f, -50f, viewKonfetti.height + 50f)
                                    .streamFor(300, 3000L)
                            }
                        }

                    }
                }
            }
            true
        }

        binding.btnRegresar.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_game5Fragment_to_alumnoFragment3)
        }
    }
}