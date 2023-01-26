package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReservedSeatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReservedSeat.class);
        ReservedSeat reservedSeat1 = new ReservedSeat();
        reservedSeat1.setId(1L);
        ReservedSeat reservedSeat2 = new ReservedSeat();
        reservedSeat2.setId(reservedSeat1.getId());
        assertThat(reservedSeat1).isEqualTo(reservedSeat2);
        reservedSeat2.setId(2L);
        assertThat(reservedSeat1).isNotEqualTo(reservedSeat2);
        reservedSeat1.setId(null);
        assertThat(reservedSeat1).isNotEqualTo(reservedSeat2);
    }
}
