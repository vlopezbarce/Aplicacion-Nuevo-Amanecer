package com.tec.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.navigation.Navigation
import com.tec.myapplication.databinding.FragmentFolderBinding

class FolderFragment : Fragment() {
    private var _binding : FragmentFolderBinding? = null
    private val binding get() = _binding!!

    private lateinit var imagenAdapter: ImagenAdapter
    private val imagenesList = mutableListOf<Imagen>()
    private lateinit var listViewImagenes: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFolderBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegresar.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_folderFragment_to_editaDatosAlumnoFragment)
        }

        listViewImagenes = view.findViewById(R.id.listViewImagenes)
        imagenAdapter = ImagenAdapter(requireContext(), imagenesList)
        listViewImagenes.adapter = imagenAdapter

        imagenesList.add(Imagen("1", "imagen_1.png"))
        imagenesList.add(Imagen("2", "imagen_2.png"))
        imagenesList.add(Imagen("3", "imagen_3.png"))
        imagenesList.add(Imagen("4", "imagen_4.png"))
        imagenesList.add(Imagen("5", "imagen_5.png"))
        imagenesList.add(Imagen("6", "imagen_6.png"))
        imagenesList.add(Imagen("7", "imagen_7.png"))
        imagenesList.add(Imagen("8", "imagen_8.png"))

        imagenAdapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FolderFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}