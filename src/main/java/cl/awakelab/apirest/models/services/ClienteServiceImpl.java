package cl.awakelab.apirest.models.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.awakelab.apirest.models.dao.IClienteDao;
import cl.awakelab.apirest.models.entity.Cliente;

@Service
public class ClienteServiceImpl implements IClienteService {
  
  @Autowired
  private IClienteDao clienteDao;

  @Override
  @Transactional(readOnly = true)
  public List<Cliente> findAll() {
    return (List<Cliente>) clienteDao.findAll();
  }

  @Override
  public Optional<Cliente> findById(Long id) {
    return clienteDao.findById(id);
  }

  @Override
  public Cliente save(Cliente cliente) {
    return clienteDao.save(cliente);
  }

  @Override
  public void delete(Long id) {

    clienteDao.deleteById(id);
    
  }

}
