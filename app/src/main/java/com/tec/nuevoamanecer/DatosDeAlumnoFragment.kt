package com.tec.nuevoamanecer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.tec.nuevoamanecer.databinding.FragmentDatosDeAlumnoBinding

class DatosDeAlumnoFragment : Fragment() {
    private var _binding : FragmentDatosDeAlumnoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDatosDeAlumnoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegresar.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_datosDeAlumnoFragment_to_registroFragment)
        }

        binding.btnSiguiente.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_datosDeAlumnoFragment_to_datosRegistradosFragment)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DatosDeAlumnoFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}