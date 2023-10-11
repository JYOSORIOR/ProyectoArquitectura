package co.edu.usbcali.arquitectura.service.impl;

import co.edu.usbcali.arquitectura.domain.Retiro;
import co.edu.usbcali.arquitectura.repository.RetiroRepository;
import co.edu.usbcali.arquitectura.service.RetiroService;
import co.edu.usbcali.arquitectura.service.dto.RetiroDTO;
import co.edu.usbcali.arquitectura.service.mapper.RetiroMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Retiro}.
 */
@Service
@Transactional
public class RetiroServiceImpl implements RetiroService {

    private final Logger log = LoggerFactory.getLogger(RetiroServiceImpl.class);

    private final RetiroRepository retiroRepository;

    private final RetiroMapper retiroMapper;

    public RetiroServiceImpl(RetiroRepository retiroRepository, RetiroMapper retiroMapper) {
        this.retiroRepository = retiroRepository;
        this.retiroMapper = retiroMapper;
    }

    @Override
    public RetiroDTO save(RetiroDTO retiroDTO) {
        log.debug("Request to save Retiro : {}", retiroDTO);
        Retiro retiro = retiroMapper.toEntity(retiroDTO);
        retiro = retiroRepository.save(retiro);
        return retiroMapper.toDto(retiro);
    }

    @Override
    public RetiroDTO update(RetiroDTO retiroDTO) {
        log.debug("Request to update Retiro : {}", retiroDTO);
        Retiro retiro = retiroMapper.toEntity(retiroDTO);
        retiro = retiroRepository.save(retiro);
        return retiroMapper.toDto(retiro);
    }

    @Override
    public Optional<RetiroDTO> partialUpdate(RetiroDTO retiroDTO) {
        log.debug("Request to partially update Retiro : {}", retiroDTO);

        return retiroRepository
            .findById(retiroDTO.getId())
            .map(existingRetiro -> {
                retiroMapper.partialUpdate(existingRetiro, retiroDTO);

                return existingRetiro;
            })
            .map(retiroRepository::save)
            .map(retiroMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RetiroDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Retiros");
        return retiroRepository.findAll(pageable).map(retiroMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RetiroDTO> findOne(Long id) {
        log.debug("Request to get Retiro : {}", id);
        return retiroRepository.findById(id).map(retiroMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Retiro : {}", id);
        retiroRepository.deleteById(id);
    }
}
