package domain.ports
interface Hasher { fun sha256Hex(s: String): String }