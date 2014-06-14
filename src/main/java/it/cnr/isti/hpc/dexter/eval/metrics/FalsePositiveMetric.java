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

import java.util.ArrayList;
import java.util.List;

/**
 * Counts the false positive predictions.
 * 
 * Please refer to <a href="http://en.wikipedia.org/wiki/Precision_and_recall">
 * Wikipedia </a> for more informations.
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 16, 2014
 */
public class FalsePositiveMetric implements Metric<Integer> {

	MetricUtil metric = new MetricUtil();

	public Integer eval(List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth, AnnotatedSpotComparator comparator) {
		List<AnnotatedSpot> tp = new ArrayList<AnnotatedSpot>();
		List<AnnotatedSpot> tpgt = new ArrayList<AnnotatedSpot>();
		List<AnnotatedSpot> fp = new ArrayList<AnnotatedSpot>();

		List<AnnotatedSpot> fn = new ArrayList<AnnotatedSpot>();

		metric.intersect(predictions, goldenTruth, tp, tpgt, fp, fn, comparator);
		return fp.size();
	}

	public String getName() {
		return "fp";
	}

}
