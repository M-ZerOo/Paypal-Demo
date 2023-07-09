package com.melfouly.paypaldemo.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.melfouly.paypaldemo.databinding.ActivityPaypalPaymentBinding
import com.melfouly.paypaldemo.di.NetworkModule.CLIENT_ID
import com.melfouly.paypaldemo.model.BillingAddress
import com.paypal.android.cardpayments.Card
import com.paypal.android.cardpayments.CardClient
import com.paypal.android.cardpayments.CardRequest
import com.paypal.android.cardpayments.model.CardResult
import com.paypal.android.corepayments.Address
import com.paypal.android.corepayments.CoreConfig
import com.paypal.android.corepayments.Environment
import com.paypal.android.corepayments.PayPalSDKError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaypalPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaypalPaymentBinding
    private lateinit var viewModel: PaypalPaymentViewModel
    private lateinit var orderId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaypalPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orderId = intent.extras?.getString("ORDER_ID").toString()

        // Initiate the Payments SDK
//        val config = CoreConfig(CLIENT_ID, Environment.SANDBOX)
//        val cardClient = CardClient(this, config)
//        cardClient.approveOrderListener = this

        viewModel = ViewModelProvider(this)[PaypalPaymentViewModel::class.java]

        viewModel.requestAccessToken()

            binding.orderId.text = orderId
            binding.orderStatus.text = intent.extras?.getString("ORDER_STATUS")


        // Once order button clicked it calls requestOrder method from viewModel and
        // makes a Card instance
        binding.orderButton.setOnClickListener {
            val card = Card(
                binding.cardNumberEdittext.text.toString(),
                binding.cardExpMonthEdittext.text.toString(),
                binding.cardExpYearEdittext.text.toString(),
                binding.cardSecurityCodeEdittext.text.toString(),
                billingAddress = Address(
                    binding.cardCountryCodeEdittext.text.toString(),
                    binding.cardStreetAddressEdittext.text.toString(),
                    binding.cardExtendedAddressEdittext.text.toString(),
                    binding.cardLocalityEdittext.text.toString(),
                    binding.cardRegionEdittext.text.toString(),
                    binding.cardPostalCodeEdittext.text.toString(),
                )
            )

            viewModel.confirmPayment(orderId, card)
            
            viewModel.paymentResponse.observe(this) {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }

//            val cardRequest = CardRequest(
//                orderId = intent.extras?.getString("ORDER_ID")!!,
//                card = card,
//                returnUrl = "myapp://return_url"
//            )
//
//            fun approveMyOrder(cardRequest: CardRequest) {
//                val result = cardClient.approveOrder(this, cardRequest)
//            }



        }


    }

    fun onApproveOrderSuccess(result: CardResult) {
        // order was successfully approved and is ready to be captured/authorized (see step 6)
        result.deepLinkUrl
    }

    fun onApproveOrderFailure(error: PayPalSDKError) {
        // inspect `error` for more information
        Log.d(
            "TAG",
            "onApproveOrderFailure: Code:${error.code}, Message:${error.message}, Desc: ${error.errorDescription}"
        )
        Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show()
    }

    fun onApproveOrderCanceled() {
        // 3DS flow was canceled
    }

    fun onApproveOrderThreeDSecureWillLaunch() {
        // 3DS flow will launch
    }

    fun onApproveOrderThreeDSecureDidFinish() {
        // user successfully completed 3DS authentication
    }
}