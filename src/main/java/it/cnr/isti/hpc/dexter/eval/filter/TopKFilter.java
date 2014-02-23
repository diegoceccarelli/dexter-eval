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
package it.cnr.isti.hpc.dexter.eval.filter;

import it.cnr.isti.hpc.dexter.eval.AnnotatedSpot;

import java.util.Collections;
import java.util.List;

/**
 * Returns the top-k annotation with highest confidence score.
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 17, 2014
 */
public class TopKFilter implements Filter {

	private final int topk;

	public TopKFilter(int topk) {
		this.topk = topk;
	}

	public List<AnnotatedSpot> filter(List<AnnotatedSpot> spots) {
		if (spots.size() < topk)
			return spots;
		Collections.sort(spots, new AnnotatedSpot.SortByConfidence());
		return spots.subList(0, topk);
	}

	public String addFilterName(String metricName) {
		return metricName + "@" + topk;
	}
}
