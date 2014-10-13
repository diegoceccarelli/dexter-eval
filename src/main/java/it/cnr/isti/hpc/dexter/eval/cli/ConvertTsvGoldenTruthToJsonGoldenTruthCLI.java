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
import it.cnr.isti.hpc.dexter.eval.AssessmentRecord;
import it.cnr.isti.hpc.dexter.eval.reader.AnnotatedSpotReader;
import it.cnr.isti.hpc.dexter.eval.reader.TsvAnnotatedSpotReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 17, 2014
 */
public class ConvertTsvGoldenTruthToJsonGoldenTruthCLI extends
		AbstractCommandLineInterface {

	private static final Logger logger = LoggerFactory
			.getLogger(ConvertTsvGoldenTruthToJsonGoldenTruthCLI.class);
	private static String usage = "java -jar $jar  it.cnr.isti.hpc.dexter.eval.cli.EvaluatorCLI -input goldentruth.tsv[.gz] -outut goldentruth.json[.gz] -docs document-folder";
	private static String[] params = new String[] { INPUT, OUTPUT, "docs" };

	public ConvertTsvGoldenTruthToJsonGoldenTruthCLI(String[] args) {
		super(args, params, usage);
	}

	public static void main(String[] args) throws IOException {
		ConvertTsvGoldenTruthToJsonGoldenTruthCLI cli = new ConvertTsvGoldenTruthToJsonGoldenTruthCLI(
				args);
		String input = cli.getInput();

		AnnotatedSpotReader goldenTruthReader = null;

		if (input.endsWith(".tsv") || input.endsWith(".tsv.gz")) {
			goldenTruthReader = new TsvAnnotatedSpotReader(input);
		}
		File docs = new File(cli.getParam("docs"));

		cli.openOutput();
		while (goldenTruthReader.hasNext()) {

			AssessmentRecord ar = new AssessmentRecord();
			ar.setAnnotatedSpot(goldenTruthReader.next());
			ar.setDocId(goldenTruthReader.getCurrentDocId());
			Path path = Paths.get(new File(docs, ar.getDocId())
					.getAbsolutePath() + ".txt");
			byte[] data = Files.readAllBytes(path);
			// String text = new String(data, "UTF-8");

			String str = new String(data, "UTF-8");
			ar.setText(str);
			cli.writeLineInOutput(ar.toJson());
		}
		cli.closeOutput();
	}
}
