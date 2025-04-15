package yahor.backend.project_analysis.spring_boot_project.database.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(
    @Value("\${jwt.secret}") private val secretKeyBase64: String,
) {

    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKeyBase64)) //create secret key from base64 string
    private val accessTokenValidityMs = 15L * 60L * 1000L // 15 minutes
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
    fun validateAccessToken(token: String): Boolean {
        val claims = parseAllClaims(token)?: return false
        val tokenType = claims["type"] as? String?: return false
        return tokenType == "access"
    }

    fun validateRefreshToken(token: String): Boolean {
        val claims = parseAllClaims(token)?: return false
        val tokenType = claims["type"] as? String?: return false
        return tokenType == "refresh"
    }

    //Bearer <token>
    fun getUserIdFromToken(token: String) :String{
        val rawToken = if(token.startsWith("Bearer ")){
            token.removePrefix("Bearer ")
        } else token
            val claims = parseAllClaims(rawToken)?: throw IllegalArgumentException("Invalid token")
            return claims.subject

    }

    private fun parseAllClaims(token: String): Claims? {
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: Exception) {
            null
        }
    }




}