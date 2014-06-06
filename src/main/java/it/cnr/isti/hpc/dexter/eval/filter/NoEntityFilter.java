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

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Returns the top-k annotation with highest confidence score.
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 17, 2014
 */
public class NoEntityFilter implements Filter {

	private static final Logger logger = LoggerFactory
			.getLogger(NoEntityFilter.class);

	public List<AnnotatedSpot> filter(List<AnnotatedSpot> spots) {
		List<AnnotatedSpot> filtered = new LinkedList<AnnotatedSpot>();
		for (AnnotatedSpot annotation : spots) {
			try {
				String id = annotation.getEntity();
				int wikiid = Integer.parseInt(id);
				if (wikiid != 0) {
					filtered.add(annotation);
				}
			} catch (Exception e) {
				logger.error("you are not using wikiid, cannot remove entities without id");
				filtered.add(annotation);
			}
		}
		return filtered;
	}

	public String addFilterName(String collectorName) {
		return collectorName + "[-noId]";
	}

}
