package com.tec.nuevoamanecer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.navigation.Navigation
import com.tec.nuevoamanecer.databinding.FragmentTerapeutaBinding

class TerapeutaFragment : Fragment() {
    private var _binding : FragmentTerapeutaBinding? = null
    private val binding get() = _binding!!

    private lateinit var alumnoAdapter: AlumnoAdapter
    private val alumnosList = mutableListOf<Alumno>()
    private lateinit var listViewAlumnos: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTerapeutaBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegresar.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_terapeutaFragment_to_mainFragment)
        }

        listViewAlumnos = view.findViewById(R.id.listViewAlumnos)
        alumnoAdapter = AlumnoAdapter(requireContext(), alumnosList)
        listViewAlumnos.adapter = alumnoAdapter

        alumnosList.add(Alumno("1", "Alumno 1"))
        alumnosList.add(Alumno("2", "Alumno 2"))
        alumnosList.add(Alumno("3", "Alumno 3"))
        alumnosList.add(Alumno("4", "Alumno 3"))
        alumnosList.add(Alumno("5", "Alumno 5"))
        alumnosList.add(Alumno("6", "Alumno 6"))
        alumnosList.add(Alumno("7", "Alumno 7"))
        alumnosList.add(Alumno("8", "Alumno 8"))

        alumnoAdapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TerapeutaFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}