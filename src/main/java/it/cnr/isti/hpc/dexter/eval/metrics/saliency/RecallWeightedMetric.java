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
import it.cnr.isti.hpc.dexter.eval.metrics.MetricUtil;

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
public class RecallWeightedMetric implements Metric<Double> {

	MetricUtil metric;
	
	public RecallWeightedMetric() {
		metric = new MetricUtil();
	}

	public Double eval(
			List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth, 
			AnnotatedSpotComparator comparator) {
		
		List<AnnotatedSpot> tp = new ArrayList<AnnotatedSpot>();
		List<AnnotatedSpot> tpgt = new ArrayList<AnnotatedSpot>();
		List<AnnotatedSpot> fp = new ArrayList<AnnotatedSpot>();
		List<AnnotatedSpot> fn = new ArrayList<AnnotatedSpot>();

		metric.intersect(predictions, goldenTruth, tp, tpgt, fp, fn, comparator);
		
		double sum_scores_tpgt = 0;
		for (AnnotatedSpot spot: tpgt) {
			sum_scores_tpgt += spot.getConfidenceScore();
		}
		
		double sum_scores_fn = 0;
		for (AnnotatedSpot spot: fn) {
			sum_scores_fn += spot.getConfidenceScore();
		}
		
		if (sum_scores_tpgt + sum_scores_fn > 0)
			return sum_scores_tpgt / (sum_scores_tpgt + sum_scores_fn);
		else
			return 0.;
	}

	public String getName() {
		return "Rweighted";
	}

}
