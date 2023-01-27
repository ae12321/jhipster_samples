package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SeatGroupTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SeatGroup.class);
        SeatGroup seatGroup1 = new SeatGroup();
        seatGroup1.setId(1L);
        SeatGroup seatGroup2 = new SeatGroup();
        seatGroup2.setId(seatGroup1.getId());
        assertThat(seatGroup1).isEqualTo(seatGroup2);
        seatGroup2.setId(2L);
        assertThat(seatGroup1).isNotEqualTo(seatGroup2);
        seatGroup1.setId(null);
        assertThat(seatGroup1).isNotEqualTo(seatGroup2);
    }
}
