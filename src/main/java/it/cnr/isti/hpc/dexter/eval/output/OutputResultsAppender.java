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

import java.util.List;

/**
 * Defines an output where to send the results of a collector. You can implement
 * this object to store the results on a file, in the format you want, or in a
 * db etc etc.
 * 
 * See {@link ConsoleResultsAppender} for more details.
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 17, 2014
 */
public interface OutputResultsAppender {

	/**
	 * True if this object receives all the partial updates.
	 */
	public boolean isAppendPartial();

	/**
	 * Receive a partial result
	 */
	public void appendPartial(List<AnnotatedSpot> prediction,
			List<AnnotatedSpot> goldenTruth,
			AnnotatedSpotComparator comparator,
			List<MetricValuesCollector<?>> metrics);

	/**
	 * Receive a final result
	 */
	public void append(MetricValuesCollector<?> metric);

	/**
	 * ask to the the appender to print all the partial
	 */
	public void setPartial(boolean isPartial);

	/**
	 * evaluation is finished
	 */
	public void end();

}
