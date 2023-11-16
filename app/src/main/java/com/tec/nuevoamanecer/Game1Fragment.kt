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
import com.tec.nuevoamanecer.databinding.FragmentGame1Binding

class Game1Fragment : Fragment() {

    private var _binding : FragmentGame1Binding? = null
    private val binding get() = _binding!!

    private lateinit var progressBar: ProgressBar
    private var conta = 0

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
        val burbuja = binding.imagenBurbuja
        val contador = binding.textContador
        val mp = MediaPlayer.create(requireContext(), R.raw.sample)

        burbuja.setOnClickListener {
            if (conta < 95) {
                conta += 5
                progressBar.progress = conta

                mp.start()
                val random = Random.Default

                val sizeBase = 300
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
            } else {
                contador.text = "20"
                conta += 5
                progressBar.progress = conta

                burbuja.visibility = View.INVISIBLE
            }
        }

        binding.btnRegresar.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_game1Fragment_to_alumnoFragment)
        }
    }
}