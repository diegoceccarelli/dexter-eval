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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 20, 2014
 */
public class ComparatorFactory {

	Map<String, AnnotatedSpotComparator> map = new HashMap<String, AnnotatedSpotComparator>();

	public ComparatorFactory() {
		register(new EntityComparator());
		register(new WeakMentionComparator());
		register(new WeakAnnotationMatchComparator());
	}

	public void register(AnnotatedSpotComparator comparator) {
		map.put(comparator.getName(), comparator);
	}

	public boolean contains(String name) {
		return map.containsKey(name);
	}

	public String getUsage() {
		StringBuffer sb = new StringBuffer();
		for (AnnotatedSpotComparator cmp : map.values()) {
			String value = String.format("%-10s%s", cmp.getName(),
					cmp.getDescription());
			sb.append(value);
			sb.append('\n');
		}
		return sb.toString();
	}

	public AnnotatedSpotComparator getComparator(String name) {
		return map.get(name);
	}

}
