package com.zhumqs.placement;

import com.zhumqs.constants.ExperimentConstants;
import com.zhumqs.encounter.EncounterProbability;
import com.zhumqs.model.Content;
import com.zhumqs.model.MobileUser;
import com.zhumqs.predict.RequestProbability;
import com.zhumqs.utils.DataMockUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author mingqizhu
 * @date 20200103
 */
@Slf4j
@Data
public class ContentPlacement {

    private double weight1;
    private double weight2;
    private double socialWeight;
    private int userNumber;
    private int contentNumber;
    private Map<Integer, List<Integer>> cacheMap;
    private Map<Integer, Integer> placementMap;
    private double[][] encounterMat;
    private double[][] requestProbabilityMat;
    private double[][] expectationMat;

    public ContentPlacement(double weight1, double weight2, double socialWeight, int userNumber, int contentNumber) {
        this.weight1 = weight1;
        this.weight2 = weight2;
        this.socialWeight = socialWeight;
        this.userNumber = userNumber;
        this.contentNumber = contentNumber;
        this.cacheMap = new HashMap<Integer, List<Integer>>();
        this.placementMap = new HashMap<Integer, Integer>();
    }

    private void initCacheStrategy() {
        this.expectationMat = getExpectationMatrix();
        for (int i = 0; i <  contentNumber; i++) {
            double maxExpectation = Double.MIN_VALUE;
            int cacheUserId = 0;
            for (int j = 0; j < userNumber; j++) {
                if (expectationMat[j][i] > maxExpectation ) {
                    List<Integer> list = cacheMap.get(j + 1);
                    if (list == null || list.size() < ExperimentConstants.DEVICE_CAPACITY) {
                        cacheUserId = j + 1;
                        maxExpectation = expectationMat[j][i];
                    }
                }
            }
            if (cacheUserId != 0) {
                List<Integer> cachedContents = cacheMap.get(cacheUserId);
                if (cachedContents == null) {
                    cachedContents = new ArrayList<Integer>();
                }
                cachedContents.add(i + 1);
                cacheMap.put(cacheUserId, cachedContents);
                placementMap.put(i + 1, cacheUserId);
            }
        }
    }

    private double[][] getExpectationMatrix() {
        List<MobileUser> users = DataMockUtils.mockUserInfo(userNumber);
        int[][] trustMat = DataMockUtils.mockTrustRelationship(socialWeight, userNumber);
        EncounterProbability encounterProbability = new EncounterProbability(users, trustMat);
        this.encounterMat = encounterProbability.getEncounterMatrix(weight1, weight2);

        List<Content> contents = DataMockUtils.mockContents(contentNumber);
        RequestProbability requestProbability = new RequestProbability(users, contents);
        this.requestProbabilityMat = requestProbability.getRequestProbabilityMatrix();

        double[][] expectationMat = new double[userNumber][contentNumber];
        for (int i = 0; i < userNumber; i++) {
            for (int j = 0; j < contentNumber; j++) {
                double expectation = 0.0;
                for (int k = 0; k < userNumber; k++) {
                    if (i != k) {
                        expectation += encounterMat[i][k] * requestProbabilityMat[i][j];
                    }
                }
                expectationMat[i][j] = expectation;
            }
        }
        return expectationMat;
    }

    /**
     *  如何计算缓存命中率?
     *
      */
    double getCacheHitRatio() {
        initCacheStrategy();
        double d1 = 0.0, d2 = 0.0;
        for (int i = 0; i < userNumber; i++) {
            for (int j = 0; j < contentNumber; j++) {
                double requestProbability = requestProbabilityMat[i][j];
                double encounterProbability = 1.0;
                Integer cacheUserId = placementMap.get(j + 1);
                if (cacheUserId == null) {
                    encounterProbability = 0.0;
                } else if (cacheUserId != i + 1 ) {
                    encounterProbability = encounterMat[i][cacheUserId - 1];
                }
                d1 += requestProbability * encounterProbability;
                d2 += requestProbability;
            }
        }
        return d1 / d2;
    }

    public static void main(String[] args) {
        ContentPlacement placement = new ContentPlacement(ExperimentConstants.DEFAULT_WEIGHT1,
                ExperimentConstants.DEFAULT_WEIGHT2,
                ExperimentConstants.DEFAULT_SOCIAL_WEIGHT,
                ExperimentConstants.DEFAULT_USER_NUMBER,
                ExperimentConstants.DEFAULT_CONTENT_NUMBER);
        double cacheHitRatio = placement.getCacheHitRatio();
        log.info(String.valueOf(cacheHitRatio));
    }

}
