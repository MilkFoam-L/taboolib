package taboolib.common.io

import taboolib.common.PrimitiveIO
import taboolib.common.util.t
import java.io.File

object BinaryClass {

    fun <T> read(name: String, version: String, block: (bytes: ByteArray) -> T): T? {
        // 是否有缓存文件
        val cacheFile = getCacheFile().resolve("binary/${name}.cache")
        if (cacheFile.exists()) {
            // 检查版本
            val metaFile = getCacheFile().resolve("binary/${name}.cache.sha1")
            val sha1 = if (metaFile.exists()) metaFile.readText() else ""
            if (sha1 == version) {
                // 从缓存中读取
                try {
                    return block(cacheFile.readBytes())
                } catch (ex: Throwable) {
                    PrimitiveIO.warning(
                        """
                            无法从缓存文件 "${cacheFile.name}" 中读取类信息。
                            Failed to read class information from cache file "${cacheFile.name}".
                        """.t()
                    )
                    ex.printStackTrace()
                    drop(name)
                }
            }
        }
        return null
    }

    fun save(name: String, version: String, block: () -> ByteArray) {
        val cacheFile = getCacheFile().resolve("binary/${name}.cache")
        val metaFile = getCacheFile().resolve("binary/${name}.cache.sha1")
        try {
            newFile(cacheFile).writeBytes(block())
            newFile(metaFile).writeText(version)
        } catch (ex: Throwable) {
            PrimitiveIO.warning(
                """
                    无法将类信息写入缓存文件 "${cacheFile.name}"。
                    Failed to write class information to cache file "${cacheFile.name}".
                """.t()
            )
            ex.printStackTrace()
        }
    }

    fun drop(name: String) {
        try {
            getCacheFile().resolve("binary/${name}.cache").delete()
            getCacheFile().resolve("binary/${name}.cache.sha1").delete()
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
    }

    fun getCacheFile(): File {
        val file = File("cache/taboolib/$groupId")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }
}