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
package it.cnr.isti.hpc.dexter.eval;

import it.cnr.isti.hpc.dexter.eval.cmp.AnnotatedSpotComparator;
import it.cnr.isti.hpc.dexter.eval.cmp.SameSpotComparator;
import it.cnr.isti.hpc.dexter.eval.collector.MetricValuesCollector;
import it.cnr.isti.hpc.dexter.eval.filter.Filter;
import it.cnr.isti.hpc.dexter.eval.filter.SameAnnotatedSpotFilter;
import it.cnr.isti.hpc.dexter.eval.reader.AnnotatedSpotReader;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An evaluator evaluates a file containing the predictions against a golden
 * truth file (using a given {@link SameSpotComparator}) and returns the values
 * of the collectors set.
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 16, 2014
 */
public class Evaluator {

	private static final Logger logger = LoggerFactory
			.getLogger(Evaluator.class);

	private final List<MetricValuesCollector<?>> collectors;
	private final AnnotatedSpotComparator comparator;
	private final AnnotatedSpotReader predictionsReader;
	private final AnnotatedSpotReader goldenTruthReader;

	private Filter sameAnnotatedSpotFilter;

	public Evaluator(AnnotatedSpotReader predictionsReader,
			AnnotatedSpotReader goldenTruthReader,
			AnnotatedSpotComparator comparator) {

		this.comparator = comparator;
		sameAnnotatedSpotFilter = new SameAnnotatedSpotFilter(comparator);
		collectors = new LinkedList<MetricValuesCollector<?>>();
		this.predictionsReader = predictionsReader;
		this.goldenTruthReader = goldenTruthReader;
	}

	public void setSameAnnotatedSpotFilter(Filter f) {
		sameAnnotatedSpotFilter = f;
	}

	public void run() {
		List<AnnotatedSpot> predictions = predictionsReader.next();
		while (this.goldenTruthReader.hasNext()) {
			List<AnnotatedSpot> goldenTruth = goldenTruthReader.next();
			logger.info("processing document {} ",
					goldenTruthReader.getCurrentDocId());

			if (predictions == null) {
				logger.warn("no prediction for doc id {} ",
						goldenTruthReader.getCurrentDocId());
				predictions = Collections.emptyList();
			} else if (!predictionsReader.getCurrentDocId().equals(
					goldenTruthReader.getCurrentDocId())) {
				logger.warn(
						"current document in predictions {} does not match current document in golden truth {}. List must be sorted by docid",
						predictionsReader.getCurrentDocId(),
						goldenTruthReader.getCurrentDocId());
			} else {
				eval(predictions, goldenTruth);
				if (predictionsReader.hasNext()) {
					predictions = predictionsReader.next();
				}
			}

		}

		logger.info("final summary:");

		summary();
	}

	public void addMetricValuesCollector(MetricValuesCollector<?> collector) {
		collector.addFilter(sameAnnotatedSpotFilter);
		collectors.add(collector);
	}

	public void eval(List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth) {

		logger.info("filtering goldenTruth");
		goldenTruth = sameAnnotatedSpotFilter.filter(goldenTruth);

		for (MetricValuesCollector<?> collector : collectors) {
			collector.collect(predictions, goldenTruth, comparator);
		}

	}

	public void summary() {
		for (MetricValuesCollector<?> collector : collectors) {
			collector.finalCollect();
		}
	}

}
