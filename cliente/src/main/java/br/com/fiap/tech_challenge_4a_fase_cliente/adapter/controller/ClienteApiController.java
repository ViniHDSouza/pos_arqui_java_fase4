package br.com.fiap.tech_challenge_4a_fase_cliente.adapter.controller;

import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.controller.request.ClienteRequestDto;
import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.controller.response.ClienteResponseDto;
import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.mapper.ClienteMapper;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.domain.entities.cliente.Cliente;
import br.com.fiap.tech_challenge_4a_fase_cliente.core.usecases.cliente.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteApiController implements ClienteController{

    private final CriarClienteUseCase criarUsuarioUseCase;
    private final AtualizarClienteUseCase atualizarClienteUseCase;
    private final DeletarClienteUseCase deletarClienteUseCase;
    private final BuscarClienteUseCase buscarClientePorIdUseCase;
    private final ListarTodosClienteUseCase listarUsuariosUseCase;
    private final ExisteClienteUseCase existeClienteUseCase;

    private ClienteMapper mapper;

    public ClienteApiController(
            CriarClienteUseCase criarUsuarioUseCase,
            AtualizarClienteUseCase atualizarClienteUseCase,
            DeletarClienteUseCase deletarClienteUseCase,
            BuscarClienteUseCase buscarClientePorIdUseCase,
            ListarTodosClienteUseCase listarUsuariosUseCase, ExisteClienteUseCase existeClienteUseCase) {
        this.criarUsuarioUseCase = criarUsuarioUseCase;
        this.atualizarClienteUseCase = atualizarClienteUseCase;
        this.deletarClienteUseCase = deletarClienteUseCase;
        this.buscarClientePorIdUseCase = buscarClientePorIdUseCase;
        this.listarUsuariosUseCase = listarUsuariosUseCase;
        this.existeClienteUseCase = existeClienteUseCase;
        this.mapper = new ClienteMapper();
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDto cadastrarCliente(@RequestBody ClienteRequestDto dto) {
        Cliente usuario = mapper.toClienteDomain(dto);
        Cliente salvo = criarUsuarioUseCase.executar(usuario);
        return mapper.toClienteResponseDto(salvo);
    }

    @Override
    @GetMapping
    public List<ClienteResponseDto> listarClientes() {
        return listarUsuariosUseCase.executar().stream()
                .map(mapper::toClienteResponseDto)
                .toList();
    }

    @Override
    @GetMapping("/{id}")
    public ClienteResponseDto buscarClientePorId(@PathVariable Long id) {
        Cliente clienteOptional =  buscarClientePorIdUseCase.executar(id);
        return mapper.toClienteResponseDto(clienteOptional);
    }

    @Override
    @GetMapping("/existe/{id}")
    public boolean clienteExiste(@PathVariable Long id) {
        return existeClienteUseCase.executar(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarCliente(@PathVariable Long id) {
        deletarClienteUseCase.executar(id);
    }

    @Override
    @PutMapping("/{id}")
    public ClienteResponseDto atualizarCliente(@PathVariable Long id, @RequestBody ClienteRequestDto dto) {

        Cliente clienteDomain = mapper.toClienteDomain(dto);
        Cliente executar = atualizarClienteUseCase.executar(id,clienteDomain);
        return mapper.toClienteResponseDto(executar);

    }


}
