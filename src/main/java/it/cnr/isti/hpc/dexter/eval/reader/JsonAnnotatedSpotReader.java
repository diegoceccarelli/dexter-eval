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
package it.cnr.isti.hpc.dexter.eval.reader;

import it.cnr.isti.hpc.dexter.eval.AnnotatedSpot;
import it.cnr.isti.hpc.dexter.eval.AssessmentRecord;
import it.cnr.isti.hpc.io.reader.JsonRecordParser;
import it.cnr.isti.hpc.io.reader.RecordReader;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Reads a list of {@link AssessmentRecord} encoded in json, one per line. For
 * each record returns the annotations.
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 17, 2014
 */
public class JsonAnnotatedSpotReader implements AnnotatedSpotReader {

	private String currentDocId = "NOID";
	private final Iterator<AssessmentRecord> iterator;
	AssessmentRecord rec;

	public JsonAnnotatedSpotReader(String annotatedSpotFile) {
		this(new File(annotatedSpotFile));
	}

	public String getCurrentDocId() {
		return currentDocId;
	}

	public JsonAnnotatedSpotReader(File annotatedSpotFile) {
		RecordReader<AssessmentRecord> reader = new RecordReader<AssessmentRecord>(
				annotatedSpotFile.getAbsolutePath(),
				new JsonRecordParser<AssessmentRecord>(AssessmentRecord.class));
		iterator = reader.iterator();
	}

	public List<AnnotatedSpot> next() {
		rec = iterator.next();
		currentDocId = rec.getDocId();
		return rec.getAnnotatedSpot();
	}

	public boolean hasNext() {
		return iterator.hasNext();
	}

	public void remove() {
		throw new UnsupportedOperationException();

	}

	public boolean hasText() {
		return true;
	}

	public String getText() {
		return rec.getText();
	}

}
