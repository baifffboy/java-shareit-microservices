package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StatusTest {

    @Test
    void shouldHaveCorrectStatusValues() {
        assertThat(Status.values()).containsExactly(
                Status.WAITING,
                Status.APPROVED,
                Status.REJECTED
        );
    }

    @Test
    void shouldHaveCorrectStatusNames() {
        assertThat(Status.WAITING.name()).isEqualTo("WAITING");
        assertThat(Status.APPROVED.name()).isEqualTo("APPROVED");
        assertThat(Status.REJECTED.name()).isEqualTo("REJECTED");
    }

    @Test
    void shouldHaveCorrectStatusOrdinals() {
        assertThat(Status.WAITING.ordinal()).isEqualTo(0);
        assertThat(Status.APPROVED.ordinal()).isEqualTo(1);
        assertThat(Status.REJECTED.ordinal()).isEqualTo(2);
    }
}