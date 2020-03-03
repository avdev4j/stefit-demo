package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class ConvoiTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Convoi.class);
        Convoi convoi1 = new Convoi();
        convoi1.setId(1L);
        Convoi convoi2 = new Convoi();
        convoi2.setId(convoi1.getId());
        assertThat(convoi1).isEqualTo(convoi2);
        convoi2.setId(2L);
        assertThat(convoi1).isNotEqualTo(convoi2);
        convoi1.setId(null);
        assertThat(convoi1).isNotEqualTo(convoi2);
    }
}
