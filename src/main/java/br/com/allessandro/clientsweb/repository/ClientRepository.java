package br.com.allessandro.clientsweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.allessandro.clientsweb.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

}
