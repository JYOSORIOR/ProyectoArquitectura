package co.edu.usbcali.arquitectura.service.mapper;

import co.edu.usbcali.arquitectura.domain.ATM;
import co.edu.usbcali.arquitectura.domain.Transaccion;
import co.edu.usbcali.arquitectura.service.dto.ATMDTO;
import co.edu.usbcali.arquitectura.service.dto.TransaccionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ATM} and its DTO {@link ATMDTO}.
 */
@Mapper(componentModel = "spring")
public interface ATMMapper extends EntityMapper<ATMDTO, ATM> {
    @Mapping(target = "transacciones", source = "transacciones", qualifiedByName = "transaccionId")
    ATMDTO toDto(ATM s);

    @Named("transaccionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransaccionDTO toDtoTransaccionId(Transaccion transaccion);
}
