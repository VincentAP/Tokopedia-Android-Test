package com.tokopedia.maps

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tokopedia.maps.di.Injection
import com.tokopedia.maps.model.LocationResponse
import com.tokopedia.maps.util.RxHelper
import com.tokopedia.maps.util.Status
import com.tokopedia.maps.viewmodel.LocationViewModelFactory
import com.tokopedia.maps.viewmodel.MapsViewModel
import kotlinx.android.synthetic.main.activity_maps.*

open class MapsActivity : AppCompatActivity() {

    private var mapFragment: SupportMapFragment? = null
    private var googleMap: GoogleMap? = null

    private lateinit var mapsViewModel: MapsViewModel

    private lateinit var textCountryName: TextView
    private lateinit var textCountryCapital: TextView
    private lateinit var textCountryPopulation: TextView
    private lateinit var textCountryCallCode: TextView

    private var editText: EditText? = null
    private var buttonSubmit: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        setupMapsViewModel()
        bindViews()
        initListeners()
        loadMap()
    }

    private fun setupMapsViewModel() {
        val vmFactory = LocationViewModelFactory(Injection.injectLocationUseCase())
        mapsViewModel = ViewModelProvider(this, vmFactory).get(MapsViewModel::class.java)

        mapsViewModel.locationItem.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    linearCountryDetailWrapper.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    linearCountryDetailWrapper.visibility = View.VISIBLE
                    it.data?.get(0)?.let { resp ->
                        setSuccessItemUi(resp)
                    }
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    linearCountryDetailWrapper.visibility = View.VISIBLE
                    setNoItemUi()
                    showToast(it.message ?: "unknown error")
                }
                else -> {
                    progressBar.visibility = View.GONE
                    linearCountryDetailWrapper.visibility = View.VISIBLE
                    setNoItemUi()
                }
            }
        })
    }

    private fun bindViews() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        editText = findViewById(R.id.editText)
        buttonSubmit = findViewById(R.id.buttonSubmit)
        textCountryName = findViewById(R.id.txtCountryName)
        textCountryCapital = findViewById(R.id.txtCountryCapital)
        textCountryPopulation = findViewById(R.id.txtCountryPopulation)
        textCountryCallCode = findViewById(R.id.txtCountryCallCode)
    }

    private fun initListeners() {
        buttonSubmit!!.setOnClickListener {
            editText?.text?.let { et ->
                if (et.toString().isNotEmpty()) mapsViewModel.getCountryDetail(et.toString())
                else showToast("Please input country name")
            }
        }
    }

    private fun loadMap() {
        mapFragment!!.getMapAsync { googleMap -> this@MapsActivity.googleMap = googleMap }
    }

    private fun setSuccessItemUi(item: LocationResponse) {
        textCountryName.text = getString(R.string.nama_negara, item.name)
        textCountryCapital.text = getString(R.string.ibukota, item.capital)
        textCountryPopulation.text = getString(R.string.jumlah_penduduk, item.population)
        textCountryCallCode.text =
            getString(R.string.kode_telepon, item.callingCodes.joinToString(", ") { it })

        // Setup map
        val latLng = LatLng(item.latlng[0].toDouble(), item.latlng[1].toDouble())
        googleMap?.apply {
            addMarker(MarkerOptions().position(latLng).title(item.name))
            moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5F))
        }
    }

    private fun setNoItemUi() {
        textCountryName.text = getString(R.string.nama_negara, "-")
        textCountryCapital.text = getString(R.string.ibukota, "-")
        textCountryPopulation.text = getString(R.string.jumlah_penduduk, "-")
        textCountryCallCode.text = getString(R.string.kode_telepon, "-")
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        RxHelper.clear()
    }
}
