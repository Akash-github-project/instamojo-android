package com.recharge.instamojo_poc

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.instamojo.android.Instamojo
import com.permissionx.guolindev.PermissionX
import com.recharge.instamojo_poc.api.OrderIdModel
import com.recharge.instamojo_poc.api.Repository
import com.recharge.instamojo_poc.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), Instamojo.InstamojoPaymentCallback {
    private lateinit var binding: ActivityMainBinding
    var viewModel: PocViewModel? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[PocViewModel::class.java]
        binding.open.setOnClickListener {
            startPayment()
        }
        viewModel!!.responseLiveData.observe(this) {
            binding.response.text = it.toString()
            Instamojo.getInstance().initiatePayment(this, it.created.order_id, this)
        }
    }

    private fun startPayment() {
        checkForPermission()
    }

    private fun checkForPermission() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS
            )
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "Core fundamental are based on these permissions",
                    "OK",
                    "Cancel"
                )
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    Toast.makeText(this, "All permissions are granted", Toast.LENGTH_LONG).show()
                    runPaymentDialog()
                } else {
                    showDialog("Some Permissons are not granded Please grant them manually form settings")
                }
            }
    }

    private fun runPaymentDialog() {
        Instamojo.getInstance().initialize(this, Instamojo.Environment.TEST)
        CoroutineScope(Dispatchers.IO).launch {
            Repository.getOrderIdFormServer(
                OrderIdModel(
                    amount = "10",
                    buyer_name = "Akash sinha",
                    email = "someone@anyone.com",
                    phone = "8709031440",
                    purpose = "testing"
                )
            ).body()?.let {
                viewModel?.setLiveData(
                    it
                )
            }
        }
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this).setMessage(message)
            .setPositiveButton(
                "Allow Manually"
            ) { dialogInterface, _ ->
                val intent = Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", this@MainActivity.packageName, null)
                    data = uri
                    dialogInterface.dismiss()
                }
                startActivity(intent)
            }
            .setNegativeButton("Nope") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
    }

    override fun onInstamojoPaymentComplete(
        orderID: String,
        transactionID: String,
        paymentID: String,
        paymentStatus: String
    ) {
        Toast.makeText(
            this,
            "status $paymentStatus payment id $paymentID payment",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onPaymentCancelled() {
        Toast.makeText(
            this,
            "Payment canceled",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onInitiatePaymentFailure(errorMessage: String?) {
        Toast.makeText(
            this,
            "Payment Failed $errorMessage",
            Toast.LENGTH_SHORT
        ).show()
    }
}