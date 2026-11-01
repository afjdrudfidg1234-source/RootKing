package com.rootking.toolkit

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {

    private val bg = Color.rgb(8, 13, 22)
    private val card = Color.rgb(17, 26, 39)
    private val green = Color.rgb(57, 245, 145)
    private val cyan = Color.rgb(48, 215, 235)
    private val white = Color.rgb(235, 242, 250)
    private lateinit var content: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        window.decorView.systemUiVisibility = 0
        showDashboard()
    }

    private fun text(
        value: String,
        size: Float = 16f,
        color: Int = white,
        bold: Boolean = false
    ): TextView {
        return TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            if (bold) setTypeface(null, Typeface.BOLD)
            setPadding(0, 8, 0, 8)
        }
    }

    private fun button(label: String, action: () -> Unit) {
        val b = Button(this).apply {
            text = label
            textSize = 14f
            setTextColor(Color.BLACK)
            setBackgroundColor(green)
            isAllCaps = false
            setOnClickListener { action() }
        }
        content.addView(b, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 54
        ).apply { setMargins(0, 8, 0, 8) })
    }

    private fun section(title: String, detail: String) {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18, 12, 18, 12)
            setBackgroundColor(card)
        }
        box.addView(text(title, 17f, green, true))
        box.addView(text(detail, 14f, white))
        content.addView(box, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(0, 8, 0, 8) })
    }

    private fun base(title: String, subtitle: String) {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
            setPadding(20, 18, 20, 16)
        }

        root.addView(text("ROOTKING", 13f, cyan, true))
        root.addView(text(title, 27f, white, true))
        root.addView(text(subtitle, 14f, cyan))

        val scroll = ScrollView(this)
        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
        ))

        val nav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }

        listOf("Home", "Device", "Root", "Logs", "About").forEach { name ->
            val b = Button(this).apply {
                text = name
                textSize = 10f
                isAllCaps = false
                setTextColor(green)
                setBackgroundColor(card)
                setOnClickListener {
                    when (name) {
                        "Home" -> showDashboard()
                        "Device" -> showDevice()
                        "Root" -> showRoot()
                        "Logs" -> showLogs()
                        "About" -> showAbout()
                    }
                }
            }
            nav.addView(b, LinearLayout.LayoutParams(0, 48, 1f))
        }

        root.addView(nav)
        setContentView(root)
    }

    private fun showDashboard() {
        base("Root Dashboard", "DEVICE ROOT TOOLKIT")
        section("ROOT STATUS", "Not rooted / Not verified")
        section("DEVICE", "${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}")
        section("ANDROID VERSION", android.os.Build.VERSION.RELEASE)
        section("BUILD FINGERPRINT", android.os.Build.FINGERPRINT)
        button("CHECK ROOT STATUS") {
            showRoot()
        }
        button("DEVICE INFORMATION") {
            showDevice()
        }
        section("NOTICE", "Rooting is device-specific. This app does not claim root access unless it is actually verified.")
    }

    private fun showDevice() {
        base("Device Information", "DEVICE DETAILS")
        section("MANUFACTURER", android.os.Build.MANUFACTURER)
        section("MODEL", android.os.Build.MODEL)
        section("DEVICE", android.os.Build.DEVICE)
        section("PRODUCT", android.os.Build.PRODUCT)
        section("ANDROID", android.os.Build.VERSION.RELEASE)
        section("SDK VERSION", android.os.Build.VERSION.SDK_INT.toString())
        section("BUILD ID", android.os.Build.ID)
        section("FINGERPRINT", android.os.Build.FINGERPRINT)
    }

    private fun showRoot() {
        base("Root Status", "ROOT CHECK")
        section("CURRENT STATUS", "Checking is not the same as rooting.")
        button("VERIFY ROOT ACCESS") {
            val process = try {
                Runtime.getRuntime().exec(arrayOf("su", "-c", "id"))
            } catch (_: Exception) {
                null
            }

            val result = if (process == null) {
                "Root access unavailable or not granted."
            } else {
                try {
                    val output = process.inputStream.bufferedReader().readText()
                    val code = process.waitFor()
                    if (code == 0 && output.contains("uid=0")) {
                        "Root access verified: uid=0"
                    } else {
                        "Root access not verified."
                    }
                } catch (_: Exception) {
                    "Root access could not be verified."
                }
            }

            base("Root Check Result", "DEVICE ACCESS")
            section("RESULT", result)
            button("BACK TO DASHBOARD") { showDashboard() }
        }
        section("IMPORTANT", "This check only verifies existing root access. It does not unlock the bootloader or root the phone.")
    }

    private fun showLogs() {
        base("Activity Logs", "DIAGNOSTICS")
        section("APP STARTED", "RootKing interface loaded.")
        section("ROOT OPERATIONS", "No rooting operation has been performed.")
        section("SAFETY", "No device changes are made by this screen.")
    }

    private fun showAbout() {
        base("About RootKing", "ANDROID TOOLKIT")
        section("VERSION", "1.0")
        section("PURPOSE", "Device information and root-status verification.")
        section("COMPATIBILITY", "Rooting support depends on device firmware and bootloader status.")
        section("SAFETY", "Back up your data before attempting any bootloader or firmware changes.")
    }
}
