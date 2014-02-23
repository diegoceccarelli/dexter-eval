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
import it.cnr.isti.hpc.dexter.eval.metrics.FalseNegativeMetric;
import it.cnr.isti.hpc.dexter.eval.metrics.TruePositiveMetric;
import it.cnr.isti.hpc.dexter.eval.output.ConsoleResultsAppender;

import java.util.List;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 16, 2014
 */
public class MicroRecallValuesCollector extends
		AbstractMetricValueCollector<Double> implements
		MetricValuesCollector<Double> {

	IntValuesCollector truePositivesCollector;
	IntValuesCollector falseNegativeCollector;

	public MicroRecallValuesCollector() {
		this.name = "micro-R";
		truePositivesCollector = new IntValuesCollector(
				new TruePositiveMetric());
		falseNegativeCollector = new IntValuesCollector(
				new FalseNegativeMetric());
		addOutputCollector(new ConsoleResultsAppender());
	}

	@Override
	protected void eval(List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth, AnnotatedSpotComparator comparator) {
		truePositivesCollector.collect(predictions, goldenTruth, comparator);
		falseNegativeCollector.collect(predictions, goldenTruth, comparator);
	}

	@Override
	public Double getCollectedTotal() {
		double tp = truePositivesCollector.getCollectedTotal();
		double fn = falseNegativeCollector.getCollectedTotal();
		if (tp + fn == 0) {
			return 0.0;
		}
		return (tp) / (tp + fn);
	}

	public double getScore() {
		return getCollectedTotal();
	}

	public Double getPartial() {
		// partial does not have sense for micro.. btw it returns the micro
		// until now
		double tp = truePositivesCollector.getCollectedTotal();
		double fn = falseNegativeCollector.getCollectedTotal();
		if (tp + fn == 0) {
			return 0.0;
		}
		return (tp) / (tp + fn);
	}

}
