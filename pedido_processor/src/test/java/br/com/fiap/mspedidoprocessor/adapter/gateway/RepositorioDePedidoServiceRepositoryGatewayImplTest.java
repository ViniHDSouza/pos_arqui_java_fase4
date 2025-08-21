package br.com.fiap.mspedidoprocessor.adapter.gateway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.fiap.mspedidoprocessor.adapter.mapper.PedidoProcessorMapper;
import br.com.fiap.mspedidoprocessor.adapter.persistence.entity.PedidoProcessorEntity;
import br.com.fiap.mspedidoprocessor.adapter.persistence.repository.PedidoRepositoryJpa;
import br.com.fiap.mspedidoprocessor.core.domain.PagamentoCallback;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoProcessor;

class RepositorioDePedidoServiceRepositoryGatewayImplTest {

    @Mock
    private PedidoRepositoryJpa pedidoRepositoryJpa;

    @Mock
    private PedidoProcessorMapper pedidoProcessorMapper;

    @InjectMocks
    private RepositorioDePedidoServiceRepositoryGatewayImpl gateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveSalvarPedidoComNovoUUIDQuandoNaoInformado() {
        PedidoProcessor pedido = new PedidoProcessor();
        PedidoProcessorEntity entity = new PedidoProcessorEntity();
        PedidoProcessorEntity entitySalvo = new PedidoProcessorEntity();
        PedidoProcessor pedidoRetornado = new PedidoProcessor();

        when(pedidoProcessorMapper.toEntity(any())).thenReturn(entity);
        when(pedidoRepositoryJpa.save(entity)).thenReturn(entitySalvo);
        when(pedidoProcessorMapper.toModel(entitySalvo)).thenReturn(pedidoRetornado);

        PedidoProcessor salvo = gateway.salvar(pedido);

        assertNotNull(salvo);
        assertNotNull(pedido.getId()); // Verifica se UUID foi atribuído
        verify(pedidoRepositoryJpa).save(entity);
    }

    @Test
    void deveConsultarPagamentoCallbackQuandoPedidoExiste() {
        Long pedidoId = 123L;
        PedidoProcessorEntity entity = new PedidoProcessorEntity();
        PagamentoCallback callbackEsperado = new PagamentoCallback(pedidoId, pedidoId, null); // ajuste conforme necessário

        when(pedidoRepositoryJpa.findByPedidoReciverId(pedidoId)).thenReturn(Optional.of(entity));
        when(pedidoProcessorMapper.toPagamentoCallback(entity)).thenReturn(callbackEsperado);

        PagamentoCallback callback = gateway.consultarPagamentoCallback(pedidoId);

        assertNotNull(callback);
        assertEquals(callbackEsperado.getPedidoId(), callback.getPedidoId());
    }

    @Test
    void deveRetornarNullQuandoPedidoNaoExiste() {
        Long pedidoId = 999L;

        when(pedidoRepositoryJpa.findByPedidoReciverId(pedidoId)).thenReturn(Optional.empty());

        PagamentoCallback callback = gateway.consultarPagamentoCallback(pedidoId);

        assertNull(callback);
    }
}
