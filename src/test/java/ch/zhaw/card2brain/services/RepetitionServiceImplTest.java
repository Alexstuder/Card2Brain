package ch.zhaw.card2brain.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.mockito.Mockito.doReturn;
/**
 * RepetitionServiceImplTest is a test class for RepetitionServiceImpl.
 * It tests the behavior of {@link RepetitionServiceImpl#getNextRepetitionDate(int)} method.
 * @AutoConfigureMockMvc, and @Autowired annotations to configure the test environment and
 * inject necessary dependencies.
 * The class also extends from EmptyDb class, which is used to clean up the database before each test.
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 28-01-2023
 */
@SpringBootTest
class RepetitionServiceImplTest {

    // Some fixed date to make your tests
    private final static LocalDate LOCAL_DATE = LocalDate.of(2022, 1, 13);

    // mock your tested class
    @InjectMocks
    private RepetitionServiceImpl repetitionService;

    //Mock your clock bean
    @Mock
    private Clock clock;


    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);

        //tell your tests to return the specified LOCAL_DATE when calling LocalDate.now(clock)
        //field that will contain the fixed clock
        Clock fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
    }

    /**
     * Tests the {@link RepetitionServiceImpl#getNextRepetitionDate(int)} method.
     */
    @Test
    void getNextRepetitionDate() {

        Assertions.assertEquals(LocalDate.of(2022, 1, 13), repetitionService.getNextRepetitionDate(0));
        Assertions.assertEquals(LocalDate.of(2022, 1, 15), repetitionService.getNextRepetitionDate(1));
        Assertions.assertEquals(LocalDate.of(2022, 1, 16), repetitionService.getNextRepetitionDate(2));
        Assertions.assertEquals(LocalDate.of(2022, 1, 18), repetitionService.getNextRepetitionDate(3));
        Assertions.assertEquals(LocalDate.of(2022, 1, 20), repetitionService.getNextRepetitionDate(4));
        Assertions.assertEquals(LocalDate.of(2022, 1, 22), repetitionService.getNextRepetitionDate(5));
        Assertions.assertEquals(LocalDate.of(2022, 1, 27), repetitionService.getNextRepetitionDate(6));
        Assertions.assertEquals(LocalDate.of(2022, 2, 12), repetitionService.getNextRepetitionDate(7));
        Assertions.assertEquals(LocalDate.of(2022, 2, 27), repetitionService.getNextRepetitionDate(8));
        Assertions.assertEquals(LocalDate.of(2022, 3, 14), repetitionService.getNextRepetitionDate(9));
        Assertions.assertEquals(LocalDate.of(2022, 4, 13), repetitionService.getNextRepetitionDate(10));
        Assertions.assertEquals(LocalDate.of(2022, 4, 13), repetitionService.getNextRepetitionDate(20));

    }

}
