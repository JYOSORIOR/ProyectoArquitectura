package co.edu.usbcali.arquitectura.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.edu.usbcali.arquitectura.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RetiroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Retiro.class);
        Retiro retiro1 = new Retiro();
        retiro1.setId(1L);
        Retiro retiro2 = new Retiro();
        retiro2.setId(retiro1.getId());
        assertThat(retiro1).isEqualTo(retiro2);
        retiro2.setId(2L);
        assertThat(retiro1).isNotEqualTo(retiro2);
        retiro1.setId(null);
        assertThat(retiro1).isNotEqualTo(retiro2);
    }
}
