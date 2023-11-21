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
import android.widget.ImageView
import android.widget.TextView
import com.tec.nuevoamanecer.databinding.FragmentGame6Binding


class Game6Fragment : Fragment() {

    private var _binding : FragmentGame6Binding? = null
    private val binding get() = _binding!!

    private lateinit var progressBar: ProgressBar
    private lateinit var viewKonfetti: nl.dionsegijn.konfetti.KonfettiView
    private var conta = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGame6Binding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar
        viewKonfetti = binding.viewKonfetti

        val cerdo = binding.cerdo
        val bebeCerdo = binding.bebeCerdo

        val chango = binding.chango
        val bebeChango = binding.bebeChango

        val tigre = binding.tigre
        val bebeTigre = binding.bebeTigre

        val rana = binding.rana
        val bebeRana = binding.bebeRana

        val vaca = binding.vaca
        val bebeVaca = binding.bebeVaca


        val contador = binding.textContador

        val mpCerdo = MediaPlayer.create(requireContext(), R.raw.cerdo)
        val mpChango = MediaPlayer.create(requireContext(), R.raw.chango)
        val mpTigre = MediaPlayer.create(requireContext(), R.raw.tigre)
        val mpRana = MediaPlayer.create(requireContext(), R.raw.rana)
        val mpVaca = MediaPlayer.create(requireContext(), R.raw.vaca)
        val mpCheer = MediaPlayer.create(requireContext(), R.raw.cheer)
        val mpInstrucicones = MediaPlayer.create(requireContext(), R.raw.instrucciones2)
        mpInstrucicones.start()

        bebeCerdo.setOnLongClickListener { view ->
            val dragData = View.DragShadowBuilder(view)
            view.startDragAndDrop(null, dragData, view, 0) // Set the local state to the view being dragged
        }

        cerdo.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    if (event.localState == bebeCerdo) {
                        mpCerdo.start()

                        val temp = contador.text.toString().toInt()
                        contador.text = (temp + 1).toString()
                        bebeCerdo.visibility = View.GONE

                        cerdo.setImageResource(R.drawable.cerdofamilia)

                        if (conta < 100) {
                            conta += 20
                            if (conta == 100){
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

        bebeChango.setOnLongClickListener { view ->
            val dragData = View.DragShadowBuilder(view)
            view.startDragAndDrop(null, dragData, view, 0) // Set the local state to the view being dragged
        }

        chango.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    if (event.localState == bebeChango) {
                        mpChango.start()

                        val temp = contador.text.toString().toInt()
                        contador.text = (temp + 1).toString()
                        bebeChango.visibility = View.GONE

                        chango.setImageResource(R.drawable.changofamilia)

                        if (conta < 100) {
                            conta += 20
                            if (conta == 100){
                                mpCheer.start()
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
                            progressBar.progress = conta
                        }
                    }
                }
            }
            true
        }

        bebeTigre.setOnLongClickListener { view ->
            val dragData = View.DragShadowBuilder(view)
            view.startDragAndDrop(null, dragData, view, 0) // Set the local state to the view being dragged
        }

        tigre.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    if (event.localState == bebeTigre) {
                        mpTigre.start()

                        val temp = contador.text.toString().toInt()
                        contador.text = (temp + 1).toString()
                        bebeTigre.visibility = View.GONE

                        tigre.setImageResource(R.drawable.tigrefamilia)

                        if (conta < 100) {
                            conta += 20
                            if (conta == 100){
                                mpCheer.start()
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
                            progressBar.progress = conta
                        }
                    }
                }
            }
            true
        }

        bebeRana.setOnLongClickListener { view ->
            val dragData = View.DragShadowBuilder(view)
            view.startDragAndDrop(null, dragData, view, 0) // Set the local state to the view being dragged
        }

        rana.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    if (event.localState == bebeRana) {
                        mpRana.start()

                        val temp = contador.text.toString().toInt()
                        contador.text = (temp + 1).toString()
                        bebeRana.visibility = View.GONE

                        rana.setImageResource(R.drawable.ranafamilia)

                        if (conta < 100) {
                            conta += 20
                            if (conta == 100){
                                mpCheer.start()
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
                            progressBar.progress = conta
                        }
                    }
                }
            }
            true
        }

        bebeVaca.setOnLongClickListener { view ->
            val dragData = View.DragShadowBuilder(view)
            view.startDragAndDrop(null, dragData, view, 0) // Set the local state to the view being dragged
        }

        vaca.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    if (event.localState == bebeVaca) {
                        mpVaca.start()

                        val temp = contador.text.toString().toInt()
                        contador.text = (temp + 1).toString()
                        bebeVaca.visibility = View.GONE

                        vaca.setImageResource(R.drawable.vacafamilia)

                        if (conta < 100) {
                            conta += 20
                            if (conta == 100){
                                mpCheer.start()
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
                            progressBar.progress = conta
                        }
                    }
                }
            }
            true
        }

        binding.btnRegresar.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_game6Fragment_to_alumnoFragment3)
        }
    }
}