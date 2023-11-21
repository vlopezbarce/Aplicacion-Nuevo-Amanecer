package com.tec.nuevoamanecer

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import android.graphics.Color
import com.tec.nuevoamanecer.databinding.FragmentGame3Binding
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import android.view.DragEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Game3Fragment : Fragment() {

    private var _binding : FragmentGame3Binding? = null
    private val binding get() = _binding!!

    private lateinit var progressBar: ProgressBar
    private lateinit var viewKonfetti: nl.dionsegijn.konfetti.KonfettiView
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
        _binding = FragmentGame3Binding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar
        viewKonfetti = binding.viewKonfetti

        val abeja = binding.abeja
        val contador = binding.textContador
        val panal = binding.panal

        val random = java.util.Random()

        val mp = MediaPlayer.create(requireContext(), R.raw.correcto)
        val mpCheer = MediaPlayer.create(requireContext(), R.raw.cheer)

        abeja.setOnLongClickListener { view ->
            val dragData = View.DragShadowBuilder(view)
            view.startDragAndDrop(null, dragData, null, 0)
        }

        panal.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    mp.start()

                    val screenWidth = resources.displayMetrics.widthPixels

                    val maxWidth = screenWidth / 4
                    val newMarginStart = screenWidth - abeja.width - random.nextInt(maxWidth) - 100

                    val maxHeight = 950
                    val newMarginTop = random.nextInt(maxHeight)

                    val params = abeja.layoutParams as ConstraintLayout.LayoutParams
                    params.marginStart = newMarginStart
                    params.topMargin = newMarginTop
                    abeja.layoutParams = params

                    val temp = contador.text.toString().toInt()
                    contador.text = (temp + 1).toString()

                    if (conta < 90) {
                        conta += 10
                        progressBar.progress = conta
                    }

                    else {
                        contador.text = "10"
                        conta += 10
                        progressBar.progress = conta

                        abeja.visibility = View.INVISIBLE

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
                }
            }
            true
        }

        binding.btnRegresar.setOnClickListener{
            when (nivel) {
                2 -> Navigation.findNavController(binding.root)
                    .navigate(R.id.action_game3Fragment_to_alumnoFragment2)
                3 -> Navigation.findNavController(binding.root)
                    .navigate(R.id.action_game3Fragment_to_alumnoFragment3)
                4 -> Navigation.findNavController(binding.root)
                    .navigate(R.id.action_game3Fragment_to_alumnoFragment4)
            }
        }
    }
}