package co.edu.usbcali.arquitectura.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RetiroMapperTest {

    private RetiroMapper retiroMapper;

    @BeforeEach
    public void setUp() {
        retiroMapper = new RetiroMapperImpl();
    }
}
