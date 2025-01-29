@file:Suppress("SpellCheckingInspection")

package taboolib.common5

import java.math.BigDecimal
import java.math.RoundingMode

inline val Any?.cint: Int
    get() = Coerce.toInteger(this)

inline val Any?.cdouble: Double
    get() = Coerce.toDouble(this)

inline val Any?.cfloat: Float
    get() = Coerce.toFloat(this)

inline val Any?.clong: Long
    get() = Coerce.toLong(this)

inline val Any?.cshort: Short
    get() = Coerce.toShort(this)

inline val Any?.cbyte: Byte
    get() = Coerce.toByte(this)

inline val Any?.cchar: Char
    get() = Coerce.toChar(this)

inline val Any?.cbool: Boolean
    get() = Coerce.toBoolean(this)

fun Double.format(digits: Int = 2, roundingMode: Int = BigDecimal.ROUND_HALF_UP): Double {
    return try {
        Coerce.format(this, digits, roundingMode)
    } catch (_: Throwable) {
        0.0
    }
}

fun Float.format(digits: Int = 2, roundingMode: Int = BigDecimal.ROUND_HALF_UP): Float {
    return try {
        Coerce.format(this, digits, roundingMode)
    } catch (_: Throwable) {
        0f
    }
}

infix fun String.eqic(other: String): Boolean {
    return this.equals(other, ignoreCase = true)
}