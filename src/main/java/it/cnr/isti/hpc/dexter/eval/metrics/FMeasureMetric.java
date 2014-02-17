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
package it.cnr.isti.hpc.dexter.eval.metrics;

import it.cnr.isti.hpc.dexter.eval.AnnotatedSpot;
import it.cnr.isti.hpc.dexter.eval.cmp.AnnotatedSpotComparator;

import java.util.List;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 16, 2014
 */
public class FMeasureMetric implements Metric<Double> {

	private final PrecisionMetric precisionMetric;
	private final RecallMetric recallMetric;

	public FMeasureMetric() {
		precisionMetric = new PrecisionMetric();
		recallMetric = new RecallMetric();
	}

	public Double eval(List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth, AnnotatedSpotComparator comparator) {
		double precision = precisionMetric.eval(predictions, goldenTruth,
				comparator);
		double recall = recallMetric.eval(predictions, goldenTruth, comparator);
		if (precision + recall == 0) {
			return 0.0;
		}
		return 2 * (precision * recall) / (precision + recall);
	}

}
