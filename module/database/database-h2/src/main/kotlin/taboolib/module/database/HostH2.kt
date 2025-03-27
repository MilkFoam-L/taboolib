package taboolib.module.database

import java.io.File

data class HostH2(val file: File) : Host<SQL>() {

    override val columnBuilder: ColumnBuilder
        get() = SQL()

    override val connectionUrl: String
        get() = "jdbc:h2:${file.absolutePath};MODE=MySQL"

    override val connectionUrlSimple: String
        get() = "jdbc:h2:${file.absolutePath};MODE=MySQL"
    override val driverClass: String
        get() = "org.h2.Driver"

    override fun toString(): String {
        return "HostH2(file=$file, connectionUrl='$connectionUrl', connectionUrlSimple='$connectionUrlSimple')"
    }
}