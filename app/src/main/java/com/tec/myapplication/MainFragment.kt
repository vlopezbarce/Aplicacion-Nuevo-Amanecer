package com.tec.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.tec.myapplication.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnIniciarSesion.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_loginFragment)
        }

        binding.btnRegistrar.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_registroFragment)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}