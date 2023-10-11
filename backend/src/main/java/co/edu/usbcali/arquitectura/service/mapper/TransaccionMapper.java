package co.edu.usbcali.arquitectura.service.mapper;

import co.edu.usbcali.arquitectura.domain.Cuenta;
import co.edu.usbcali.arquitectura.domain.Transaccion;
import co.edu.usbcali.arquitectura.service.dto.CuentaDTO;
import co.edu.usbcali.arquitectura.service.dto.TransaccionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transaccion} and its DTO {@link TransaccionDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransaccionMapper extends EntityMapper<TransaccionDTO, Transaccion> {
    @Mapping(target = "cuenta", source = "cuenta", qualifiedByName = "cuentaId")
    TransaccionDTO toDto(Transaccion s);

    @Named("cuentaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CuentaDTO toDtoCuentaId(Cuenta cuenta);
}
