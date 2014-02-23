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
package it.cnr.isti.hpc.dexter.eval.cmp;

import it.cnr.isti.hpc.dexter.eval.AnnotatedSpot;

/**
 * With this comparator two AnnotatedSpots are the same if the are annotated
 * with the same entity.
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 16, 2014
 */
public class EntityComparator implements AnnotatedSpotComparator {

	public boolean match(AnnotatedSpot x, AnnotatedSpot y) {
		return x.getEntity() == y.getEntity();
	}

	public String getName() {
		return "Me";
	}

	public String getDescription() {
		return "With this comparator two AnnotatedSpots are the same if the are annotated with the same entity";
	}
}
