package com.example.previsaodotempo.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.previsaodotempo.data.CurrentLocation
import com.example.previsaodotempo.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.Locale;
import java.util.Date;


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val PrevisaoMeteorologica = PrevisaoMeteorologica(
        onLocationClicked = {
            Toast.makeText(requireContext(),  "onLocationClicked()", Toast.LENGTH_SHORT).show()
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPrevisaoMeteorologica()
        setPrevisaoData()
    }

    private fun setPrevisaoMeteorologica() {
        binding.previsaoDataRecyclerView.adapter = PrevisaoMeteorologica
    }

    private fun setPrevisaoData() {
        PrevisaoMeteorologica.setData(data = listOf(CurrentLocation(date = getCurrentDate())))
    }
    private fun getCurrentDate(): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        return "Hoje, ${formatter.format(currentDate)}"
    }
}