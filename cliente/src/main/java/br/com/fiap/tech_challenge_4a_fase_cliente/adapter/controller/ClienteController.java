package br.com.fiap.tech_challenge_4a_fase_cliente.adapter.controller;

import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.controller.request.ClienteRequestDto;
import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.controller.response.ClienteResponseDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ClienteController {

    ClienteResponseDto cadastrarCliente(ClienteRequestDto dto);

    List<ClienteResponseDto> listarClientes();

    ClienteResponseDto buscarClientePorId(Long id);

    void deletarCliente(Long id);

    ClienteResponseDto atualizarCliente(Long id, ClienteRequestDto dto);

    boolean clienteExiste(@PathVariable Long id);
}
