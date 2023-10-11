package co.edu.usbcali.arquitectura.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ATMMapperTest {

    private ATMMapper aTMMapper;

    @BeforeEach
    public void setUp() {
        aTMMapper = new ATMMapperImpl();
    }
}
