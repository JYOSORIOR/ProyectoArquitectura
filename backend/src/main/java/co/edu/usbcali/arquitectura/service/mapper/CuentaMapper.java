package co.edu.usbcali.arquitectura.service.mapper;

import co.edu.usbcali.arquitectura.domain.Cliente;
import co.edu.usbcali.arquitectura.domain.Cuenta;
import co.edu.usbcali.arquitectura.domain.Retiro;
import co.edu.usbcali.arquitectura.service.dto.ClienteDTO;
import co.edu.usbcali.arquitectura.service.dto.CuentaDTO;
import co.edu.usbcali.arquitectura.service.dto.RetiroDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cuenta} and its DTO {@link CuentaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CuentaMapper extends EntityMapper<CuentaDTO, Cuenta> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteId")
    @Mapping(target = "retiros", source = "retiros", qualifiedByName = "retiroId")
    CuentaDTO toDto(Cuenta s);

    @Named("clienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClienteDTO toDtoClienteId(Cliente cliente);

    @Named("retiroId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RetiroDTO toDtoRetiroId(Retiro retiro);
}
