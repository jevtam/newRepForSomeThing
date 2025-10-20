package infrastructure.crypto
import domain.ports.Hasher
import java.security.MessageDigest

class Sha256Hasher : Hasher {
    override fun sha256Hex(s: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(s.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
}