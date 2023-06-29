package cl.awakelab.apirest.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;


@Configuration  //* Anotación para indicar que esta clase provee beans de configuración para Spring.
@AllArgsConstructor
public class WebSecurityConfig  {

  private final UserDetailsService userDetailService;
  // private final JWTAuthenticationFilter jwtAuthenticationFilter;
  private final JWTAuthorizationFilter jwtAuthorizationFilter;
  @Bean  //* Anotación para declarar un bean que será administrado por el contenedor de Spring.
  //* Este método configura la seguridad web de Spring.
  SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception{

    JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter();
    jwtAuthenticationFilter.setAuthenticationManager(authManager);
    jwtAuthenticationFilter.setFilterProcessesUrl("/login"); // establcer la ruta de autentificacion

    return http
            .csrf().disable()  //* Desactiva la protección contra ataques CSRF ya que nuestra API es stateless.
            .authorizeRequests()  //* Permite restringir el acceso basado en el HttpServletRequest.
            .anyRequest()  //* Aplica las restricciones a todas las solicitudes.
            .authenticated()  //* Requiere que cualquier solicitud esté autenticada.
            .and()
            .sessionManagement()  //* Gestiona la creación de sesiones.
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  //* Indica que no se debe crear una sesión.
            .and()
            .addFilter(jwtAuthenticationFilter)
            .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();  //* Construye el objeto SecurityFilterChain.
  }


  @Bean
  //* Este método proporciona el gestor de autenticación para Spring Security.
  AuthenticationManager authManager(HttpSecurity http) throws Exception{
    return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailService)  //* Establece el servicio de detalles del usuario.
            .passwordEncoder(passwordEncoder()).and().build(); //* Configura el codificador de contraseñas y construye el AuthenticationManager.
  }

  @Bean
  //* Este método proporciona el codificador de contraseñas para Spring Security.
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();  //* Usa BCrypt para encriptar las contraseñas.
  }
}