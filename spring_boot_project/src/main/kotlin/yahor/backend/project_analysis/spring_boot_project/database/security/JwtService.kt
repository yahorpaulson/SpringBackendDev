package yahor.backend.project_analysis.spring_boot_project.database.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(
    @Value("JWT_SECRET_KEY_BASE64") private val secretKeyBase64: String,
) {

    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKeyBase64))
    private val accessTokenValidityMs = 15L * 60L + 1000L // 15 minutes
    val refreshTokenValidityMs = 30L * 24L * 60L * 60L + 1000L // 30 days


    private fun generateToken(
        userId : String,
        type: String,
        expiry: Long
    ): String {
        val now = Date()
        val expiryDate = Date(now.time + expiry)
        return Jwts.builder()
            .subject(userId)
            .claim("type", type)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }
    fun generateAccessToken(userId: String): String {
        return generateToken(
            userId = userId,
            type = "access",
            expiry = accessTokenValidityMs
        )
    }

    fun generateRefreshToken(userId: String): String {
        return generateToken(
            userId = userId,
            type = "refresh",
            expiry = refreshTokenValidityMs
        )
    }
}