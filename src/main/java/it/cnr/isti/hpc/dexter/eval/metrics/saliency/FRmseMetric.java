/**
 *  Copyright 2014 Diego Ceccarelli
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package it.cnr.isti.hpc.dexter.eval.metrics.saliency;

import it.cnr.isti.hpc.dexter.eval.AnnotatedSpot;
import it.cnr.isti.hpc.dexter.eval.cmp.AnnotatedSpotComparator;
import it.cnr.isti.hpc.dexter.eval.metrics.Metric;

import java.util.ArrayList;
import java.util.List;

/**
 * Compute the recall as: <br>
 * <br>
 * <code>
 * 	true positive / (true positive + false negative)
 * </code> <br>
 * <br>
 * 
 * Please refer to <a href="http://en.wikipedia.org/wiki/Precision_and_recall">
 * Wikipedia </a>.
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 16, 2014
 */
public class FRmseMetric implements Metric<Double> {

	private final PrecisionRmseMetric precisionRmseMetric;
	private final RecallRmseMetric recallRmseMetric;

	public FRmseMetric() {
		precisionRmseMetric = new PrecisionRmseMetric();
		recallRmseMetric = new RecallRmseMetric();
	}

	public Double eval(
			List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth, 
			AnnotatedSpotComparator comparator) {

		double precision = precisionRmseMetric.eval(predictions, goldenTruth, comparator);
		double recall = recallRmseMetric.eval(predictions, goldenTruth, comparator);
		if (precision + recall == 0) {
			return 0.0;
		}
		return 2 * (precision * recall) / (precision + recall);
	}

	public String getName() {
		return "F1rmse";
	}

}
