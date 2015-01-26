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
import it.cnr.isti.hpc.dexter.eval.AnnotatedSpot.SortByConfidence;
import it.cnr.isti.hpc.dexter.eval.cmp.AnnotatedSpotComparator;
import it.cnr.isti.hpc.dexter.eval.metrics.Metric;
import it.cnr.isti.hpc.dexter.eval.metrics.MetricUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * NDCG metric
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 16, 2014
 */
public class NDCGMetric implements Metric<Double> {

	MetricUtil metric;

	public NDCGMetric() {
		metric = new MetricUtil();
	}

	public Double eval(List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth, AnnotatedSpotComparator comparator) {

		List<AnnotatedSpot> tp = new ArrayList<AnnotatedSpot>();

		Collections.sort(predictions, new SortByConfidence());
		double dcg = 0;
		for (int i = 0; i < predictions.size(); i++) {

			AnnotatedSpot p = predictions.get(i);
			// System.out.println((i + 1) + ") Entity " + p.asString());
			double pos = i + 1;
			for (AnnotatedSpot g : goldenTruth) {
				if (comparator.match(p, g)) {
					// System.out.println("Match: entity " + g.asString());
					tp.add(g);
					double rel = g.getConfidenceScore();
					// System.out.println("Match: adding " + rel
					// / (Math.log10(pos + 1) / Math.log10(2)));
					dcg += rel / (Math.log10(pos + 1) / Math.log10(2));
				}
			}
		}
		// System.out.println("final dcg " + dcg);

		double idcg = 0;
		Collections.sort(tp, new SortByConfidence());

		for (int i = 0; i < tp.size(); i++) {
			AnnotatedSpot p = tp.get(i);
			double pos = i + 1;
			double rel = p.getConfidenceScore();
			idcg += rel / (Math.log10(pos + 1) / Math.log10(2));
		}
		// System.out.println("final idcg " + idcg);
		if (idcg == 0)
			return 0.0;

		return dcg / idcg;

	}

	public String getName() {
		return "ndcg";
	}

}
