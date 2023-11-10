package com.tec.nuevoamanecer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.tec.nuevoamanecer.databinding.FragmentEditaDatosAlumnoBinding

class EditaDatosAlumnoFragment : Fragment() {
    private var _binding : FragmentEditaDatosAlumnoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditaDatosAlumnoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegresar.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_editaDatosAlumnoFragment_to_terapeutaFragment)
        }

        binding.btnSiguiente.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_editaDatosAlumnoFragment_to_terapeutaFragment)
        }

        binding.btnFolder.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_editaDatosAlumnoFragment_to_folderFragment)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            EditaDatosAlumnoFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}