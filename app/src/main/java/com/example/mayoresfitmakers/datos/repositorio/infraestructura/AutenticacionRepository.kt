package com.example.mayoresfitmakers.datos.repositorio

import com.google.firebase.auth.FirebaseAuth


class AutenticacionRepository {

    interface LoginCallback {
        fun onLoginOk(uid: String, correo: String)
        fun onLoginError(mensaje: String)
    }

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(correo: String, contrasena: String, callback: LoginCallback) {

        firebaseAuth
            .signInWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        callback.onLoginOk(user.uid, user.email ?: correo)
                    } else {
                        callback.onLoginError("Usuario autenticado pero null")
                    }
                } else {
                    callback.onLoginError(task.exception?.message ?: "Error en login")
                }
            }
    }

    fun registrar(correo: String, contrasena: String, callback: LoginCallback) {

        firebaseAuth
            .createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        callback.onLoginOk(user.uid, user.email ?: correo)
                    } else {
                        callback.onLoginError("Usuario creado pero null")
                    }
                } else {
                    callback.onLoginError(task.exception?.message ?: "Error en registro")
                }
            }
    }
}
