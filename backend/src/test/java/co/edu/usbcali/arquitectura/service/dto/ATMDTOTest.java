package co.edu.usbcali.arquitectura.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.usbcali.arquitectura.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ATMDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ATMDTO.class);
        ATMDTO aTMDTO1 = new ATMDTO();
        aTMDTO1.setId(1L);
        ATMDTO aTMDTO2 = new ATMDTO();
        assertThat(aTMDTO1).isNotEqualTo(aTMDTO2);
        aTMDTO2.setId(aTMDTO1.getId());
        assertThat(aTMDTO1).isEqualTo(aTMDTO2);
        aTMDTO2.setId(2L);
        assertThat(aTMDTO1).isNotEqualTo(aTMDTO2);
        aTMDTO1.setId(null);
        assertThat(aTMDTO1).isNotEqualTo(aTMDTO2);
    }
}
