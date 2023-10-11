package co.edu.usbcali.arquitectura.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.usbcali.arquitectura.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RetiroDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RetiroDTO.class);
        RetiroDTO retiroDTO1 = new RetiroDTO();
        retiroDTO1.setId(1L);
        RetiroDTO retiroDTO2 = new RetiroDTO();
        assertThat(retiroDTO1).isNotEqualTo(retiroDTO2);
        retiroDTO2.setId(retiroDTO1.getId());
        assertThat(retiroDTO1).isEqualTo(retiroDTO2);
        retiroDTO2.setId(2L);
        assertThat(retiroDTO1).isNotEqualTo(retiroDTO2);
        retiroDTO1.setId(null);
        assertThat(retiroDTO1).isNotEqualTo(retiroDTO2);
    }
}
