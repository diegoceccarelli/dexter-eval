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
public class PrecisionRmseMetric implements Metric<Double> {

	MetricUtil metric;

	public PrecisionRmseMetric() {
		metric = new MetricUtil();
	}

	public Double eval(List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth, AnnotatedSpotComparator comparator) {

		List<AnnotatedSpot> tp = new ArrayList<AnnotatedSpot>();
		List<AnnotatedSpot> tpgt = new ArrayList<AnnotatedSpot>();
		List<AnnotatedSpot> fp = new ArrayList<AnnotatedSpot>();
		List<AnnotatedSpot> fn = new ArrayList<AnnotatedSpot>();

		metric.intersect(predictions, goldenTruth, tp, tpgt, fp, fn, comparator);

		double se = 0;
		for (AnnotatedSpot spot_tpgt : tpgt) {
			for (AnnotatedSpot spot_tp : tp) {
				if (comparator.match(spot_tp, spot_tpgt)) {
					se += Math.pow(
							spot_tp.getConfidenceScore()
									- spot_tpgt.getConfidenceScore(), 2);
					break;
				}
			}
		}

		for (AnnotatedSpot spot_fp : fp) {
			se += Math.pow(spot_fp.getConfidenceScore(), 2);
		}

		if (tp.size() + fp.size() > 0) {
			return Math.sqrt(se / (tp.size() + fp.size()));
		} else {
			return 0.;
		}
	}

	public String getName() {
		return "Prmse";
	}

}
