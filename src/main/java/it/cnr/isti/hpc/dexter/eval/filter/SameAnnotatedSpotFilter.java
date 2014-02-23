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
import it.cnr.isti.hpc.dexter.eval.cmp.AnnotatedSpotComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Removes occurrences of the same spots (based on the given comparator), if the
 * are more occurrences of the same spot, only the first in the order is kept,
 * hence you can decide which annotation select in case of duplicates changing
 * the order of the list (using the method 'presortBy'). By default, is selected
 * the annotation with the longest spot.
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 17, 2014
 */
public class SameAnnotatedSpotFilter implements Filter {

	private static final Logger logger = LoggerFactory
			.getLogger(SameAnnotatedSpotFilter.class);
	private final AnnotatedSpotComparator comparator;

	private Comparator<AnnotatedSpot> presortBy = new AnnotatedSpot.SortByLength();

	public SameAnnotatedSpotFilter(AnnotatedSpotComparator comparator) {
		this.comparator = comparator;
	}

	public void setPresortBy(Comparator<AnnotatedSpot> cmp) {
		presortBy = cmp;
	}

	public List<AnnotatedSpot> filter(List<AnnotatedSpot> spots) {
		Collections.sort(spots, presortBy);
		List<AnnotatedSpot> filtered = new ArrayList<AnnotatedSpot>();
		for (AnnotatedSpot s : spots) {
			boolean found = false;
			for (AnnotatedSpot f : filtered) {
				if (comparator.match(s, f)) {
					found = true;
					logger.warn("Based on the comparator, an identical spot is yet in the list, I'll ignore it");
					logger.warn("spot in the list (the longer): {}", f);
					logger.warn("ignored spot: {}", s);
				}
			}
			if (!found) {
				filtered.add(s);
			}

		}
		return filtered;
	}

	public String addFilterName(String collectorName) {
		// this filter is always applied, i don't want to
		// reflect that in the name;
		return collectorName;
	}
}
