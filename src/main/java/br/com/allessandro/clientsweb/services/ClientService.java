package br.com.allessandro.clientsweb.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.allessandro.clientsweb.dto.ClientDTO;
import br.com.allessandro.clientsweb.entities.Client;
import br.com.allessandro.clientsweb.repository.ClientRepository;
import br.com.allessandro.clientsweb.services.exceptions.DatabaseException;
import br.com.allessandro.clientsweb.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
		Page<Client> list = clientRepository.findAll(pageRequest);
		Page<ClientDTO> pageClientDTO = list.map(x -> new ClientDTO(x));
		return pageClientDTO;
	}

	@Transactional(readOnly = true)
	public List<ClientDTO> findAll() {
		List<Client> list = clientRepository.findAll();
		List<ClientDTO> listDTO = list.stream().map(x -> new ClientDTO(x)).collect(Collectors.toList());
		return listDTO;
	}

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> optional = clientRepository.findById(id);
		Client client = optional.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado(a)"));

		return new ClientDTO(client);
	}

	@Transactional
	public ClientDTO insert(ClientDTO clientDTO) {
		Client client = new Client();
		copyDtoToEntity(clientDTO, client);
		client = clientRepository.save(client);
		return new ClientDTO(client);
	}

	@Transactional
	public ClientDTO update(Long id, ClientDTO clientDTO) {
		try {
			Client client = clientRepository.getOne(id);
			copyDtoToEntity(clientDTO, client);
			clientRepository.save(client);
			return new ClientDTO(client);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("ID not found " + id);
		}
	}

	public void delete(Long id) {
		try {
			clientRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("ID not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
	
	private void copyDtoToEntity(ClientDTO clientDTO, Client client) {
		client.setName(clientDTO.getName());
		client.setBirthDate(clientDTO.getBirthDate());
		client.setCpf(clientDTO.getCpf());
		client.setIncome(clientDTO.getIncome());
		client.setChildren(clientDTO.getChildren());
	}

}
