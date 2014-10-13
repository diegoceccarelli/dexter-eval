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
import it.cnr.isti.hpc.io.reader.RecordReader;
import it.cnr.isti.hpc.io.reader.TsvRecordParser;
import it.cnr.isti.hpc.io.reader.TsvTuple;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Reads a list of {@link AnnotatedSpot}s encoded in tsv, one per line, sorted
 * by document id. For each docid provides the annotations. The format of a
 * {@link AnnotatedSpot} is: <br>
 * 
 * <pre>
 *   docid \t mention \t start position \t end position \t entity-id (wiki-id) \t confidence score
 * </pre>
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 17, 2014
 */
public class TsvAnnotatedSpotReader implements AnnotatedSpotReader {

	private String currentDocId = "NOID";
	private final Iterator<TsvTuple> iterator;
	private TsvTuple next;

	public TsvAnnotatedSpotReader(String annotatedSpotFile) {
		this(new File(annotatedSpotFile));
	}

	public String getCurrentDocId() {
		return currentDocId;
	}

	public TsvAnnotatedSpotReader(File annotatedSpotFile) {
		RecordReader<TsvTuple> reader = new RecordReader<TsvTuple>(
				annotatedSpotFile.getAbsolutePath(), new TsvRecordParser(
						"docid", "spot", "start", "end", "entity", "wikiname",
						"score"));
		iterator = reader.iterator();
		next = iterator.next();

	}

	public List<AnnotatedSpot> next() {
		if (next != null) {
			currentDocId = next.get("docid");
		}
		List<AnnotatedSpot> spots = new ArrayList<AnnotatedSpot>();
		while ((next != null) && (next.get("docid").equals(currentDocId))) {
			AnnotatedSpot spot = new AnnotatedSpot();
			spot.setSpot(next.get("spot"));
			spot.setEntity(next.get("entity"));
			spot.setWikiname(next.get("wikiname"));
			spot.setStart(next.getInt("start"));
			spot.setEnd(next.getInt("end"));
			spot.setDocId(currentDocId);
			spot.setConfidenceScore(next.getDouble("score").floatValue());
			spots.add(spot);
			next = iterator.next();
		}
		return spots;

	}

	public boolean hasNext() {
		return next != null;
	}

	public void remove() {
		throw new UnsupportedOperationException();

	}

	public boolean hasText() {
		return false;
	}

	public String getText() {
		return "";
	}

}
