package com.recepgemalmaz.konumkullanimi

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.recepgemalmaz.konumkullanimi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var izinKontrol = 0
    private lateinit var flpc: FusedLocationProviderClient
    private lateinit var locationTask: Task<Location>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        flpc = LocationServices.getFusedLocationProviderClient(this)

        binding.buttonKonumAl.setOnClickListener {
            izinKontrol =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

            if (izinKontrol == PackageManager.PERMISSION_GRANTED) {
                locationTask = flpc.lastLocation
                konumBilgisiAl()
                // İzin verilmişse
            } else {
                // İzin verilmemişse
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    100
                )
            }
        }
    }

    fun konumBilgisiAl() {

        locationTask.addOnSuccessListener { location ->
            if (location != null) {
                binding.textViewEnlem.text = "Enlem: ${location.latitude}"
                binding.textViewBoylam.text = "Boylam: ${location.longitude}"
                Toast.makeText(
                    applicationContext,
                    "Enlem: ${location.latitude} Boylam: ${location.longitude}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Konum alınamadı",
                    Toast.LENGTH_SHORT
                ).show()
                binding.textViewEnlem.text = "Enlem: Bulunamadı"
                binding.textViewBoylam.text = "Boylam: Bulunamadı"
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((requestCode == 100)) {
            izinKontrol =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // İzin verilmişse
                locationTask = flpc.lastLocation
                konumBilgisiAl()
                Toast.makeText(applicationContext, "İzin verildi", Toast.LENGTH_SHORT).show()
            } else {
                // İzin verilmemişse
                Toast.makeText(applicationContext, "İzin verilmedi", Toast.LENGTH_SHORT).show()
            }
        } else {
            // İzin verilmemişse
        }
    }
}