package cl.awakelab.apirest.security;

import cl.awakelab.apirest.models.entity.Cliente;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor // Anotación de Lombok para generar un constructor con todos los atributos de la clase
public class UserDetailsImpl implements UserDetails {

  private final Cliente cliente; // @AllArgsConstructor generará un constructor que recibe un cliente

  @Override // Sobreescribir el método de la interfaz UserDetails
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList(); // Este método sería útil si tuviéramos roles
  }

  @Override // Sobreescribir el método de la interfaz UserDetails
  public String getPassword() {
    return cliente.getPassword(); // Devuelve la contraseña del cliente
  }

  @Override // Sobreescribir el método de la interfaz UserDetails
  public String getUsername() {
    return cliente.getEmail(); // Devuelve el email del cliente
  }

  @Override // Sobreescribir el método de la interfaz UserDetails
  public boolean isAccountNonExpired() {
    return true; // Devuelve si la cuenta no ha expirado
  }

  @Override // Sobreescribir el método de la interfaz UserDetails
  public boolean isAccountNonLocked() {
    return true; // Devuelve si la cuenta no está bloqueada
  }

  @Override // Sobreescribir el método de la interfaz UserDetails
  public boolean isCredentialsNonExpired() {
    return true; // Devuelve si las credenciales no han expirado
  }

  @Override // Sobreescribir el método de la interfaz UserDetails
  public boolean isEnabled() {
    return true; // Devuelve si el usuario está habilitado
  }

  // Método personalizado para devolver el nombre del cliente
  public String getNombre() {
    return cliente.getNombre();
  }
}
