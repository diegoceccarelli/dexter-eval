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
import it.cnr.isti.hpc.dexter.eval.output.ConsoleResultsAppender;

import java.util.List;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 16, 2014
 */
public class MicroFMeasureValuesCollector extends
		AbstractMetricValueCollector<Double> implements
		MetricValuesCollector<Double> {

	MicroPrecisionValuesCollector precision;
	MicroRecallValuesCollector recall;

	public MicroFMeasureValuesCollector() {
		this.name = "micro-F1";
		precision = new MicroPrecisionValuesCollector();
		recall = new MicroRecallValuesCollector();
		addOutputCollector(new ConsoleResultsAppender());
	}

	@Override
	protected void eval(List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth, AnnotatedSpotComparator comparator) {
		precision.collect(predictions, goldenTruth, comparator);
		recall.collect(predictions, goldenTruth, comparator);
	}

	@Override
	public Double getCollectedTotal() {
		double p = precision.getScore();
		double r = recall.getScore();
		if ((p + r) == 0) {
			return 0.0;
		}
		return 2 * (p * r) / (p + r);
	}

	public double getScore() {
		return getCollectedTotal();
	}

	public Double getPartial() {
		// partial does not have sense for micro.. btw it returns the micro
		// until now
		double p = precision.getScore();
		double r = recall.getScore();
		if ((p + r) == 0) {
			return 0.0;
		}
		return 2 * (p * r) / (p + r);
	}

}
