package com.micauditor.app

import android.Manifest
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

data class MicApp(
    val label: String,
    val packageName: String,
    val icon: android.graphics.drawable.Drawable,
    val isSystemApp: Boolean
)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val emptyState = findViewById<View>(R.id.emptyState)
        val countText = findViewById<TextView>(R.id.countText)

        recyclerView.layoutManager = LinearLayoutManager(this)

        val micApps = findAppsWithMicPermission()

        countText.text = "${micApps.size} apps"

        if (micApps.isEmpty()) {
            emptyState.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            recyclerView.adapter = MicAppAdapter(micApps) { app ->
                openAppSettings(app.packageName)
            }
        }
    }

    /**
     * Scans every installed app and checks, via the public PackageManager API,
     * whether it currently holds the RECORD_AUDIO permission.
     * This does NOT require any special/system permission - PackageManager
     * exposes permission grant-state for any package to any app.
     */
    private fun findAppsWithMicPermission(): List<MicApp> {
        val pm = packageManager
        val result = mutableListOf<MicApp>()

        val installedApps: List<ApplicationInfo> = try {
            pm.getInstalledApplications(PackageManager.GET_META_DATA)
        } catch (e: Exception) {
            emptyList()
        }

        for (appInfo in installedApps) {
            // Skip our own app
            if (appInfo.packageName == packageName) continue

            val granted = pm.checkPermission(
                Manifest.permission.RECORD_AUDIO,
                appInfo.packageName
            ) == PackageManager.PERMISSION_GRANTED

            if (granted) {
                val isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                try {
                    result.add(
                        MicApp(
                            label = pm.getApplicationLabel(appInfo).toString(),
                            packageName = appInfo.packageName,
                            icon = pm.getApplicationIcon(appInfo),
                            isSystemApp = isSystemApp
                        )
                    )
                } catch (e: Exception) {
                    // Skip apps whose icon/label can't be resolved
                }
            }
        }

        // User-installed apps first, then system apps, alphabetically within each group
        return result.sortedWith(compareBy({ it.isSystemApp }, { it.label.lowercase() }))
    }

    private fun openAppSettings(packageName: String) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }
}
