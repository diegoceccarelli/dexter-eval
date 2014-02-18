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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 16, 2014
 */
public abstract class AbstractMetricValueCollector<T> implements
		MetricValuesCollector<T> {

	private int size = 0;
	private String name = this.getClass().toString();
	private final List<Filter> filters = new ArrayList<Filter>();
	private final List<OutputResultsAppender<T>> appenders = new LinkedList<OutputResultsAppender<T>>();

	public int size() {
		return size;
	}

	protected abstract void eval(List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth, AnnotatedSpotComparator comparator);

	public void collect(List<AnnotatedSpot> predictions,
			List<AnnotatedSpot> goldenTruth, AnnotatedSpotComparator comparator) {
		for (Filter f : filters) {
			predictions = f.filter(predictions);
		}
		eval(predictions, goldenTruth, comparator);
		size++;
		for (OutputResultsAppender<T> appender : appenders) {
			if (appender.isAppendPartial()) {
				appender.appendPartial(this);
			}
		}

	}

	public void finalCollect() {
		for (OutputResultsAppender<T> appender : appenders) {

			appender.append(this);

		}
	}

	public MetricValuesCollector<T> addFilter(Filter f) {
		filters.add(f);
		return this;
	}

	public MetricValuesCollector<T> addOutputCollector(
			OutputResultsAppender<T> resultsAppender) {
		appenders.add(resultsAppender);
		return this;
	}

	public abstract T getCollectedTotal();

	public String getName() {
		return name;
	}

	public MetricValuesCollector<T> setName(String name) {
		this.name = name;
		return this;
	}

}
