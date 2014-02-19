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
package it.cnr.isti.hpc.dexter.eval.cmp;

import it.cnr.isti.hpc.dexter.eval.AnnotatedSpot;

/**
 * a AnnotatedSpotComparator object provides a method match. match() returns
 * true if two AnnotatedSpot objects represent a match. Allows to benchmark a
 * entity-linked method using different concepts of 'match', for example you
 * could consider a match if the the annotator returns the correct entities
 * (with no interest for the mentions referred if the position of the match), or
 * you can consider a match only if the entity is the same and the text
 * annotated is exactly the same.
 * 
 * For more information please refer to:
 * 
 * A Framework for Benchmarking Entity-Annotation Systems, Marco Cornolti and
 * Paolo Ferragina and Massimiliano Ciaramita, Proceedings of the International
 * World Wide Web Conference (WWW) (Practice & Experience Track) 2013.
 * http://research.google.com/pubs/archive/40749.pdf
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 16, 2014
 */
public interface AnnotatedSpotComparator {

	/**
	 * 
	 */
	public boolean match(AnnotatedSpot x, AnnotatedSpot y);

}
