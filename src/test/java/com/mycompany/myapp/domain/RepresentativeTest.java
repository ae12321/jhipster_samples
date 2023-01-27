package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RepresentativeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Representative.class);
        Representative representative1 = new Representative();
        representative1.setId(1L);
        Representative representative2 = new Representative();
        representative2.setId(representative1.getId());
        assertThat(representative1).isEqualTo(representative2);
        representative2.setId(2L);
        assertThat(representative1).isNotEqualTo(representative2);
        representative1.setId(null);
        assertThat(representative1).isNotEqualTo(representative2);
    }
}
