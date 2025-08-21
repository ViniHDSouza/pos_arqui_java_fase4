package br.com.fiap.tech_challenge_4a_fase_cliente.core.usecases.cliente;

import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.cliente.Cliente;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.exception.ClienteNaoEncontradoException;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.gateways.ClienteGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarClienteUseCase {

    private final ClienteGateway clienteGateway;

    public Cliente executar(Long id, Cliente cliente) {
        clienteGateway
                .buscarPorId(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));

        return clienteGateway.atualizar(id, cliente);
    }

}
