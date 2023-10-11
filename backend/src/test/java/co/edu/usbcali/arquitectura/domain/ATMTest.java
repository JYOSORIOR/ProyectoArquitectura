package co.edu.usbcali.arquitectura.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.usbcali.arquitectura.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ATMTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ATM.class);
        ATM aTM1 = new ATM();
        aTM1.setId(1L);
        ATM aTM2 = new ATM();
        aTM2.setId(aTM1.getId());
        assertThat(aTM1).isEqualTo(aTM2);
        aTM2.setId(2L);
        assertThat(aTM1).isNotEqualTo(aTM2);
        aTM1.setId(null);
        assertThat(aTM1).isNotEqualTo(aTM2);
    }
}
