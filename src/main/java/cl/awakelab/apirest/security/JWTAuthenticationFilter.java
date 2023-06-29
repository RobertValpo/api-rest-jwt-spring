package cl.awakelab.apirest.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  //* Este método intenta autenticar al usuario con las credenciales proporcionadas
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    
    //* Se crea una instancia vacía de AuthCredentials para almacenar las credenciales del usuario
    AuthCredentials authCredentials = new AuthCredentials();
    try {
      //* Se intenta leer las credenciales del cuerpo de la solicitud HTTP y se almacenan en authCredentials
      authCredentials = new ObjectMapper().readValue(request.getReader(), AuthCredentials.class);
    } catch(IOException e) {
      //* En caso de error en la lectura, se atrapa la excepción
      System.out.println(e.getMessage());
    }

    //* Se crea un token de autenticación a partir de las credenciales
    UsernamePasswordAuthenticationToken usernamePAT = new UsernamePasswordAuthenticationToken(
            authCredentials.getEmail(),
            authCredentials.getPassword(),
            Collections.emptyList()
    );

    //* Se intenta autenticar el token
    return getAuthenticationManager().authenticate(usernamePAT);
  }

  //* Este método se ejecuta si la autenticación es exitosa
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    
    //* Se obtienen los detalles del usuario autenticado
    UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
    
    //* Se crea un token JWT
    String token = TokenUtils.createToken(userDetails.getNombre(), userDetails.getUsername());
    
    //* Se añade el token JWT al encabezado de la respuesta
    response.addHeader("Authorization", "Bearer " + token);
    
    //* Se vacía el buffer de escritura de la respuesta
    response.getWriter().flush();
    
    //* Se llama al método original con la solicitud y la respuesta modificadas
    super.successfulAuthentication(request, response, chain, authResult);
  }
}
