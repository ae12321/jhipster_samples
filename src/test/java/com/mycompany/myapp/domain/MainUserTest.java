package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MainUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MainUser.class);
        MainUser mainUser1 = new MainUser();
        mainUser1.setId(1L);
        MainUser mainUser2 = new MainUser();
        mainUser2.setId(mainUser1.getId());
        assertThat(mainUser1).isEqualTo(mainUser2);
        mainUser2.setId(2L);
        assertThat(mainUser1).isNotEqualTo(mainUser2);
        mainUser1.setId(null);
        assertThat(mainUser1).isNotEqualTo(mainUser2);
    }
}
