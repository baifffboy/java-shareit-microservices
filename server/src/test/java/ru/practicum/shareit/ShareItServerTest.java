package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ShareItServerTest {

    @Test
    void contextLoads() {
        assertThat(true).isTrue();
    }

    @Test
    void mainMethodShouldRun() {
        ShareItServer.main(new String[]{});
        assertThat(true).isTrue();
    }
}