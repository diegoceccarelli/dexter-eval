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

import java.util.ArrayList;
import java.util.List;

/**
 * This filter filters out all the annotations where the confidence score is
 * below a certain threshold. Please not that if the confidence score is == the
 * threshold the annotation is kept.
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 17, 2014
 */
public class ThresholdFilter implements Filter {

	private final double threshold;

	public ThresholdFilter(double threshold) {
		this.threshold = threshold;
	}

	public List<AnnotatedSpot> filter(List<AnnotatedSpot> spots) {
		List<AnnotatedSpot> filtered = new ArrayList<AnnotatedSpot>();
		for (AnnotatedSpot s : spots) {
			if (s.getConfidenceScore() >= threshold) {
				filtered.add(s);
			}

		}

		boolean debug = false;
		String dbg = System.getProperty("debug");
		if (dbg != null) {
			debug = new Boolean(dbg);
		}
		if (debug) {
			System.out.println("---------- " + this.getClass()
					+ " ------------");
			System.out.println("filter thres = " + threshold);

			String value = String.format("%-70s%s", "before", "after");
			System.out.println(value);
			for (int i = 0; i < spots.size(); i++) {
				if (i < filtered.size()) {
					value = String.format("%-70s%s", spots.get(i).asString(),
							filtered.get(i).asString());
				} else {
					value = String.format("%-70s%s", spots.get(i).asString(),
							"");
				}
				System.out.println(value);
			}

		}

		return filtered;
	}

	public String addFilterName(String metricName) {
		return metricName + String.format("[conf>= %.3f]", threshold);
	}

}
