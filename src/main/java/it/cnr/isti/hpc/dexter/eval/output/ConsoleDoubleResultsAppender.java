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

import it.cnr.isti.hpc.dexter.eval.collector.MetricValuesCollector;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 17, 2014
 */
public class ConsoleDoubleResultsAppender implements
		OutputResultsAppender<Double> {

	boolean partial = false;

	public ConsoleDoubleResultsAppender appendPartial() {
		partial = true;
		return this;
	}

	public void append(MetricValuesCollector<Double> metric) {
		String value = String.format("%-40s%.3f", metric.getName(),
				metric.getScore());
		System.out.println(value);
	}

	public boolean isAppendPartial() {
		return partial;
	}

	public void appendPartial(MetricValuesCollector<Double> metric) {

		String value = String.format("%-40s%.3f", metric.getName(), new Double(
				metric.getPartial()));
		System.out.println(value);

	}
}