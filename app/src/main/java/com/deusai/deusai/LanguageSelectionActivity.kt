package com.deusai.deusai

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.deusai.deusai.databinding.ActivityLanguageSelectionBinding
import java.util.Locale

class LanguageSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLanguageSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Dillerin listesi
        val languages = arrayOf(
            getString(R.string.select_language), // "Uygulama dilini seçiniz"
            "English", "Türkçe", "Français", "Deutsch", "Español"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.languageSpinner.adapter = adapter

        binding.btnNext.isEnabled = false

        // Seçilen dili kontrol et
        binding.languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.btnNext.isEnabled = position != 0
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.btnNext.isEnabled = false
            }
        }

        binding.btnNext.setOnClickListener {
            val selectedLanguage = binding.languageSpinner.selectedItemPosition
            setAppLanguage(selectedLanguage)

            // Kullanıcının dili seçtiğini kaydet
            val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("LanguageSelected", true).apply()

            // Giriş ekranına yönlendir
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun setAppLanguage(selectedLanguage: Int) {
        val languageCode = when (selectedLanguage) {
            1 -> "en"  // English
            2 -> "tr"  // Turkish
            3 -> "fr"  // French
            4 -> "de"  // German
            5 -> "es"  // Spanish
            else -> return
        }

        LanguageHelper.setAppLanguage(this, languageCode)
    }

}
