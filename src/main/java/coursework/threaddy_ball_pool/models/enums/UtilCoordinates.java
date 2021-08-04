package coursework.threaddy_ball_pool.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UtilCoordinates {

    START(95, 70), FINISH(710, 90);

    private final double X;
    private final double Y;


}
