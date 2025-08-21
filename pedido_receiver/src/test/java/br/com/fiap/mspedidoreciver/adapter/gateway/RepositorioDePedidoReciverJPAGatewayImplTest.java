package br.com.fiap.mspedidoreciver.adapter.gateway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.fiap.mspedidoreciver.adapter.mapper.PedidoMapper;
import br.com.fiap.mspedidoreciver.adapter.persistence.entity.PedidoEntity;
import br.com.fiap.mspedidoreciver.adapter.persistence.repository.PedidoReciverJpaRepository;
import br.com.fiap.mspedidoreciver.core.domain.Pedido;

class RepositorioDePedidoReciverJPAGatewayImplTest {

    @Mock
    private PedidoReciverJpaRepository pedidoReciverRepository;

    @Mock
    private PedidoMapper pedidoMapper;

    @InjectMocks
    private RepositorioDePedidoReciverJPAGatewayImpl gateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveSalvarPedidoERetornarDominio() {
        // Arrange
        Pedido pedidoInput = new Pedido();
        PedidoEntity pedidoEntity = new PedidoEntity();
        PedidoEntity pedidoSalvo = new PedidoEntity();
        Pedido pedidoRetorno = new Pedido();

        when(pedidoMapper.toPedidoEntity(pedidoInput)).thenReturn(pedidoEntity);
        when(pedidoReciverRepository.save(pedidoEntity)).thenReturn(pedidoSalvo);
        when(pedidoMapper.toPedidoDomain(pedidoSalvo)).thenReturn(pedidoRetorno);

        // Act
        Pedido resultado = gateway.processarPedidoReciver(pedidoInput);

        // Assert
        assertEquals(pedidoRetorno, resultado);
        verify(pedidoMapper).toPedidoEntity(pedidoInput);
        verify(pedidoReciverRepository).save(pedidoEntity);
        verify(pedidoMapper).toPedidoDomain(pedidoSalvo);
    }
}
