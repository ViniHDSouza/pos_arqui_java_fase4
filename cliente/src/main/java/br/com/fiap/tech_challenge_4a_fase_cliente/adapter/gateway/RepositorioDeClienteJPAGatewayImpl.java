package br.com.fiap.tech_challenge_4a_fase_cliente.adapter.gateway;

import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.mapper.ClienteMapper;
import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.persistence.entity.ClienteEntity;
import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.persistence.repository.ClienteRepository;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.cliente.Cliente;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.gateways.ClienteGateway;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RepositorioDeClienteJPAGatewayImpl implements ClienteGateway {

    private final ClienteRepository clienteRepository;
    private ClienteMapper mapper;

    public RepositorioDeClienteJPAGatewayImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
        mapper = new ClienteMapper();
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        ClienteEntity entity = mapper.toClienteEntity(cliente);
        clienteRepository.save(entity);
        return mapper.toClientDomain(entity);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        Optional<ClienteEntity> clienteEntity = clienteRepository.findById(id);
        return clienteEntity.map(entity -> mapper.toClientDomain(entity));
    }

    @Override
    public Optional<Cliente> buscarPorCpf(String cpf) {
        Optional<ClienteEntity> clienteEntity = clienteRepository.findByCpf(cpf);
        return clienteEntity.map( e-> mapper.toClientDomain(e));
    }

    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public Cliente atualizar(Long id, Cliente cliente) {
        cliente.setId(id);
        ClienteEntity entity = mapper.toClienteEntity(cliente);
        clienteRepository.save(entity);
        return mapper.toClientDomain(entity);
    }

    @Override
    public List<Cliente> listarTodos() {
        return clienteRepository
                .findAll()
                .stream()
                .map(entity -> mapper.toClientDomain(entity))
                .toList();
    }

    @Override
    public boolean clienteExiste(Long id) {
        Optional<ClienteEntity> clienteEntity = clienteRepository.findById(id);
        if (clienteEntity.isPresent()) {
            return true;
        }
        return false;
    }
}

