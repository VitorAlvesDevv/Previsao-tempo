package com.example.previsaodotempo.fragments.home

import com.example.previsaodotempo.R
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
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat
import java.util.Locale;
import java.util.Date;
import android.location.Geocoder
import android.Manifest
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener

import androidx.navigation.fragment.findNavController
import com.example.previsaodotempo.armazenar.PreferenciasCompartilhadas
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.android.inject


class HomeFragment : Fragment() {

    companion object {
        const val REQUEST_KEY_MANUAL_LOCATION_SEARCH = "manualLocationSearch"
        const val KEY_LOCATION_TEXT = "locationText"
        const val KEY_LATITUDE = "latitude"
        const val KEY_LONGITUDE = "longitude"
    }


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val modeloVisualizacao: ModeloVisualizacao by viewModel()
    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }
    private val geocoder by lazy { Geocoder(requireContext()) }
    private val PrevisaoMeteorologica = PrevisaoMeteorologica(
        onLocationClicked = { showLocationOptions() }
    )

    private val PreferenciasCompartilhadas: PreferenciasCompartilhadas by inject()

    private val locationPermissionLaucher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation()
        } else {
            Toast.makeText(requireContext(), "Permissão Negada", Toast.LENGTH_SHORT).show()
        }
    }

    private var isInitialLocationSet: Boolean = false

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
        setCurrentLocation(currentLocation = PreferenciasCompartilhadas.getCurrentLocation())
        setObservers()
        if (!isInitialLocationSet) {
            setCurrentLocation(currentLocation = PreferenciasCompartilhadas.getCurrentLocation())
            isInitialLocationSet = true
        }
    }

    private fun setObservers() {
        with(modeloVisualizacao) {
            currentLocation.observe(viewLifecycleOwner) {
                val currentLocationDataState = it.getContentIfNotHandled() ?: return@observe


                if (currentLocationDataState.isLoading) {
                    showLoading()
                }
                currentLocationDataState.currentLocation?.let { currentLocation ->
                    hideLoading()
                    PreferenciasCompartilhadas.saveCurrentLocation(currentLocation)
                    setCurrentLocation(currentLocation)
                }
                currentLocationDataState.error?.let { error ->
                    hideLoading()
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            }
            previsaoData.observe(viewLifecycleOwner) {
                val previsaoDataState = it.getContentIfNotHandled() ?: return@observe
                binding.swipeRefreshLayout.isRefreshing = previsaoDataState.isLoading
                previsaoDataState.currentPrevisao?.let { currentPrevisao ->
                    Toast.makeText(
                        requireContext(),
                        currentPrevisao.temperature.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                previsaoDataState.error?.let { error ->
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun setPrevisaoMeteorologica() {
        binding.previsaoDataRecyclerView.adapter = PrevisaoMeteorologica
    }

    private fun setCurrentLocation(currentLocation: CurrentLocation? = null) {
        PrevisaoMeteorologica.setCurrentLocation(currentLocation ?: CurrentLocation())
        currentLocation?.let { getPrevisaoData(currentLocation = it) }
    }


    private fun getCurrentLocation() {
        modeloVisualizacao.getCurrentLocation(fusedLocationProviderClient, geocoder)
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
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
            setTitle("Selecione seu metodo de Localização")
            setItems(options) { _, which ->
                when (which) {
                    0 -> proceedWithCurrentLocation()
                    1 -> startManualLocationSearch()
                }
            }
            show()
        }
    }


    private fun showLoading() {
        with(binding) {
            previsaoDataRecyclerView.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = true
        }
    }

    private fun hideLoading() {
        with(binding) {
            previsaoDataRecyclerView.visibility = View.VISIBLE
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun startManualLocationSearch() {
        startListeningManualLocationSelection()
        findNavController().navigate(R.id.action_home_fragment_to_location_fragment)
    }

    private fun startListeningManualLocationSelection() {
        setFragmentResultListener(REQUEST_KEY_MANUAL_LOCATION_SEARCH) { _, bundle ->
            stopListeningManualLocationSelection()
            val currentLocation = CurrentLocation(
                location = bundle.getString(KEY_LOCATION_TEXT) ?: "N/A",
                latitude = bundle.getDouble(KEY_LATITUDE),
                longitude = bundle.getDouble(KEY_LONGITUDE)
            )
            PreferenciasCompartilhadas.saveCurrentLocation(currentLocation)
            setCurrentLocation(currentLocation)
        }
    }


    private fun stopListeningManualLocationSelection() {
        clearFragmentResultListener(REQUEST_KEY_MANUAL_LOCATION_SEARCH)
    }

    private fun getPrevisaoData(currentLocation: CurrentLocation) {
        if (currentLocation.latitude != null && currentLocation.longitude != null) {
            modeloVisualizacao.getPrevisaoData(
                latitude = currentLocation.latitude,
                longitude = currentLocation.longitude

            )
        }
    }

}