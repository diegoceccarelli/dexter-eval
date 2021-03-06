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

import java.util.List;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 16, 2014
 */
public class DoubleValuesCollector extends AbstractMetricValueCollector<Double>
		implements MetricValuesCollector<Double> {

	private Double total;
	private Double partial;
	private final Metric<Double> metric;

	public DoubleValuesCollector(Metric<Double> metric) {
		this.name = metric.getName();
		this.metric = metric;
		total = 0.0;
	}

	@Override
	protected void eval(List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth, AnnotatedSpotComparator comparator) {
		partial = metric.eval(predictions, goldenTruth, comparator);
		total += partial;
	}

	@Override
	public Double getCollectedTotal() {
		return total;
	}

	public double getScore() {
		if (this.serveAverageScore)
			return total / size();
		return total;
	}

	public Double getPartial() {
		return partial;
	}

}
