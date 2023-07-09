package com.melfouly.paypaldemo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.melfouly.paypaldemo.databinding.ActivityMainBinding
import com.melfouly.paypaldemo.model.Amount
import com.melfouly.paypaldemo.model.OrderRequest
import com.melfouly.paypaldemo.model.PurchaseUnit
import com.paypal.android.cardpayments.OrderIntent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.requestAccessToken()

        viewModel.expirationTime.observe(this) {
            val timer = object : CountDownTimer((it*1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.d("TAG", "onCreate: $millisUntilFinished")
                    binding.expirationTime.text = (millisUntilFinished/1000).toString()
                }

                override fun onFinish() {
                    viewModel.requestAccessToken()
                }
            }
            timer.start()
        }

        binding.paypalButton.setOnClickListener {
            val amount = Amount(
                binding.amountCurrencyCodeEdittext.text.toString(),
                binding.amountValueEdittext.text.toString()
            )
            val purchaseUnit = PurchaseUnit(amount)
            val purchaseList: List<PurchaseUnit> = listOf(purchaseUnit)
            val orderRequest = OrderRequest(
                "CAPTURE",
                purchaseList
            )

            viewModel.requestOrder(orderRequest)

            val intent = Intent(this, PaypalPaymentActivity::class.java)

            viewModel.orderResponse.observe(this) {
                intent.putExtra("ORDER_ID", it.id)
                intent.putExtra("ORDER_STATUS", it.status)
                Log.d("Activity", "onCreate: ${intent.extras?.getString("ORDER_ID")}")
                startActivity(intent)
            }

        }
    }
}