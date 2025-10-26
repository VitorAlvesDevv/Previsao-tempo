package com.example.previsaodotempo.fragments.home

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.previsaodotempo.data.CurrentLocation
import com.example.previsaodotempo.databinding.FragmentHomeBinding

import java.text.SimpleDateFormat
import java.util.Locale;
import java.util.Date;
import android.Manifest




class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val PrevisaoMeteorologica = PrevisaoMeteorologica(
        onLocationClicked = {showLocationOptions() }
    )

    private val locationPermissionLaucher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation()
        } else {
            Toast.makeText(requireContext(), "Permissão Negada", Toast.LENGTH_SHORT).show()
        }
    }

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

    private fun getCurrentLocation() {
        Toast.makeText(requireContext(), "getCurrentLocation()", Toast.LENGTH_SHORT).show()
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission (
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        locationPermissionLaucher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun proceedWithCurrentLocation() {
        if (isLocationPermissionGranted()) {
            getCurrentLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun showLocationOptions() {
        val options = arrayOf("Localização atual", "Pesquisa Manual")
        AlertDialog.Builder(requireContext()).apply {
            setTittle("Selecione seu metodo de Localização")
            setItems(options) { _, which ->
                when (which) {
                    0 -> proceedWithCurrentLocation()
                }
            }
            show()
        }
    }

    private fun setTittle(string: String) {}


}