package co.edu.usbcali.arquitectura.service.impl;

import co.edu.usbcali.arquitectura.domain.ATM;
import co.edu.usbcali.arquitectura.repository.ATMRepository;
import co.edu.usbcali.arquitectura.service.ATMService;
import co.edu.usbcali.arquitectura.service.dto.ATMDTO;
import co.edu.usbcali.arquitectura.service.mapper.ATMMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ATM}.
 */
@Service
@Transactional
public class ATMServiceImpl implements ATMService {

    private final Logger log = LoggerFactory.getLogger(ATMServiceImpl.class);

    private final ATMRepository aTMRepository;

    private final ATMMapper aTMMapper;

    public ATMServiceImpl(ATMRepository aTMRepository, ATMMapper aTMMapper) {
        this.aTMRepository = aTMRepository;
        this.aTMMapper = aTMMapper;
    }

    @Override
    public ATMDTO save(ATMDTO aTMDTO) {
        log.debug("Request to save ATM : {}", aTMDTO);
        ATM aTM = aTMMapper.toEntity(aTMDTO);
        aTM = aTMRepository.save(aTM);
        return aTMMapper.toDto(aTM);
    }

    @Override
    public ATMDTO update(ATMDTO aTMDTO) {
        log.debug("Request to update ATM : {}", aTMDTO);
        ATM aTM = aTMMapper.toEntity(aTMDTO);
        aTM = aTMRepository.save(aTM);
        return aTMMapper.toDto(aTM);
    }

    @Override
    public Optional<ATMDTO> partialUpdate(ATMDTO aTMDTO) {
        log.debug("Request to partially update ATM : {}", aTMDTO);

        return aTMRepository
            .findById(aTMDTO.getId())
            .map(existingATM -> {
                aTMMapper.partialUpdate(existingATM, aTMDTO);

                return existingATM;
            })
            .map(aTMRepository::save)
            .map(aTMMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ATMDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ATMS");
        return aTMRepository.findAll(pageable).map(aTMMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ATMDTO> findOne(Long id) {
        log.debug("Request to get ATM : {}", id);
        return aTMRepository.findById(id).map(aTMMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ATM : {}", id);
        aTMRepository.deleteById(id);
    }
}
