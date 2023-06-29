package cl.awakelab.apirest.security;

import cl.awakelab.apirest.models.dao.IClienteDao;
import cl.awakelab.apirest.models.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service //* Anotación para indicar que esta clase es un servicio
public class UserDetailServiceImpl implements UserDetailsService {

  @Autowired //*Inyectar el repositorio de cliente
  private IClienteDao clienteDao;

  @Override //* Sobreescribir el método de la interfaz UserDetailsService
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    //* Buscar al cliente por su email. Si no existe, lanzar una excepción
    Cliente cliente = clienteDao
            .findOneByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("El usuario con email " + email + " no existe"));
    //* Retornar una nueva instancia de UserDetailsImpl con el cliente encontrado
    return new UserDetailsImpl(cliente);
  }
}
