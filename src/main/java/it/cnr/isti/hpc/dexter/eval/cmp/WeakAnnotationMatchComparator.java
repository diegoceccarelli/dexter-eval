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
 * This comparator was proposed in [1]:two AnnotatedSpots are the same if the
 * are annotated with the same entity and window of texts where the two
 * annotation was performed overlap.
 * 
 * [1] A Framework for Benchmarking Entity-Annotation Systems, Marco Cornolti
 * and Paolo Ferragina and Massimiliano Ciaramita, Proceedings of the
 * International World Wide Web Conference (WWW) (Practice & Experience Track)
 * 2013. http://research.google.com/pubs/archive/40749.pdf
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 16, 2014
 */
public class WeakAnnotationMatchComparator implements AnnotatedSpotComparator {

	public boolean match(AnnotatedSpot x, AnnotatedSpot y) {
		if (x.getEntity().equals(y.getEntity()))
			return false;

		if ((x.getStart() >= y.getStart()) && (x.getStart() <= y.getEnd())) {
			return true;
		}
		if ((x.getEnd() >= y.getStart()) && (x.getEnd() <= y.getEnd())) {
			return true;
		}

		if ((x.getStart() <= y.getStart()) && (x.getEnd() >= y.getEnd())) {
			return true;
		}
		return false;

	}

	public String getName() {
		return "Mwa";
	}

	public String getDescription() {
		return "two AnnotatedSpots are the same if they are annotated with the same entity and window of texts where the two annotation was performed overlap";
	}
}
