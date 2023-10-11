package co.edu.usbcali.arquitectura.service.mapper;

import co.edu.usbcali.arquitectura.domain.ATM;
import co.edu.usbcali.arquitectura.domain.Retiro;
import co.edu.usbcali.arquitectura.service.dto.ATMDTO;
import co.edu.usbcali.arquitectura.service.dto.RetiroDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Retiro} and its DTO {@link RetiroDTO}.
 */
@Mapper(componentModel = "spring")
public interface RetiroMapper extends EntityMapper<RetiroDTO, Retiro> {
    @Mapping(target = "atm", source = "atm", qualifiedByName = "aTMId")
    RetiroDTO toDto(Retiro s);

    @Named("aTMId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ATMDTO toDtoATMId(ATM aTM);
}
