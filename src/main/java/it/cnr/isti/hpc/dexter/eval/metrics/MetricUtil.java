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

import java.util.Collections;
import java.util.List;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 21, 2014
 */
public class MetricUtil {

	public MetricUtil() {

	}

	public void intersect(List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth, List<AnnotatedSpot> tpPredictions,
			List<AnnotatedSpot> tpGoldenTruth, List<AnnotatedSpot> fp,
			List<AnnotatedSpot> fn, AnnotatedSpotComparator comparator) {

		Collections.sort(predictions, new AnnotatedSpot.SortByStart());
		Collections.sort(goldenTruth, new AnnotatedSpot.SortByStart());
		for (AnnotatedSpot spot : predictions) {
			boolean found = false;
			// System.out.println(spot);
			for (AnnotatedSpot goldSpot : goldenTruth) {
				// System.out.println("> Comparing with " + goldSpot);
				if (comparator.match(spot, goldSpot)
						&& !tpGoldenTruth.contains(goldSpot)) {
					tpPredictions.add(spot);
					tpGoldenTruth.add(goldSpot);
					found = true;
					break;
				}
			}
			if (!found) {
				// System.out.println("NOT FOUND");
				// if I've not matched the spot with a goldSpot, then
				// it's a false positive
				fp.add(spot);
			} else {
				// System.out.println("FOUND");
			}

		}
		for (AnnotatedSpot goldSpot : goldenTruth) {
			if (!tpGoldenTruth.contains(goldSpot)) {
				fn.add(goldSpot);
			}
		}

		Collections.sort(fp, new AnnotatedSpot.SortByStart());
		Collections.sort(fn, new AnnotatedSpot.SortByStart());
		Collections.sort(tpPredictions, new AnnotatedSpot.SortByStart());
		Collections.sort(tpGoldenTruth, new AnnotatedSpot.SortByStart());

		return;

	}
}
