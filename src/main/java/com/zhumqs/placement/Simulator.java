package com.zhumqs.placement;

import com.zhumqs.constants.ExperimentConstants;
import com.zhumqs.model.Content;
import com.zhumqs.model.MobileUser;
import com.zhumqs.utils.DataMockUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author mingqizhu
 * @date 20200105
 */
@Slf4j
@Data
public class Simulator {

    private static void testSocialImpact() {

        log.info("<------Impact of social relation------>");
        List<MobileUser> users = DataMockUtils.mockUserInfo(ExperimentConstants.DEFAULT_USER_NUMBER);
        List<Content> contents = DataMockUtils.mockContents(ExperimentConstants.DEFAULT_CONTENT_NUMBER);

        for (int i = 1; i <= 10; i++) {
            double socialWeight = i * 1.0 / 10;
            int[][] trustMat = DataMockUtils.mockTrustRelationship(socialWeight,
                    ExperimentConstants.DEFAULT_USER_NUMBER);

            FullCooperativeCaching fullCooperativeCaching = new FullCooperativeCaching(ExperimentConstants.DEFAULT_WEIGHT1,
                    ExperimentConstants.DEFAULT_WEIGHT2,
                    ExperimentConstants.DEVICE_CAPACITY,
                    users, contents, trustMat);
            log.info("ProposedCaching: Social relation: {}, cache hit ratio: {} ", socialWeight, fullCooperativeCaching.getCacheHitRatio());

            PopularCache popularCache = new PopularCache(users, contents, ExperimentConstants.DEVICE_CAPACITY, trustMat);
            log.info("PopularCaching: Social relation: {}, cache hit ratio: {} ", socialWeight, popularCache.getCacheHitRatio());

            FairCache fairCache = new FairCache(users, contents, ExperimentConstants.DEVICE_CAPACITY, trustMat);
            log.info("FairCaching: Social relation: {}, cache hit ratio: {} ", socialWeight, fairCache.getCacheHitRatio());
            log.info("");

        }

    }

    private static void testUserNumberImpact() {

        log.info("<------Impact of user number------>");
        List<Content> contents = DataMockUtils.mockContents(ExperimentConstants.DEFAULT_CONTENT_NUMBER);

        for (int i = 50; i <= 200; i += 50) {
            List<MobileUser> users = DataMockUtils.mockUserInfo(i);
            int[][] trustMat = DataMockUtils.mockTrustRelationship(ExperimentConstants.DEFAULT_SOCIAL_WEIGHT, i);
            FullCooperativeCaching fullCooperativeCaching = new FullCooperativeCaching(ExperimentConstants.DEFAULT_WEIGHT1,
                    ExperimentConstants.DEFAULT_WEIGHT2,
                    ExperimentConstants.DEVICE_CAPACITY,
                    users, contents, trustMat);
            log.info("ProposedCaching: User number: {}, cache hit ratio: {} ", i, fullCooperativeCaching.getCacheHitRatio());

            PopularCache popularCache = new PopularCache(users, contents, ExperimentConstants.DEVICE_CAPACITY, trustMat);
            log.info("PopularCaching: User number: {}, cache hit ratio: {} ", i, popularCache.getCacheHitRatio());

            FairCache fairCache = new FairCache(users, contents, ExperimentConstants.DEVICE_CAPACITY, trustMat);
            log.info("FairCaching: User number: {}, cache hit ratio: {} ", i, fairCache.getCacheHitRatio());
            log.info("");
        }
    }

    private static void testContentNumberImpact() {
        log.info("<------Impact of content number------>");
        List<MobileUser> users = DataMockUtils.mockUserInfo(ExperimentConstants.DEFAULT_USER_NUMBER);
        int[][] trustMat = DataMockUtils.mockTrustRelationship(ExperimentConstants.DEFAULT_SOCIAL_WEIGHT,
                ExperimentConstants.DEFAULT_USER_NUMBER);

        for (int i = 1000; i <= 10000; i += 1000) {
            List<Content> contents = DataMockUtils.mockContents(i);

            FullCooperativeCaching fullCooperativeCaching = new FullCooperativeCaching(ExperimentConstants.DEFAULT_WEIGHT1,
                    ExperimentConstants.DEFAULT_WEIGHT2,
                    ExperimentConstants.DEVICE_CAPACITY,
                    users, contents, trustMat);
            log.info("ProposedCaching: Content number: {}, cache hit ratio: {} ", i, fullCooperativeCaching.getCacheHitRatio());

            PopularCache popularCache = new PopularCache(users, contents, ExperimentConstants.DEVICE_CAPACITY, trustMat);
            log.info("PopularCaching: Content number: {}, cache hit ratio: {} ", i, popularCache.getCacheHitRatio());

            FairCache fairCache = new FairCache(users, contents, ExperimentConstants.DEVICE_CAPACITY, trustMat);
            log.info("FairCaching: Content number: {}, cache hit ratio: {} ", i, fairCache.getCacheHitRatio());
            log.info("");
        }
    }

    private static void testCapacityImpact() {

        log.info("<------Impact of capacity------>");
        List<MobileUser> users = DataMockUtils.mockUserInfo(ExperimentConstants.DEFAULT_USER_NUMBER);
        List<Content> contents = DataMockUtils.mockContents(ExperimentConstants.DEFAULT_CONTENT_NUMBER);
        int[][] trustMat = DataMockUtils.mockTrustRelationship(ExperimentConstants.DEFAULT_SOCIAL_WEIGHT,
                ExperimentConstants.DEFAULT_USER_NUMBER);

        for (int i = 10; i <= 100; i += 10) {
            FullCooperativeCaching fullCooperativeCaching = new FullCooperativeCaching(ExperimentConstants.DEFAULT_WEIGHT1,
                    ExperimentConstants.DEFAULT_WEIGHT2,
                    i, users, contents, trustMat);
            log.info("ProposedCaching: Capacity: {}, cache hit ratio: {} ", i, fullCooperativeCaching.getCacheHitRatio());

            PopularCache popularCache = new PopularCache(users, contents, ExperimentConstants.DEVICE_CAPACITY, trustMat);
            log.info("PopularCaching: Capacity: {}, cache hit ratio: {} ", i, popularCache.getCacheHitRatio());

            FairCache fairCache = new FairCache(users, contents, ExperimentConstants.DEVICE_CAPACITY, trustMat);
            log.info("FairCaching: Capacity: {}, cache hit ratio: {} ", i, fairCache.getCacheHitRatio());
            log.info("");
        }
    }

    public static void main(String[] args) {
        Simulator.testSocialImpact();
        Simulator.testUserNumberImpact();
        Simulator.testContentNumberImpact();
        Simulator.testCapacityImpact();
    }

}
