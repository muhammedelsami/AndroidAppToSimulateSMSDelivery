package com.muhammed.androidapptosimulatesmsdelivery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var sendSmsButton: Button
    private lateinit var receivedSmsTextView: TextView
    private val SMS_PERMISSION_CODE = 100
    private val SMS_SENT = "SMS_SENT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sendSmsButton = findViewById(R.id.sendSmsButton)
        receivedSmsTextView = findViewById(R.id.receivedSmsTextView)

        // SMS izinlerini kontrol et
        checkSmsPermission()

        // SMS gönderme butonunun tıklama olayı
        sendSmsButton.setOnClickListener {
            sendSmsToSelf()
        }

        // SMS alıcıyı kaydet
        registerSmsReceiver()
    }

    private fun checkSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) !=
            PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) !=
            PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) !=
            PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_SMS
                ),
                SMS_PERMISSION_CODE
            )
        }
    }

    private fun sendSmsToSelf() {
        try {
            val phoneNumber = getDevicePhoneNumber() ?: "5555" // Emülatör için varsayılan numara
            //val message = "Bu bir test SMS'idir. Gönderilme zamanı: ${System.currentTimeMillis()}"
            val message = "Kullanıcı Adınız: vpn.detay.sefadonmezcan\n" +
                    "Token Kodunuz: 831880\n" +
                    "\n" +
                    " B002"

            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)

            Toast.makeText(this, "SMS gönderme isteği yollandı", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "SMS gönderilemedi: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun getDevicePhoneNumber(): String? {
        // Gerçek cihazlarda telefon numarasını TelephonyManager ile alabilirsiniz
        // Ancak modern Android sürümlerinde güvenlik nedeniyle bu kısıtlıdır
        // Bu örnekte test amaçlı olarak manuel bir numara kullanıyoruz
        // return "5555" // Emülatör için test numarası
        return "+905522471513" // Gerçek bir numara
    }

    private val smsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
                val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                for (message in messages) {
                    val sender = message.originatingAddress
                    val body = message.messageBody
                    displayReceivedSms(sender, body)
                }
            }
        }
    }

    private fun registerSmsReceiver() {
        val filter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        registerReceiver(smsReceiver, filter)
    }

    private fun displayReceivedSms(sender: String?, body: String?) {
        val text = "Gönderici: $sender\nMesaj: $body\nAlınma zamanı: ${System.currentTimeMillis()}"
        receivedSmsTextView.text = text
        Toast.makeText(this, "Yeni SMS alındı!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(smsReceiver)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "SMS izinleri verildi", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "SMS izinleri reddedildi, uygulama düzgün çalışmayabilir", Toast.LENGTH_LONG).show()
            }
        }
    }
}