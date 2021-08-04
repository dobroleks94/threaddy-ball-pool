package coursework.threaddy_ball_pool.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UtilMeasures {
    RADIUS(17),
    STEP(1),
    MIN_BALLS_SIZE(3), MAX_BALLS_SIZE(10),
    MIN_SPEED(1), MAX_SPEED(10),
    MILLISECOND_SPEED_START_THRESHOLD(100),
    SPEED_REGULATION(10),
    NODE_SPACING(20),
    MIN_PRIORITY(1),
    MAX_PRIORITY(10),
    PRIORITY_STEP(1),
    SLIDER_TITLE_FONT_SIZE(16);

    private final int measure;
}
