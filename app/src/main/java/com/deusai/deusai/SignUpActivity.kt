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

        // CheckBox metnine tıklanınca dialog açılsın
        checkBox.setOnClickListener {
            showPrivacyDialog()
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

        // Kullanım şartlarını burada belirleyebilirsin
        contentText.text = """
            • Uygulamayı kullanarak gizlilik politikamızı kabul etmiş olursunuz.
            • Kullanıcı verileri saklanmaz ve paylaşılmaz.
            • Uygulama kullanımı tamamen ücretsizdir.
        """.trimIndent()

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setContentView(view)
        // Pencereyi şeffaf hale getir
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setDimAmount(0.6f) // Arka planın kararma seviyesini belirle (%60 koyu)


        // Dialog'un genişliğini belirleyerek ekranı tamamen kaplamamasını sağla
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent) // Arka planı şeffaf yap

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
