package com.deusai.deusai

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.deusai.deusai.databinding.ActivitySignUpBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class SignUpActivity : AppCompatActivity() {
    // ViewBinding değişkeni tanımlanıyor
    private lateinit var binding: ActivitySignUpBinding

    // Veritabanı gibi bir yapı ile kayıtlı e-postaları saklıyoruz (Gerçek uygulamada Firebase veya Room Database kullanılmalı)
    private val registeredEmails = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding ile XML dosyasını bağlama
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val checkBox = findViewById<CheckBox>(R.id.checkBox)
        val tvPrivacyPolicy = findViewById<TextView>(R.id.tvPrivacyPolicy)

        // Metne tıklanınca sadece diyalog açılacak, CheckBox değişmeyecek
        tvPrivacyPolicy.setOnClickListener {
            showPrivacyDialog()
        }

        // CheckBox'a tıklanınca sadece CheckBox işaretlenecek
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            checkBox.isChecked = isChecked
        }

        // Şifre görünürlüğünü değiştiren fonksiyonlar çağırılıyor
        setupPasswordVisibilityToggle()

        // Butonlara tıklama olayları atanıyor
        binding.btnSignUp.setOnClickListener { onSignUpClicked() }
        binding.txtLogin.setOnClickListener { onLoginClicked() }

        binding.imgGoogle.setOnClickListener {
            // Google ile giriş işlemi
            signInWithGoogle()
        }

        binding.imgApple.setOnClickListener {
            // Apple ile giriş işlemi
            signInWithApple()
        }

        binding.imgFacebook.setOnClickListener {
            // Facebook ile giriş işlemi
             signInWithFacebook()
        }
    }
    private fun showPrivacyDialog() {
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)

        val closeButton = view.findViewById<Button>(R.id.closeButton)
        val contentText = view.findViewById<TextView>(R.id.dialogContent)

        contentText.text = getString(R.string.terms_and_privacy).trimIndent()

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setDimAmount(0.6f) // Arka plan %60 koyulaşsın
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        dialog.show()
    }

    // Şifre görünürlüğünü değiştiren fonksiyon
    private fun setupPasswordVisibilityToggle() {
        binding.visibilityIcon1.setOnClickListener {
            togglePasswordVisibility(binding.etPassword, binding.visibilityIcon1)
        }
        binding.visibilityIcon2.setOnClickListener {
            togglePasswordVisibility(binding.etPasswordConfirm, binding.visibilityIcon2)
        }
    }

    // Şifre giriş alanının görünürlüğünü değiştirir
    private fun togglePasswordVisibility(editText: EditText, icon: ImageView) {
        if (editText.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            icon.setImageResource(R.drawable.ic_visibility_off)
        } else {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            icon.setImageResource(R.drawable.ic_visibility)
        }
        editText.setSelection(editText.text.length) // İmleç konumunu korur
    }

    // Geçerli bir e-posta formatını kontrol eden fonksiyon
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Şifrenin belirlenen kurallara uygun olup olmadığını kontrol eden fonksiyon
    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$".toRegex()
        return passwordPattern.matches(password)
    }

    // Kayıt ol butonuna tıklandığında çalışacak fonksiyon
    private fun onSignUpClicked() {
        val username = binding.etUsername.text.toString()
        val email = binding.etEposta.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etPasswordConfirm.text.toString()
        val isChecked = binding.checkBox.isChecked

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showToast(getString(R.string.fill_all_fields))
            return
        }

        if (!isValidEmail(email)) {
            showToast(getString(R.string.invalid_email))
            return
        }

        if (!isValidPassword(password)) {
            showToast(getString(R.string.invalid_password))
            return
        }

        if (password != confirmPassword) {
            showToast(getString(R.string.passwords_do_not_match))
            return
        }

        if (!isChecked) {
            showToast(getString(R.string.accept_privacy_policy))
            return
        }

        if (registeredEmails.contains(email)) {
            showToast(getString(R.string.email_already_registered))
            return
        }

        // Kullanıcıyı kayıtlı olarak listeye ekle
        registeredEmails.add(email)

        showToast(getString(R.string.registration_successful))
    }

    // Giriş yap butonuna tıklandığında çalışacak fonksiyon
    private fun onLoginClicked() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    // Kullanıcıya mesaj göstermek için yardımcı fonksiyon
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Google ile giriş işlemini başlatan fonksiyon
     */
    private fun signInWithGoogle() {
        Toast.makeText(this, getString(R.string.google_icon), Toast.LENGTH_SHORT).show()
        // Firebase veya başka bir Google giriş entegrasyonu burada olacak
    }

    /**
     * Apple ile giriş işlemini başlatan fonksiyon
     */
    private fun signInWithApple() {
        Toast.makeText(this, getString(R.string.apple_icon), Toast.LENGTH_SHORT).show()
        // Apple giriş entegrasyonunu buraya ekle
    }

    /**
     * Facebook ile giriş işlemini başlatan fonksiyon
     */
    private fun signInWithFacebook() {
        Toast.makeText(this, getString(R.string.facebook_icon), Toast.LENGTH_SHORT).show()
        // Facebook giriş entegrasyonunu buraya ekle
    }
}
