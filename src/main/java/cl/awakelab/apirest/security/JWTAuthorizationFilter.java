package cl.awakelab.apirest.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//* Anotación para marcar la clase como un componente Spring, para que pueda ser detectada automáticamente
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {
  
  //* Este método se invoca una vez por cada solicitud
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    //* Obtenemos el token Bearer del encabezado "Authorization" de la solicitud
    String bearerToken = request.getHeader("Authorization");

    //* Verificamos que el token exista y que comienza con "Bearer "
    if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
      
      //* Si es así, eliminamos la parte "Bearer " del token para quedarnos sólo con el JWT
      String token = bearerToken.replace("Bearer ", "");

      //* Obtenemos la autenticación a partir del token
      UsernamePasswordAuthenticationToken usernamePAT = TokenUtils.getAuthentication(token);

      //* Establecemos la autenticación en el contexto de seguridad de Spring, para que pueda ser usada posteriormente
      SecurityContextHolder.getContext().setAuthentication(usernamePAT);
    }
    
    //* Permitimos que la solicitud continúe su camino a través de la cadena de filtros
    filterChain.doFilter(request, response);
  }
}
