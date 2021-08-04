package coursework.threaddy_ball_pool.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResourcesPaths {
    BALL_IMAGE("imgs/ball/%d.png");
    private final String path;

}
