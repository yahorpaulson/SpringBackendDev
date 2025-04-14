package yahor.backend.project_analysis.spring_boot_project.database.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class HashEncoder {
    private val bcrypt = BCryptPasswordEncoder()

    fun encode(raw: String): String {
        return bcrypt.encode(raw)
    }

    fun matches(raw: String, hashed: String): Boolean {
        return bcrypt.matches(raw, hashed)
    }
}