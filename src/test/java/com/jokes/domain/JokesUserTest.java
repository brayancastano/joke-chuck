package com.jokes.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jokes.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JokesUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JokesUser.class);
        JokesUser jokesUser1 = new JokesUser();
        jokesUser1.setId(1L);
        JokesUser jokesUser2 = new JokesUser();
        jokesUser2.setId(jokesUser1.getId());
        assertThat(jokesUser1).isEqualTo(jokesUser2);
        jokesUser2.setId(2L);
        assertThat(jokesUser1).isNotEqualTo(jokesUser2);
        jokesUser1.setId(null);
        assertThat(jokesUser1).isNotEqualTo(jokesUser2);
    }
}
