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
package it.cnr.isti.hpc.dexter.eval.output;

import it.cnr.isti.hpc.dexter.eval.AnnotatedSpot;
import it.cnr.isti.hpc.dexter.eval.cmp.AnnotatedSpotComparator;
import it.cnr.isti.hpc.dexter.eval.collector.MetricValuesCollector;
import it.cnr.isti.hpc.dexter.eval.metrics.MetricUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Prints the results on the std out.
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 17, 2014
 */
public class ConsoleResultsAppender implements OutputResultsAppender {

	boolean partial = false;
	MetricUtil utils = new MetricUtil();

	public ConsoleResultsAppender appendPartial() {
		partial = true;
		return this;
	}

	public void append(MetricValuesCollector<?> metric) {
		double score = metric.getScore();
		String value = String.format("%-40s%.3f", metric.getName(), score);
		System.out.println(value);
	}

	public boolean isAppendPartial() {
		return partial;
	}

	public void appendPartial(List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth,
			AnnotatedSpotComparator comparator,
			MetricValuesCollector<?> collector) {
		System.out.println("DOC-ID= " + goldenTruth.get(0).getDocId());

		List<AnnotatedSpot> tpPredictions = new ArrayList<AnnotatedSpot>();
		List<AnnotatedSpot> tpGoldenTruth = new ArrayList<AnnotatedSpot>();
		List<AnnotatedSpot> fp = new ArrayList<AnnotatedSpot>();
		List<AnnotatedSpot> fn = new ArrayList<AnnotatedSpot>();

		utils.intersect(predictions, goldenTruth, tpPredictions, tpGoldenTruth,
				fp, fn, comparator);

		String value = String.format("%-40s%s", "prediction", "golden truth");
		System.out.println(value);
		for (int i = 0; i < tpGoldenTruth.size(); i++) {
			value = String.format("%-40s%s", tpPredictions.get(i).asString(),
					tpGoldenTruth.get(i).asString());
			System.out.println(value);

		}
		for (int i = 0; i < fn.size(); i++) {
			value = String.format("%-40s%s", "", fn.get(i).asString());
			System.out.println(value);

		}
		for (int i = 0; i < fp.size(); i++) {
			value = String.format("%-40s%s", fp.get(i).asString(), "");
			System.out.println(value);

		}

		Object o = collector.getPartial();
		value = "NONE";
		if (o instanceof Integer) {
			value = String.format("%-40s%d", collector.getName(),
					collector.getPartial());
		}
		if (o instanceof Double) {
			value = String.format("%-40s%.3f", collector.getName(),
					collector.getPartial());
		}
		System.out.println(value);
		System.out.println();

	}

	public void setPartial(boolean isPartial) {
		partial = isPartial;

	}
}
