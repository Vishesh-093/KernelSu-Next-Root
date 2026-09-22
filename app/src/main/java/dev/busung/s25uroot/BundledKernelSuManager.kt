package dev.busung.s25uroot

import android.content.Context
import java.io.File
import java.security.MessageDigest

/** Installs the official KernelSU Manager bundled with the offline APK. */
object BundledKernelSuManager {
    private const val ASSET_PATH = "manager/KernelSU_v3.3.0_32601-release.apk"
    private const val FILE_NAME = "KernelSU_v3.3.0_32601-release.apk"
    private const val SHA256 = "c197060ecb89702e7d54a4c95e29cf5e8d97369bbbb436979ab7fd6bcde7b077"

    fun install(context: Context): Boolean = runCatching {
        val updatesDir = File(context.cacheDir, "updates").apply { mkdirs() }
        val apk = File(updatesDir, FILE_NAME)

        context.assets.open(ASSET_PATH).use { input ->
            apk.outputStream().use { output -> input.copyTo(output) }
        }

        check(apk.sha256() == SHA256) { "Bundled KernelSU Manager integrity check failed" }
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
