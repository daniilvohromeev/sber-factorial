package org.main.sberfactorial.metrics;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.main.sberfactorial.configuration.TimedConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

public class TimedConfigurationTest {

    @Test
    public void timedAspectBeanShouldBeCreated() {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.register(TimedConfiguration.class);

        MeterRegistry mockRegistry = mock(MeterRegistry.class);

        context.getBeanFactory().registerSingleton("meterRegistry", mockRegistry);

        context.refresh();

        TimedAspect aspect = context.getBean(TimedAspect.class);

        assertThat(aspect).isNotNull();

        context.close();
    }
}
