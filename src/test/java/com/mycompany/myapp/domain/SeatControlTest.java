package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SeatControlTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SeatControl.class);
        SeatControl seatControl1 = new SeatControl();
        seatControl1.setId(1L);
        SeatControl seatControl2 = new SeatControl();
        seatControl2.setId(seatControl1.getId());
        assertThat(seatControl1).isEqualTo(seatControl2);
        seatControl2.setId(2L);
        assertThat(seatControl1).isNotEqualTo(seatControl2);
        seatControl1.setId(null);
        assertThat(seatControl1).isNotEqualTo(seatControl2);
    }
}
