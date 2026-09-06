package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StateTest {

    @Test
    void shouldHaveCorrectStateValues() {
        assertThat(State.values()).containsExactly(
                State.ALL,
                State.CURRENT,
                State.PAST,
                State.FUTURE,
                State.WAITING,
                State.REJECTED
        );
    }

    @Test
    void shouldHaveCorrectStateNames() {
        assertThat(State.ALL.name()).isEqualTo("ALL");
        assertThat(State.CURRENT.name()).isEqualTo("CURRENT");
        assertThat(State.PAST.name()).isEqualTo("PAST");
        assertThat(State.FUTURE.name()).isEqualTo("FUTURE");
        assertThat(State.WAITING.name()).isEqualTo("WAITING");
        assertThat(State.REJECTED.name()).isEqualTo("REJECTED");
    }
}