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
package it.cnr.isti.hpc.dexter.eval.collector;

import it.cnr.isti.hpc.dexter.eval.AnnotatedSpot;
import it.cnr.isti.hpc.dexter.eval.cmp.AnnotatedSpotComparator;
import it.cnr.isti.hpc.dexter.eval.metrics.Metric;
import it.cnr.isti.hpc.dexter.eval.output.ConsoleResultsAppender;

import java.util.List;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 16, 2014
 */
public class IntValuesCollector extends AbstractMetricValueCollector<Integer>
		implements MetricValuesCollector<Integer> {

	Integer total;
	Integer partial;
	Metric<Integer> metric;

	public IntValuesCollector(Metric<Integer> metric) {
		this.name = metric.getName();
		this.metric = metric;
		this.serveAverageScore = false;
		total = 0;
		addOutputCollector(new ConsoleResultsAppender());

	}

	@Override
	protected void eval(List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth, AnnotatedSpotComparator comparator) {
		partial = metric.eval(predictions, goldenTruth, comparator);
		total += partial;
	}

	@Override
	public Integer getCollectedTotal() {
		return total;
	}

	public double getScore() {
		if (this.serveAverageScore)
			return (double) total / (double) size();
		return total;
	}

	public Integer getPartial() {
		return partial;
	}

}
