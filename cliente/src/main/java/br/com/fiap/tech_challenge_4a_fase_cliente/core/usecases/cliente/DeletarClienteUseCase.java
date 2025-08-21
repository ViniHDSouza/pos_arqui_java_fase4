package br.com.fiap.tech_challenge_4a_fase_cliente.core.usecases.cliente;

import br.com.fiap.tech_challenge_4a_fase_cliente.core.exception.ClienteNaoEncontradoException;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.gateways.ClienteGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletarClienteUseCase {

    private final ClienteGateway clienteGateway;

    public void executar(Long id) {
        clienteGateway.buscarPorId(id).orElseThrow(() -> new ClienteNaoEncontradoException(id));
        clienteGateway.deletar(id);
    }

}
