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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import it.cnr.isti.hpc.dexter.eval.AnnotatedSpot;

import org.junit.Test;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 16, 2014
 */
public class ComparatorTests {

	@Test
	public void sameSpotComparator() {
		AnnotatedSpot leonardo = new AnnotatedSpot("leonardo da vinci");
		AnnotatedSpot vinci = new AnnotatedSpot("da vinci");
		AnnotatedSpot leonardo2 = new AnnotatedSpot("leonardo da vinci");
		WeakMentionComparator comparator = new WeakMentionComparator();
		assertTrue(comparator.match(leonardo, leonardo2));
		assertTrue(comparator.match(leonardo2, leonardo));
		assertFalse(comparator.match(leonardo2, vinci));
		assertFalse(comparator.match(vinci, leonardo2));
		assertFalse(comparator.match(leonardo, vinci));
		assertFalse(comparator.match(vinci, leonardo));
	}

}
