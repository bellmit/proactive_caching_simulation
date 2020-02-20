package com.zhumqs.placement;

import com.zhumqs.constants.ExperimentConstants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mingqizhu
 * @date 20200105
 */
@Slf4j
@Data
public class Simulation {
    private static ContentPlacement contentPlacement;

    private static void testImpact() {
        double cacheHitRatio;

        log.info("<------Impact of social relation------>");
        for (int i = 1; i <= 10; i++) {
            double socialWeight = i * 1.0 / 10;
            contentPlacement = new ContentPlacement(ExperimentConstants.DEFAULT_WEIGHT1,
                    ExperimentConstants.DEFAULT_WEIGHT2,
                    socialWeight,
                    ExperimentConstants.DEFAULT_USER_NUMBER,
                    ExperimentConstants.DEFAULT_CONTENT_NUMBER);
            cacheHitRatio = contentPlacement.getCacheHitRatio();
            log.info("Social relation: {}, cache hit ratio: {} ", socialWeight, cacheHitRatio);
        }

        log.info("<------Impact of user number------>");
        for (int i = 50; i <= 200; i += 50) {
            contentPlacement = new ContentPlacement(ExperimentConstants.DEFAULT_WEIGHT1,
                    ExperimentConstants.DEFAULT_WEIGHT2,
                    ExperimentConstants.DEFAULT_SOCIAL_WEIGHT,
                    i,
                    ExperimentConstants.DEFAULT_CONTENT_NUMBER);
            cacheHitRatio = contentPlacement.getCacheHitRatio();
            log.info("User number: {}, cache hit ratio: {} ", i, cacheHitRatio);
        }

        log.info("<------Impact of content number------>");
        for (int i = 100; i <= 2000; i += 200) {
            contentPlacement = new ContentPlacement(ExperimentConstants.DEFAULT_WEIGHT1,
                    ExperimentConstants.DEFAULT_WEIGHT2,
                    ExperimentConstants.DEFAULT_SOCIAL_WEIGHT,
                    ExperimentConstants.DEFAULT_USER_NUMBER,
                    i);
            cacheHitRatio = contentPlacement.getCacheHitRatio();
            log.info("Content number: {}, cache hit ratio: {} ", i, cacheHitRatio);
        }
    }

    public static void main(String[] args) {
        Simulation.testImpact();
    }
}
