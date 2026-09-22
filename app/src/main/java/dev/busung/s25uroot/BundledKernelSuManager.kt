package dev.busung.s25uroot

import android.content.Context
import java.io.File
import java.security.MessageDigest

/** Installs the official KernelSU Next Manager bundled with this offline flavor. */
object BundledKernelSuManager {
    private const val ASSET_PATH = "manager/KernelSUNextManager.apk"
    private const val FILE_NAME = "KernelSU_Next_v3.4.0_33294-release.apk"

    fun install(context: Context): Boolean = runCatching {
        val updatesDir = File(context.cacheDir, "updates").apply { mkdirs() }
        val apk = File(updatesDir, FILE_NAME)

        context.assets.open(ASSET_PATH).use { input ->
            apk.outputStream().use { output -> input.copyTo(output) }
        }

        check(apk.sha256() == BuildConfig.KSU_MANAGER_SHA256) {
            "Bundled KernelSU Next Manager integrity check failed"
        }
        check(AppUpdater.installApk(context, apk)) { "No package installer is available" }
    }.onFailure {
        File(context.cacheDir, "updates/$FILE_NAME").delete()
    }.isSuccess

    private fun File.sha256(): String {
        val digest = MessageDigest.getInstance("SHA-256")
        inputStream().use { input ->
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            while (true) {
                val read = input.read(buffer)
                if (read < 0) break
                digest.update(buffer, 0, read)
            }
        }
        return digest.digest().joinToString("") { byte -> "%02x".format(byte) }
    }
}
