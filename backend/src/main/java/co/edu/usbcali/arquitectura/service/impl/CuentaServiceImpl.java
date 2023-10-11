package co.edu.usbcali.arquitectura.service.impl;

import co.edu.usbcali.arquitectura.domain.Cuenta;
import co.edu.usbcali.arquitectura.repository.CuentaRepository;
import co.edu.usbcali.arquitectura.service.CuentaService;
import co.edu.usbcali.arquitectura.service.dto.CuentaDTO;
import co.edu.usbcali.arquitectura.service.mapper.CuentaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cuenta}.
 */
@Service
@Transactional
public class CuentaServiceImpl implements CuentaService {

    private final Logger log = LoggerFactory.getLogger(CuentaServiceImpl.class);

    private final CuentaRepository cuentaRepository;

    private final CuentaMapper cuentaMapper;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, CuentaMapper cuentaMapper) {
        this.cuentaRepository = cuentaRepository;
        this.cuentaMapper = cuentaMapper;
    }

    @Override
    public CuentaDTO save(CuentaDTO cuentaDTO) {
        log.debug("Request to save Cuenta : {}", cuentaDTO);
        Cuenta cuenta = cuentaMapper.toEntity(cuentaDTO);
        cuenta = cuentaRepository.save(cuenta);
        return cuentaMapper.toDto(cuenta);
    }

    @Override
    public CuentaDTO update(CuentaDTO cuentaDTO) {
        log.debug("Request to update Cuenta : {}", cuentaDTO);
        Cuenta cuenta = cuentaMapper.toEntity(cuentaDTO);
        cuenta = cuentaRepository.save(cuenta);
        return cuentaMapper.toDto(cuenta);
    }

    @Override
    public Optional<CuentaDTO> partialUpdate(CuentaDTO cuentaDTO) {
        log.debug("Request to partially update Cuenta : {}", cuentaDTO);

        return cuentaRepository
            .findById(cuentaDTO.getId())
            .map(existingCuenta -> {
                cuentaMapper.partialUpdate(existingCuenta, cuentaDTO);

                return existingCuenta;
            })
            .map(cuentaRepository::save)
            .map(cuentaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CuentaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cuentas");
        return cuentaRepository.findAll(pageable).map(cuentaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CuentaDTO> findOne(Long id) {
        log.debug("Request to get Cuenta : {}", id);
        return cuentaRepository.findById(id).map(cuentaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cuenta : {}", id);
        cuentaRepository.deleteById(id);
    }
}
