package com.deusai.deusai

import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.deusai.deusai.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private var isButtonDisabled = false
    private var lastEmailSent: String? = null
    private var countDownTimer: CountDownTimer? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("ForgotPasswordPrefs", MODE_PRIVATE)
        restoreUserData()
        checkRemainingTime()

        binding.btnSubmit.setOnClickListener {
            val email = binding.etMail.text.toString().trim()
            if (!isValidEmail(email)) {
                Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
            } else {
                if (isButtonDisabled && lastEmailSent == email) {
                    Toast.makeText(this, getString(R.string.wait_before_resending), Toast.LENGTH_SHORT).show()
                } else {
                    sendPasswordResetEmail(email)
                }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun sendPasswordResetEmail(email: String) {
        Toast.makeText(this, "$email ${getString(R.string.password_reset_sent)}", Toast.LENGTH_LONG).show()
        lastEmailSent = email
        val endTime = System.currentTimeMillis() + 120000 // 2 dakika sonrası
        sharedPreferences.edit().putLong("timerEndTime", endTime)
            .putString("savedEmail", email)
            .apply()
        disableButtonForTwoMinutes()
    }

    private fun disableButtonForTwoMinutes() {
        isButtonDisabled = true
        binding.btnSubmit.isEnabled = false
        binding.etMail.isEnabled = false // E-posta girişini devre dışı bırak
        startCountdown()
    }

    private fun startCountdown() {
        val endTime = sharedPreferences.getLong("timerEndTime", 0)
        val remainingTime = endTime - System.currentTimeMillis()

        if (remainingTime > 0) {
            countDownTimer?.cancel()
            countDownTimer = object : CountDownTimer(remainingTime, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsRemaining = millisUntilFinished / 1000
                    val minutes = secondsRemaining / 60
                    val seconds = secondsRemaining % 60
                  //  val x=getString(R.string.app_name)
                    binding.btnSubmit.text = String.format("${getString(R.string.resend)}: %02d:%02d", minutes, seconds)
                }

                override fun onFinish() {
                    isButtonDisabled = false
                    binding.btnSubmit.isEnabled = true
                    binding.btnSubmit.text = getString(R.string.send)
                    binding.etMail.isEnabled = true // Kullanıcının yeni e-posta girmesine izin ver
                    binding.etMail.text.clear() // E-posta giriş alanını temizle
                    sharedPreferences.edit().remove("timerEndTime").remove("savedEmail").apply()
                }
            }.start()
        } else {
            isButtonDisabled = false
            binding.btnSubmit.isEnabled = true
            binding.btnSubmit.text = getString(R.string.send)
            binding.etMail.isEnabled = true // Eğer süre bitmişse tekrar girişe izin ver
            binding.etMail.text.clear() // E-posta giriş alanını temizle
        }
    }

    private fun checkRemainingTime() {
        val endTime = sharedPreferences.getLong("timerEndTime", 0)
        if (endTime > System.currentTimeMillis()) {
            disableButtonForTwoMinutes()
        }
    }

    private fun restoreUserData() {
        val savedEmail = sharedPreferences.getString("savedEmail", "")
        if (!savedEmail.isNullOrEmpty()) {
            binding.etMail.setText(savedEmail)
            binding.etMail.isEnabled = false // Kullanıcı e-postayı değiştiremesin
        }
    }
}
