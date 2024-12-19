package vn.iotstar.service;

//Bước 4: Khởi tạo một Interface Repository và services
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
	@Value("${security.jwt.secret-key}")
	private String secretKey;
	@Value("${security.jwt.expiration-time}")
	private long jwtExpiration;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return buildToken(extraClaims, userDetails, jwtExpiration);
	}

	public long getExpirationTime() {
		return jwtExpiration;
	}

	private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
	    return Jwts.builder()
	            .claim("roles", userDetails.getAuthorities().stream()
	                    .map(GrantedAuthority::getAuthority)
	                    .collect(Collectors.toList()))  // Thêm các vai trò vào claim
	            .subject(userDetails.getUsername())
	            .issuedAt(new Date(System.currentTimeMillis()))
	            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 30))
	            .signWith(getSignInKey(), Jwts.SIG.HS256)
	            .compact();
	}


	
	// Phương thức trích xuất vai trò từ JWT
    public List<GrantedAuthority> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        // Giả sử rằng các vai trò được lưu dưới dạng 'roles' trong claim
        List<String> roles = (List<String>) claims.get("roles");
        return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
    }
    // Phương thức xác thực token (bao gồm cả kiểm tra vai trò)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        List<GrantedAuthority> authorities = extractAuthorities(token);

        // So sánh danh sách quyền bằng equals()
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token) &&
                authorities.equals(userDetails.getAuthorities()); 
    }

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
	}

	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}