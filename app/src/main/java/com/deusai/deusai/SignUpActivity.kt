package com.deusai.deusai

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.deusai.deusai.databinding.ActivitySignUpBinding

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
            icon.setImageResource(R.drawable.ic_visibility)
        } else {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            icon.setImageResource(R.drawable.ic_visibility_off)
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
        val email = binding.etUsername.text.toString() // Kullanıcı adı yerine e-posta alınıyor
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etPasswordConfirm.text.toString()
        val isChecked = binding.checkBox.isChecked

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showToast("Lütfen tüm alanları doldurun")
            return
        }

        if (!isValidEmail(email)) {
            showToast("Geçerli bir e-posta adresi girin")
            return
        }

        if (!isValidPassword(password)) {
            showToast("Şifre en az 8 karakter, bir büyük harf, bir küçük harf ve bir rakam içermelidir")
            return
        }

        if (password != confirmPassword) {
            showToast("Şifreler eşleşmiyor")
            return
        }

        if (!isChecked) {
            showToast("Devam etmek için Gizlilik Politikası'nı kabul edin")
            return
        }

        if (registeredEmails.contains(email)) {
            showToast("Bu e-posta adresi zaten kayıtlı")
            return
        }

        // Kullanıcıyı kayıtlı olarak listeye ekle
        registeredEmails.add(email)

        showToast("Kayıt başarılı!")
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
        Toast.makeText(this, "Google ile giriş yapılıyor...", Toast.LENGTH_SHORT).show()
        // Firebase veya başka bir Google giriş entegrasyonu burada olacak
    }

    /**
     * Apple ile giriş işlemini başlatan fonksiyon
     */
    private fun signInWithApple() {
        Toast.makeText(this, "Apple ile giriş yapılıyor...", Toast.LENGTH_SHORT).show()
        // Apple giriş entegrasyonunu buraya ekle
    }

    /**
     * Facebook ile giriş işlemini başlatan fonksiyon
     */
    private fun signInWithFacebook() {
        Toast.makeText(this, "Facebook ile giriş yapılıyor...", Toast.LENGTH_SHORT).show()
        // Facebook giriş entegrasyonunu buraya ekle
    }
}
