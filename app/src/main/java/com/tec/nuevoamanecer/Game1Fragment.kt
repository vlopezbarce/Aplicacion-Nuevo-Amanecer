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
import androidx.navigation.Navigation
import com.tec.nuevoamanecer.databinding.FragmentGame1Binding
import android.graphics.Color
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class Game1Fragment : Fragment() {

    private var _binding : FragmentGame1Binding? = null
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
        _binding = FragmentGame1Binding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar
        viewKonfetti = binding.viewKonfetti
        val burbuja = binding.imagenBurbuja
        val contador = binding.textContador
        val mp = MediaPlayer.create(requireContext(), R.raw.sample)
        val mpCheer = MediaPlayer.create(requireContext(), R.raw.cheer)

        burbuja.setOnClickListener {
            if (conta < 84) {
                conta += 6
                progressBar.progress = conta

                mp.start()
                val random = java.util.Random()

                val sizeBase = 300;
                val change = random.nextInt(300)
                val newSize = sizeBase + change
                burbuja.layoutParams.width = newSize
                burbuja.layoutParams.height = newSize


                val maxWidth = resources.displayMetrics.widthPixels - burbuja.width
                val maxHeight = resources.displayMetrics.heightPixels - burbuja.height

                val newMarginStart = random.nextInt(maxWidth)
                val newMarginTop = random.nextInt(maxHeight)

                val params = burbuja.layoutParams as ConstraintLayout.LayoutParams
                params.marginStart = newMarginStart
                params.topMargin = newMarginTop
                burbuja.layoutParams = params


                val temp = contador.text.toString().toInt()
                contador.text = (temp + 1).toString()
                Log.e(conta.toString(), "conta")

            } else if (conta == 84){
                contador.text = "15"
                conta = 100
                progressBar.progress = conta

                burbuja.visibility = View.INVISIBLE

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

        binding.btnRegresar.setOnClickListener{
            when (nivel) {
                1 -> Navigation.findNavController(binding.root)
                    .navigate(R.id.action_game1Fragment_to_alumnoFragment)
                2 -> Navigation.findNavController(binding.root)
                    .navigate(R.id.action_game1Fragment_to_alumnoFragment2)
                3 -> Navigation.findNavController(binding.root)
                    .navigate(R.id.action_game1Fragment_to_alumnoFragment3)
                4 -> Navigation.findNavController(binding.root)
                    .navigate(R.id.action_game1Fragment_to_alumnoFragment4)
            }
        }
    }
}