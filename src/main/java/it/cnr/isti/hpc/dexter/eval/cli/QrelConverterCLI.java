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
package it.cnr.isti.hpc.dexter.eval.cli;

import it.cnr.isti.hpc.cli.AbstractCommandLineInterface;
import it.cnr.isti.hpc.dexter.eval.AnnotatedSpot;
import it.cnr.isti.hpc.dexter.eval.cmp.EntityComparator;
import it.cnr.isti.hpc.dexter.eval.filter.SameAnnotatedSpotFilter;
import it.cnr.isti.hpc.dexter.eval.reader.AnnotatedSpotReader;
import it.cnr.isti.hpc.dexter.eval.reader.JsonAnnotatedSpotReader;
import it.cnr.isti.hpc.dexter.eval.reader.TsvAnnotatedSpotReader;

import java.util.List;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 17, 2014
 */
public class QrelConverterCLI extends AbstractCommandLineInterface {

	private static String usage = "java -jar $jar  it.cnr.isti.hpc.dexter.eval.cli.QrelConverterCLI -input golden-truth[.tsv,.json] -output output.qrels";
	private static String[] params = new String[] { INPUT, OUTPUT };

	public QrelConverterCLI(String[] args) {
		super(args, params, usage);
	}

	public static void main(String[] args) {
		QrelConverterCLI cli = new QrelConverterCLI(args);
		String input = cli.getInput();
		AnnotatedSpotReader reader = null;
		if (input.endsWith(".tsv") || input.endsWith(".tsv.gz")) {
			reader = new TsvAnnotatedSpotReader(input);
		}
		if (input.endsWith(".json") || input.endsWith(".json.gz")) {
			reader = new JsonAnnotatedSpotReader(input);
		}
		cli.openOutput();
		EntityComparator comparator = new EntityComparator();
		SameAnnotatedSpotFilter filter = new SameAnnotatedSpotFilter(comparator);
		while (reader.hasNext()) {
			List<AnnotatedSpot> annotations = reader.next();
			annotations = filter.filter(annotations);
			for (AnnotatedSpot annotation : annotations) {

				cli.writeInOutput(annotation.getDocId());
				cli.writeInOutput("\t");
				cli.writeInOutput("0");
				cli.writeInOutput("\t");
				cli.writeInOutput(String.valueOf(annotation.getEntity()));
				cli.writeInOutput("\t");
				cli.writeInOutput("1");
				cli.writeLineInOutput("");
			}
		}
		cli.closeOutput();
	}
}
