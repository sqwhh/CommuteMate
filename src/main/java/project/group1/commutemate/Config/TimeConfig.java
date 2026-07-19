package project.group1.commutemate.Config;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Provides clock for scheduling in one time zone. */
@Configuration
public class TimeConfig {

    @Bean
    public Clock appClock(@Value("${app.time-zone:America/Vancouver}") String timeZone) {
        return Clock.system(ZoneId.of(timeZone));
    }
}
