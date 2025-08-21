package br.com.fiap.tech_challenge_4a_fase_cliente.core.usecases.cliente;

import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.cliente.Cliente;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.exception.CpfJaCadastradoException;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.gateways.ClienteGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CriarClienteUseCase {

    private final ClienteGateway clienteGateway;

    public Cliente executar(Cliente cliente) {

        Optional<Cliente> clienteCpf = clienteGateway.buscarPorCpf(cliente.getCpf().getDocument());
        if (clienteCpf.isPresent()) {
            throw new CpfJaCadastradoException(cliente.getCpf().getDocument());
        }
        return clienteGateway.salvar(cliente);
    }
}
