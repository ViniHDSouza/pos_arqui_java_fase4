package br.com.fiap.tech_challenge_4a_fase_cliente.adapter.persistence.repository;

import br.com.fiap.tech_challenge_4a_fase_cliente.adapter.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
    Optional<ClienteEntity> findByCpf(String cpf);
}
