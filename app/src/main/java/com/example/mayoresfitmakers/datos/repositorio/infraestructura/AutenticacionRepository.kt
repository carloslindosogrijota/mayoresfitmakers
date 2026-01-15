package com.example.mayoresfitmakers.datos.repositorio.infraestructura

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class AutenticacionRepository {

    interface LoginCallback {
        fun onLoginOk(uid: String, correo: String)
        fun onLoginError(mensaje: String)
    }

    private val auth = FirebaseAuth.getInstance()

    fun login(correo: String, contrasena: String, callback: LoginCallback) {

        Log.d("LOGIN", "AuthRepo.login() correo=$correo contrasenaLen=${contrasena.length}")

        if (correo.isBlank()) {
            callback.onLoginError("Correo vacío.")
            return
        }

        if (contrasena.isBlank()) {
            callback.onLoginError("Contraseña vacía.")
            return
        }

        val tiempoMaximoMs = 12000L
        val handler = Handler(Looper.getMainLooper())

        val runnableTimeout = Runnable {
            Log.e("LOGIN", "AuthRepo TIMEOUT tras ${tiempoMaximoMs}ms")
            callback.onLoginError("Timeout de login (emulador/red/Play Services).")
        }

        handler.postDelayed(runnableTimeout, tiempoMaximoMs)

        auth.signInWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                handler.removeCallbacks(runnableTimeout)

                Log.d("LOGIN", "AuthRepo.onComplete success=${task.isSuccessful}")

                if (task.isSuccessful) {
                    val usuario = auth.currentUser
                    val uid = usuario?.uid
                    val email = usuario?.email

                    Log.d("LOGIN", "AuthRepo.currentUser uid=$uid email=$email")

                    if (uid.isNullOrBlank()) {
                        callback.onLoginError("Login OK pero uid nulo.")
                        return@addOnCompleteListener
                    }

                    callback.onLoginOk(uid, correo)
                } else {
                    val mensaje = task.exception?.message ?: "Error desconocido en login."
                    Log.e("LOGIN", "AuthRepo.login FAIL: $mensaje", task.exception)
                    callback.onLoginError(mensaje)
                }
            }
            .addOnFailureListener { exception ->
                handler.removeCallbacks(runnableTimeout)
                Log.e("LOGIN", "AuthRepo.addOnFailureListener: ${exception.message}", exception)
                callback.onLoginError(exception.message ?: "Fallo en login.")
            }
    }
}
