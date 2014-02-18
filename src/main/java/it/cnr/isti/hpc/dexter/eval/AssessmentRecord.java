/**


 *  Copyright 2012 Diego Ceccarelli
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
package it.cnr.isti.hpc.dexter.eval;

import it.cnr.isti.hpc.dexter.eval.AnnotatedSpot;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Sep 5, 2012
 */
public class AssessmentRecord {
	public String docId;
	public String text;
	public List<AnnotatedSpot> annotatedSpot;

	public AssessmentRecord() {
		annotatedSpot = new ArrayList<AnnotatedSpot>();

	}

	public void addAnnotatedSpot(AnnotatedSpot as) {
		annotatedSpot.add(as);
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<AnnotatedSpot> getAnnotatedSpot() {
		return annotatedSpot;
	}

	public void setAnnotatedSpot(List<AnnotatedSpot> annotatedSpot) {
		this.annotatedSpot = annotatedSpot;
	}

}
