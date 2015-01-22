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
import it.cnr.isti.hpc.dexter.eval.metrics.FalsePositiveMetric;
import it.cnr.isti.hpc.dexter.eval.metrics.Metric;
import it.cnr.isti.hpc.dexter.eval.metrics.TruePositiveMetric;

import java.util.ArrayList;
import java.util.List;

/**
 * Compute the precision as: <br>
 * <br>
 * <code>
 * 	true positive / (true positive + false positive)
 * </code> <br>
 * <br>
 * 
 * 
 * 
 * Please refer to <a href="http://en.wikipedia.org/wiki/Precision_and_recall">
 * Wikipedia </a> for more informations.
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 16, 2014
 */
public class TopSalientPrecisionMetric implements Metric<Double> {

	private final FalsePositiveMetric fnm;
	private final TruePositiveMetric tpm;

	public TopSalientPrecisionMetric() {
		fnm = new FalsePositiveMetric();
		tpm = new TruePositiveMetric();
	}

	public Double eval(List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth, AnnotatedSpotComparator comparator) {
		List<AnnotatedSpot> filteredGolden = new ArrayList<AnnotatedSpot>();
		for (AnnotatedSpot spot : goldenTruth) {
			Integer rel = Math.round(spot.getConfidenceScore());
			if (rel == 3) {
				filteredGolden.add(spot);
			}
		}
		double fn = fnm.eval(predictions, filteredGolden, comparator);
		double tp = tpm.eval(predictions, filteredGolden, comparator);
		if ((fn + tp) == 0) {
			return 0d;
		}
		return (tp) / (fn + tp);
	}

	public String getName() {
		return "Ps3";
	}

}
