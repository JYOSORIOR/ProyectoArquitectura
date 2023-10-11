package co.edu.usbcali.arquitectura.service.impl;

import co.edu.usbcali.arquitectura.domain.Transaccion;
import co.edu.usbcali.arquitectura.repository.TransaccionRepository;
import co.edu.usbcali.arquitectura.service.TransaccionService;
import co.edu.usbcali.arquitectura.service.dto.TransaccionDTO;
import co.edu.usbcali.arquitectura.service.mapper.TransaccionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Transaccion}.
 */
@Service
@Transactional
public class TransaccionServiceImpl implements TransaccionService {

    private final Logger log = LoggerFactory.getLogger(TransaccionServiceImpl.class);

    private final TransaccionRepository transaccionRepository;

    private final TransaccionMapper transaccionMapper;

    public TransaccionServiceImpl(TransaccionRepository transaccionRepository, TransaccionMapper transaccionMapper) {
        this.transaccionRepository = transaccionRepository;
        this.transaccionMapper = transaccionMapper;
    }

    @Override
    public TransaccionDTO save(TransaccionDTO transaccionDTO) {
        log.debug("Request to save Transaccion : {}", transaccionDTO);
        Transaccion transaccion = transaccionMapper.toEntity(transaccionDTO);
        transaccion = transaccionRepository.save(transaccion);
        return transaccionMapper.toDto(transaccion);
    }

    @Override
    public TransaccionDTO update(TransaccionDTO transaccionDTO) {
        log.debug("Request to update Transaccion : {}", transaccionDTO);
        Transaccion transaccion = transaccionMapper.toEntity(transaccionDTO);
        transaccion = transaccionRepository.save(transaccion);
        return transaccionMapper.toDto(transaccion);
    }

    @Override
    public Optional<TransaccionDTO> partialUpdate(TransaccionDTO transaccionDTO) {
        log.debug("Request to partially update Transaccion : {}", transaccionDTO);

        return transaccionRepository
            .findById(transaccionDTO.getId())
            .map(existingTransaccion -> {
                transaccionMapper.partialUpdate(existingTransaccion, transaccionDTO);

                return existingTransaccion;
            })
            .map(transaccionRepository::save)
            .map(transaccionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransaccionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Transaccions");
        return transaccionRepository.findAll(pageable).map(transaccionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransaccionDTO> findOne(Long id) {
        log.debug("Request to get Transaccion : {}", id);
        return transaccionRepository.findById(id).map(transaccionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Transaccion : {}", id);
        transaccionRepository.deleteById(id);
    }
}
