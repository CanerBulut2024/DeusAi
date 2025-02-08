package com.deusai.deusai

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.deusai.deusai.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        LanguageHelper.applySavedLanguage(this) // Kaydedilen dili uygula

        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Kullanıcının daha önce dil seçip seçmediğini kontrol et
        val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val isLanguageSelected = sharedPreferences.getBoolean("LanguageSelected", false)

        // 2 saniyelik gecikmeden sonra yönlendirme yap
        Handler(Looper.getMainLooper()).postDelayed({
            val nextActivity = if (isLanguageSelected) {
                Intent(this, LoginActivity::class.java) // Daha önce dil seçildiyse giriş ekranına yönlendir
            } else {
                Intent(this, LanguageSelectionActivity::class.java) // İlk defa indirildiyse dil seçme ekranına yönlendir
            }
            startActivity(nextActivity)
            finish()
        }, 2000) // 2 saniye splash ekranı
    }
}
