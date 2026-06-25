package com.conectatarot.app

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.conectatarot.app.network.DisponibilidadRequest
import com.conectatarot.app.network.RegistroTarotistaRequest
import com.conectatarot.app.network.RetrofitClient
import kotlinx.coroutines.launch

class RegistroTarotistaActivity : AppCompatActivity() {

    private val checkboxesEspecialidades = mutableListOf<CheckBox>()
    private var especialidadesCargadas = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_tarotista)

        val etNombre = findViewById<EditText>(R.id.etNombreTarotista)
        val etEmail = findViewById<EditText>(R.id.etEmailTarotista)
        val etPassword = findViewById<EditText>(R.id.etPasswordTarotista)
        val etNombrePro = findViewById<EditText>(R.id.etNombreProfesional)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcionTarotista)
        val etPrecio = findViewById<EditText>(R.id.etPrecioTarotista)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrarTarotista)
        val tvResultado = findViewById<TextView>(R.id.tvResultadoTarotista)
        val tvVolver = findViewById<TextView>(R.id.tvVolverTarotista)
        val llEspecialidades = findViewById<LinearLayout>(R.id.llEspecialidades)
        val tvCargandoEspecialidades = findViewById<TextView>(R.id.tvCargandoEspecialidades)
        val cbLunes = findViewById<CheckBox>(R.id.cbLunes)
        val cbMartes = findViewById<CheckBox>(R.id.cbMartes)
        val cbMiercoles = findViewById<CheckBox>(R.id.cbMiercoles)
        val cbJueves = findViewById<CheckBox>(R.id.cbJueves)
        val cbViernes = findViewById<CheckBox>(R.id.cbViernes)
        val cbSabado = findViewById<CheckBox>(R.id.cbSabado)
        val cbDomingo = findViewById<CheckBox>(R.id.cbDomingo)

        val etHoraInicio = findViewById<EditText>(R.id.etHoraInicio)
        val etHoraFin = findViewById<EditText>(R.id.etHoraFin)

        tvVolver.setOnClickListener { finish() }

        cargarEspecialidades(llEspecialidades, tvCargandoEspecialidades, tvResultado, btnRegistrar)

        etHoraInicio.setOnClickListener {
            val cal = java.util.Calendar.getInstance()
            android.app.TimePickerDialog(
                this,
                { _, hour, minute ->
                    etHoraInicio.setText(String.format("%02d:%02d", hour, minute))
                },
                cal.get(java.util.Calendar.HOUR_OF_DAY),
                cal.get(java.util.Calendar.MINUTE),
                true
            ).show()
        }

        etHoraFin.setOnClickListener {
            val cal = java.util.Calendar.getInstance()
            android.app.TimePickerDialog(
                this,
                { _, hour, minute ->
                    etHoraFin.setText(String.format("%02d:%02d", hour, minute))
                },
                cal.get(java.util.Calendar.HOUR_OF_DAY),
                cal.get(java.util.Calendar.MINUTE),
                true
            ).show()
        }

        btnRegistrar.setOnClickListener {
            if (!especialidadesCargadas) {
                tvResultado.text = "Espera a que se carguen las especialidades"
                tvResultado.setTextColor(getColor(android.R.color.holo_red_light))
                return@setOnClickListener
            }

            val nombre = etNombre.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val nombrePro = etNombrePro.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()
            val precioStr = etPrecio.text.toString().trim()

            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() ||
                nombrePro.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty()) {
                tvResultado.text = "Por favor completa todos los campos"
                tvResultado.setTextColor(getColor(android.R.color.holo_red_light))
                return@setOnClickListener
            }

            val precio = precioStr.toDoubleOrNull() ?: run {
                tvResultado.text = "El precio debe ser un número válido"
                tvResultado.setTextColor(getColor(android.R.color.holo_red_light))
                return@setOnClickListener
            }

            val especialidades = checkboxesEspecialidades
                .filter { it.isChecked }
                .map { it.tag as Int }

            if (especialidades.isEmpty()) {
                tvResultado.text = "Selecciona al menos una especialidad"
                tvResultado.setTextColor(getColor(android.R.color.holo_red_light))
                return@setOnClickListener
            }

            val horaInicio = etHoraInicio.text.toString().trim()
            val horaFin = etHoraFin.text.toString().trim()

            if (horaInicio.isEmpty() || horaFin.isEmpty()) {
                tvResultado.text = "Selecciona horario de atención"
                tvResultado.setTextColor(getColor(android.R.color.holo_red_light))
                return@setOnClickListener
            }

            val inicio = try {
                java.time.LocalTime.parse(horaInicio)
            } catch (e: Exception) {
                tvResultado.text = "Hora de inicio inválida"
                tvResultado.setTextColor(getColor(android.R.color.holo_red_light))
                return@setOnClickListener
            }

            val fin = try {
                java.time.LocalTime.parse(horaFin)
            } catch (e: Exception) {
                tvResultado.text = "Hora de fin inválida"
                tvResultado.setTextColor(getColor(android.R.color.holo_red_light))
                return@setOnClickListener
            }

            if (!fin.isAfter(inicio)) {
                tvResultado.text = "La hora de fin debe ser mayor que la hora de inicio"
                tvResultado.setTextColor(getColor(android.R.color.holo_red_light))
                return@setOnClickListener
            }

            val disponibilidades = mutableListOf<DisponibilidadRequest>()

            if (cbLunes.isChecked)
                disponibilidades.add(DisponibilidadRequest("MONDAY", horaInicio, horaFin))
            if (cbMartes.isChecked)
                disponibilidades.add(DisponibilidadRequest("TUESDAY", horaInicio, horaFin))
            if (cbMiercoles.isChecked)
                disponibilidades.add(DisponibilidadRequest("WEDNESDAY", horaInicio, horaFin))
            if (cbJueves.isChecked)
                disponibilidades.add(DisponibilidadRequest("THURSDAY", horaInicio, horaFin))
            if (cbViernes.isChecked)
                disponibilidades.add(DisponibilidadRequest("FRIDAY", horaInicio, horaFin))
            if (cbSabado.isChecked)
                disponibilidades.add(DisponibilidadRequest("SATURDAY", horaInicio, horaFin))
            if (cbDomingo.isChecked)
                disponibilidades.add(DisponibilidadRequest("SUNDAY", horaInicio, horaFin))

            if (disponibilidades.isEmpty()) {
                tvResultado.text = "Selecciona al menos un día disponible"
                tvResultado.setTextColor(getColor(android.R.color.holo_red_light))
                return@setOnClickListener
            }

            btnRegistrar.isEnabled = false
            btnRegistrar.text = "Registrando..."

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.instance.registrarTarotista(
                        RegistroTarotistaRequest(
                            nombre,
                            email,
                            password,
                            nombrePro,
                            descripcion,
                            precio,
                            especialidades,
                            disponibilidades
                        )
                    )
                    if (response.isSuccessful) {
                        tvResultado.text = "✅ Registro exitoso. Tu cuenta está pendiente de aprobación."
                        tvResultado.setTextColor(getColor(android.R.color.holo_green_light))
                        btnRegistrar.text = "Registrado"
                    } else {
                        tvResultado.text = "❌ El email ya está registrado"
                        tvResultado.setTextColor(getColor(android.R.color.holo_red_light))
                        btnRegistrar.isEnabled = true
                        btnRegistrar.text = "Registrarme como Tarotista"
                    }
                } catch (e: Exception) {
                    tvResultado.text = "❌ Error de conexión"
                    tvResultado.setTextColor(getColor(android.R.color.holo_red_light))
                    btnRegistrar.isEnabled = true
                    btnRegistrar.text = "Registrarme como Tarotista"
                }
            }
        }
    }

    private fun cargarEspecialidades(
        llEspecialidades: LinearLayout,
        tvCargando: TextView,
        tvResultado: TextView,
        btnRegistrar: Button
    ) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getEspecialidades()

                if (response.isSuccessful && response.body()?.data != null) {
                    val especialidades = response.body()!!.data!!

                    llEspecialidades.removeAllViews()
                    checkboxesEspecialidades.clear()

                    for (esp in especialidades) {
                        val checkBox = CheckBox(this@RegistroTarotistaActivity).apply {
                            text = esp.nombre
                            setTextColor(getColor(android.R.color.white))
                            tag = esp.id
                        }
                        checkboxesEspecialidades.add(checkBox)
                        llEspecialidades.addView(checkBox)
                    }

                    tvCargando.visibility = View.GONE
                    especialidadesCargadas = true

                    if (especialidades.isEmpty()) {
                        tvResultado.text = "No hay especialidades disponibles"
                        tvResultado.setTextColor(getColor(android.R.color.holo_red_light))
                        btnRegistrar.isEnabled = false
                    }
                } else {
                    tvCargando.text = "Error al cargar especialidades"
                    tvResultado.text = "No se pudieron cargar las especialidades"
                    tvResultado.setTextColor(getColor(android.R.color.holo_red_light))
                    btnRegistrar.isEnabled = false
                }
            } catch (e: Exception) {
                tvCargando.text = "Error de conexión"
                tvResultado.text = "No se pudieron cargar las especialidades"
                tvResultado.setTextColor(getColor(android.R.color.holo_red_light))
                btnRegistrar.isEnabled = false
            }
        }
    }
}
