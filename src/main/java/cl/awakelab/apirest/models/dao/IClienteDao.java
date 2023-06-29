package cl.awakelab.apirest.models.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import cl.awakelab.apirest.models.entity.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long> {
  // query

  Optional<Cliente> findOneByEmail(String email);
}
