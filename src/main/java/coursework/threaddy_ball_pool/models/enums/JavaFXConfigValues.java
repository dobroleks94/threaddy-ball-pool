package coursework.threaddy_ball_pool.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JavaFXConfigValues {
    MAJOR_TICK_UNIT(1.0),
    MINOR_TICK_COUNT(0);

    private final double value;
}