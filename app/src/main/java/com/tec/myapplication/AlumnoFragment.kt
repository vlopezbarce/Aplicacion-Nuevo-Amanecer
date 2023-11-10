package com.tec.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.tec.myapplication.databinding.FragmentAlumnoBinding

class AlumnoFragment : Fragment() {
    private var _binding : FragmentAlumnoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlumnoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegresar.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_alumnoFragment_to_mainFragment)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AlumnoFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}