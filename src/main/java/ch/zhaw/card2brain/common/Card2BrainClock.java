package ch.zhaw.card2brain.common;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * This class provides the Card2BrainClock . Needed to calculate the nextRepetitionDate
 *
 * @author Niklaus Hänggi
 * @author Alexander Studer
 * @author Roman Joller
 * @version 1.0
 * @since 16.01.2023
 */
@Component
public class Card2BrainClock {


    /**
     * Provides a {@link java.time.Clock} instance for the system's default time zone.
     *
     * @return the {@link java.time.Clock} instance for the system's default time zone.
     */
    @Bean
    public java.time.Clock clock() {
        return java.time.Clock.systemDefaultZone();
    }

}
