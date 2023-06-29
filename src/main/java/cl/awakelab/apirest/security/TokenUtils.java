package cl.awakelab.apirest.security;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class TokenUtils {
  //* Llave secreta usada para firmar el JWT
  private final static String ACCESS_TOKEN_SECRET = "4qhq8LrEBfYcaRHxhbd9zURb2rf8e7Ud";
  
  //* Vida de un token - 30 días
  private final static Long ACCESS_TOKEN_VALIDITY_SECONDS = 2_592_000L; //* 2592000 

  public static String createToken(String nombre, String email) {
    //* Convertir segundos a milisegundos para la expiración del token
    long expirationTime = ACCESS_TOKEN_VALIDITY_SECONDS * 1_000;
    Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

    //* Mapa para agregar información extra al token
    Map<String, Object> extra = new HashMap<>();
    //* Agregar el nombre de usuario al token
    extra.put("nombre", nombre);

    //* Construir y retornar el token, estableciendo el email del usuario como el sujeto, la fecha de expiración, las reclamaciones extras, y firmándolo con la llave secreta
    return Jwts.builder()
            .setSubject(email)
            .setExpiration(expirationDate)
            .addClaims(extra)
            .signWith(Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes()))
            .compact();
  }

  //* Retorna un UsernamePasswordAuthenticationToken, algo válido para que Spring Security valide y de acceso al usuario
  public static UsernamePasswordAuthenticationToken getAuthentication(String token) {
    try {
      //* Descifrar el token y extraer las reclamaciones
      Claims claims = Jwts.parserBuilder()
              .setSigningKey(ACCESS_TOKEN_SECRET.getBytes())
              .build()
              .parseClaimsJws(token)
              .getBody();
      //* Obtener el email del sujeto del token
      String email = claims.getSubject();
      //* Crear y retornar un UsernamePasswordAuthenticationToken con el email del usuario
      return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
    } catch(JwtException e) {
      //* Si hay un error al procesar el token, retornar null
      return null;
    }
  }
}