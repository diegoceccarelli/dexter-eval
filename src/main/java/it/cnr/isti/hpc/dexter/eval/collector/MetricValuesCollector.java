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
package it.cnr.isti.hpc.dexter.eval.collector;

import it.cnr.isti.hpc.dexter.eval.AnnotatedSpot;
import it.cnr.isti.hpc.dexter.eval.cmp.AnnotatedSpotComparator;
import it.cnr.isti.hpc.dexter.eval.filter.Filter;
import it.cnr.isti.hpc.dexter.eval.output.OutputResultsAppender;

import java.util.List;

/**
 * A metric value collector allows to collect all the partial scores for a given
 * metric of type T. For each prediction the method collect will be call and the
 * list of scores will be updated. Moreover, in order to see the output for a
 * metric collector you will have to connect it with a
 * {@link OutputResultsAppend } that will print the final score for the metric
 * (and each partial, if you want). By default a collect use a
 * {@link ConsoleResultAppander} to print on the standard output.
 * 
 * @param <T>
 *            the type of the metric
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 16, 2014
 * 
 */
public interface MetricValuesCollector<T> {

	/**
	 * returns the number of collect performed over this collector
	 */
	public int size();

	/**
	 * Updates the state of the collector with a new comparison of a assessment
	 * record, using the given comparator that defines when two annotations
	 * match.
	 * 
	 * @param predictions
	 *            the predictions of a method
	 * @param goldenTruth
	 *            the correct annotations
	 * @param comparator
	 *            defines when two annotated spots match
	 */
	public void collect(List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth, AnnotatedSpotComparator comparator);

	/**
	 * Sends the final score to the output collectors
	 */
	public void finalCollect();

	/**
	 * returns the total score of the collects
	 */
	public T getCollectedTotal();

	/**
	 * returns the score in the last collect
	 */
	public T getPartial();

	/**
	 * adds an output collector
	 */
	public MetricValuesCollector<T> addOutputCollector(
			OutputResultsAppender resultsAppender);

	/**
	 * deletes all the output collectors.
	 */
	public void resetOutputCollectors();

	/**
	 * returns the score collected by the collector
	 */
	public double getScore();

	/**
	 * if true, getScore() serve the average score or the total score
	 */
	public boolean averageScore();

	/**
	 * sets average score:
	 * 
	 * @param averageScore
	 *            if true getScore() returns the average score obtained over all
	 *            the collect, otherwise it returns the total score.
	 */
	public void setAverageScore(boolean averageScore);

	/**
	 * the name of the collector, used to print the total score at the end;
	 */
	public String getName();

	/**
	 * sets the name of the collector
	 */
	public MetricValuesCollector<T> setName(String name);

	/**
	 * adds a filter to the collector, the filter is applied to the list of
	 * prediction
	 */
	public MetricValuesCollector<T> addFilter(Filter filter);

}
