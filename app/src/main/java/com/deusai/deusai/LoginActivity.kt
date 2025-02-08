package com.deusai.deusai

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.deusai.deusai.databinding.ActivityLoginBinding
import com.deusai.deusai.ui.HomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var isPasswordVisible = false

    // Örnek kullanıcı listesi (Gerçek projelerde bu bilgiler bir veritabanında saklanmalıdır)
    private val users = listOf(
        User("test@example.com", "Test123"),  // Örnek kullanıcı
        User("caner@deusai.com", "Caner123")  // Örnek kullanıcı 2
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ViewBinding ile bağlanma
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Şifre görünürlük ikonuna tıklama dinleyicisi
        binding.visibilityIcon.setOnClickListener {
            togglePasswordVisibility()
        }

        // Giriş Yap butonuna tıklama dinleyicisi
        binding.btnLogin.setOnClickListener {
            login()
        }

        // Sosyal medya ikonlarına tıklama dinleyicileri
        binding.imgGoogle.setOnClickListener {
            signInWithGoogle()
        }

        binding.imgApple.setOnClickListener {
            signInWithApple()
        }

        binding.imgFacebook.setOnClickListener {
            signInWithFacebook()
        }

        // 'Şifremi unuttum' metnine tıklama dinleyicisi
        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        // 'Hesabınız yok mu?' metnine tıklama dinleyicisi
        binding.txtSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    // Şifre görünürlüğünü değiştir
    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT
            binding.visibilityIcon.setImageResource(R.drawable.ic_visibility_off)
        } else {
            binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.visibilityIcon.setImageResource(R.drawable.ic_visibility)
        }
        binding.etPassword.setSelection(binding.etPassword.text.length)
    }

    // Giriş yap işlemi
    private fun login() {
        val email = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (!isValidEmail(email)) {
            Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidPassword(password)) {
            Toast.makeText(this, getString(R.string.invalid_password), Toast.LENGTH_SHORT).show()
            return
        }

        // Kullanıcı kontrolü
        val user = users.find { it.email == email }
        if (user == null) {

            // Kullanıcı adı ve şifre kontrolü
            if (email == "test@gmail.com" && password == "Test25") {
                // Giriş başarılı olduğunda mesaj göster ve MainActivity'ye yönlendir
                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()  // Bu Activity'yi kapat
               // Toast.makeText(this, "Kayıt bulunamadı. Lütfen önce kayıt olun.", Toast.LENGTH_SHORT).show()
            } else {
                // Kullanıcı adı veya şifre yanlışsa hata mesajı göster
                Toast.makeText(this, getString(R.string.incorrect_credentials), Toast.LENGTH_SHORT).show()
            }
        } else if (user.password != password) {
            Toast.makeText(this, getString(R.string.wrong_password), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
            // Başarılı girişten sonra ana sayfaya yönlendirme
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Giriş ekranını kapat
        }
    }

    /**
     * E-posta formatını kontrol eden fonksiyon
     */
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Şifre kurallarını kontrol eden fonksiyon
     * En az 6 karakter, bir büyük harf, bir küçük harf ve bir rakam içermelidir.
     */
    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$"
        return password.matches(passwordPattern.toRegex())
    }

    /**
     * Google ile giriş işlemini başlatan fonksiyon
     */
    private fun signInWithGoogle() {
        Toast.makeText(this, getString(R.string.google_icon), Toast.LENGTH_SHORT).show()
    }

    /**
     * Apple ile giriş işlemini başlatan fonksiyon
     */
    private fun signInWithApple() {
        Toast.makeText(this, getString(R.string.apple_icon), Toast.LENGTH_SHORT).show()
    }

    /**
     * Facebook ile giriş işlemini başlatan fonksiyon
     */
    private fun signInWithFacebook() {
        Toast.makeText(this, getString(R.string.facebook_icon), Toast.LENGTH_SHORT).show()
    }

    // Kullanıcı verisi için veri sınıfı
    data class User(val email: String, val password: String)
}
