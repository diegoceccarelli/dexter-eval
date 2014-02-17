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
import it.cnr.isti.hpc.dexter.eval.Evaluator;
import it.cnr.isti.hpc.dexter.eval.cmp.SameSpotComparator;
import it.cnr.isti.hpc.dexter.eval.collector.DoubleValuesCollector;
import it.cnr.isti.hpc.dexter.eval.collector.IntValuesCollector;
import it.cnr.isti.hpc.dexter.eval.collector.MicroFMeasureValuesCollector;
import it.cnr.isti.hpc.dexter.eval.collector.MicroPrecisionValuesCollector;
import it.cnr.isti.hpc.dexter.eval.collector.MicroRecallValuesCollector;
import it.cnr.isti.hpc.dexter.eval.filter.TopKFilter;
import it.cnr.isti.hpc.dexter.eval.metrics.FMeasureMetric;
import it.cnr.isti.hpc.dexter.eval.metrics.FalsePositiveMetric;
import it.cnr.isti.hpc.dexter.eval.metrics.PrecisionMetric;
import it.cnr.isti.hpc.dexter.eval.metrics.RecallMetric;
import it.cnr.isti.hpc.dexter.eval.metrics.TruePositiveMetric;
import it.cnr.isti.hpc.dexter.eval.output.ConsoleDoubleResultsAppender;
import it.cnr.isti.hpc.dexter.eval.output.ConsoleIntResultsAppender;
import it.cnr.isti.hpc.dexter.eval.reader.AnnotatedSpotReader;
import it.cnr.isti.hpc.dexter.eval.reader.TsvAnnotatedSpotReader;

/**
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Feb 17, 2014
 */
public class EvaluatorCLI extends AbstractCommandLineInterface {

	private static String usage = "java -jar $jar  it.cnr.isti.hpc.dexter.eval.cli.EvaluatorCLI -input predictions.tsv[.gz] -gt goldentruth.tsv[.gz]";
	private static String[] params = new String[] { INPUT, "gt" };

	public EvaluatorCLI(String[] args) {
		super(args, params, usage);
	}

	public static void main(String[] args) {
		EvaluatorCLI cli = new EvaluatorCLI(args);
		AnnotatedSpotReader predictionsReader = new TsvAnnotatedSpotReader(
				cli.getInput());
		AnnotatedSpotReader goldenTruthReader = new TsvAnnotatedSpotReader(
				cli.getParam("gt"));

		ConsoleDoubleResultsAppender console = new ConsoleDoubleResultsAppender()
				.appendPartial();
		ConsoleDoubleResultsAppender consolenp = new ConsoleDoubleResultsAppender();
		ConsoleIntResultsAppender console2 = new ConsoleIntResultsAppender()
				.appendPartial();

		Evaluator evaluator = new Evaluator(predictionsReader,
				goldenTruthReader, new SameSpotComparator());
		evaluator.addMetricValuesCollector(new IntValuesCollector(
				new TruePositiveMetric()).addOutputCollector(console2).setName(
				"tp"));
		evaluator.addMetricValuesCollector(new IntValuesCollector(
				new TruePositiveMetric()).addOutputCollector(console2)
				.setName("tp@1").addFilter(new TopKFilter(1)));
		evaluator.addMetricValuesCollector(new IntValuesCollector(
				new FalsePositiveMetric()).addOutputCollector(console2)
				.setName("fp"));
		evaluator.addMetricValuesCollector(new IntValuesCollector(
				new FalsePositiveMetric()).addOutputCollector(console2)
				.setName("fp@1").addFilter(new TopKFilter(1)));

		evaluator.addMetricValuesCollector(new DoubleValuesCollector(
				new PrecisionMetric()).addOutputCollector(console)
				.setName("p@1").addFilter(new TopKFilter(1)));
		evaluator.addMetricValuesCollector(new DoubleValuesCollector(
				new RecallMetric()).addOutputCollector(console).setName(
				"recall"));
		evaluator.addMetricValuesCollector(new DoubleValuesCollector(
				new PrecisionMetric()).addOutputCollector(console).setName(
				"precision"));
		evaluator.addMetricValuesCollector(new DoubleValuesCollector(
				new FMeasureMetric()).addOutputCollector(console).setName(
				"fmeasure"));
		evaluator.addMetricValuesCollector(new MicroPrecisionValuesCollector()
				.addOutputCollector(consolenp).setName("microP"));
		evaluator.addMetricValuesCollector(new MicroRecallValuesCollector()
				.addOutputCollector(consolenp).setName("microR"));
		evaluator.addMetricValuesCollector(new MicroFMeasureValuesCollector()
				.addOutputCollector(consolenp).setName("microF1"));

		evaluator.run();
	}
}
