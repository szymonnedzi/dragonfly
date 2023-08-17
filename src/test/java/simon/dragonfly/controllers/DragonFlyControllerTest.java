package simon.dragonfly.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class DragonFlyControllerTest {

    @Mock
    private Logger logger;

    private DragonFlyController dragonFlyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckAppStatusSuccess() {
        dragonFlyController = new DragonFlyController();
        ResponseEntity<String> response = dragonFlyController.checkAppStatus();
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is("Dragonfly is online"));
    }
}
