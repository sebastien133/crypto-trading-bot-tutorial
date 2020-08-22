package tech.cassandre.trading.bot.tutorial.ta;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import tech.cassandre.trading.bot.dto.position.PositionStatusDTO;
import tech.cassandre.trading.bot.test.mock.TickerFluxMock;

import java.math.BigDecimal;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Basic Ta4j strategy test.
 */
@SpringBootTest
@Import(TickerFluxMock.class)
@DisplayName("Simple strategy test")
public class SimpleTa4jStrategyTest {

    @Autowired
    SimpleTa4jStrategy strategy;

    @Autowired
    TickerFluxMock tickerFluxMock;

    @Test
    @DisplayName("Check gains")
    public void gainTest() {
        await().forever().until(() -> tickerFluxMock.isFluxDone());

        final BigDecimal gains = strategy.getPositions()
                .values()
                .stream()
                .filter(p -> p.getStatus().equals(PositionStatusDTO.CLOSED))
                .map(p -> p.getPositionGain().getAmount().getValue())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("Cumulated gains > " + gains);
        System.out.println("Position still opened :");
        strategy.getPositions()
                .values()
                .stream()
                .filter(p -> p.getStatus().equals(PositionStatusDTO.OPENED))
                .forEach(positionDTO -> System.out.println(" - " + positionDTO.getId()));
    }

}
