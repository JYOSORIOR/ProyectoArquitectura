package co.edu.usbcali.arquitectura.service.mapper;

import co.edu.usbcali.arquitectura.domain.Cliente;
import co.edu.usbcali.arquitectura.service.dto.ClienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cliente} and its DTO {@link ClienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClienteMapper extends EntityMapper<ClienteDTO, Cliente> {}
