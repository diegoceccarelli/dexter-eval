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
import it.cnr.isti.hpc.dexter.eval.cmp.EntityComparator;
import it.cnr.isti.hpc.dexter.eval.collector.DoubleValuesCollector;
import it.cnr.isti.hpc.dexter.eval.collector.IntValuesCollector;
import it.cnr.isti.hpc.dexter.eval.collector.MicroFMeasureValuesCollector;
import it.cnr.isti.hpc.dexter.eval.collector.MicroPrecisionValuesCollector;
import it.cnr.isti.hpc.dexter.eval.collector.MicroRecallValuesCollector;
import it.cnr.isti.hpc.dexter.eval.filter.ThresholdFilter;
import it.cnr.isti.hpc.dexter.eval.filter.TopKFilter;
import it.cnr.isti.hpc.dexter.eval.metrics.FMeasureMetric;
import it.cnr.isti.hpc.dexter.eval.metrics.FalsePositiveMetric;
import it.cnr.isti.hpc.dexter.eval.metrics.PrecisionMetric;
import it.cnr.isti.hpc.dexter.eval.metrics.RecallMetric;
import it.cnr.isti.hpc.dexter.eval.metrics.TruePositiveMetric;
import it.cnr.isti.hpc.dexter.eval.output.ConsoleDoubleResultsAppender;
import it.cnr.isti.hpc.dexter.eval.output.ConsoleIntResultsAppender;
import it.cnr.isti.hpc.dexter.eval.reader.AnnotatedSpotReader;
import it.cnr.isti.hpc.dexter.eval.reader.JsonAnnotatedSpotReader;
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
		String input = cli.getInput();
		String gt = cli.getParam("gt");
		AnnotatedSpotReader predictionsReader = null;
		AnnotatedSpotReader goldenTruthReader = null;

		if (input.endsWith(".tsv") || input.endsWith(".tsv.gz")) {
			predictionsReader = new TsvAnnotatedSpotReader(input);
		}
		if (input.endsWith(".json") || input.endsWith(".json.gz")) {
			predictionsReader = new JsonAnnotatedSpotReader(input);
		}

		if (gt.endsWith(".tsv") || gt.endsWith(".tsv.gz")) {
			goldenTruthReader = new TsvAnnotatedSpotReader(gt);
		}
		if (gt.endsWith(".json") || gt.endsWith(".json.gz")) {
			goldenTruthReader = new JsonAnnotatedSpotReader(gt);
		}

		ConsoleDoubleResultsAppender console = new ConsoleDoubleResultsAppender();
		ConsoleIntResultsAppender console2 = new ConsoleIntResultsAppender();

		Evaluator evaluator = new Evaluator(predictionsReader,
				goldenTruthReader, new EntityComparator());
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
				.addOutputCollector(console).setName("microP"));
		evaluator.addMetricValuesCollector(new MicroRecallValuesCollector()
				.addOutputCollector(console).setName("microR"));
		evaluator.addMetricValuesCollector(new MicroFMeasureValuesCollector()
				.addOutputCollector(console).setName("microF1"));
		evaluator.addMetricValuesCollector(new MicroPrecisionValuesCollector()
				.addOutputCollector(console).setName("microP(t=0.5)")
				.addFilter(new ThresholdFilter(0.5)));
		evaluator.addMetricValuesCollector(new MicroRecallValuesCollector()
				.addOutputCollector(console).setName("microR(t=0.5)")
				.addFilter(new ThresholdFilter(0.5)));
		evaluator.addMetricValuesCollector(new MicroFMeasureValuesCollector()
				.addOutputCollector(console).setName("microF1(t=0.5)")
				.addFilter(new ThresholdFilter(0.5)));
		evaluator.addMetricValuesCollector(new MicroPrecisionValuesCollector()
				.addOutputCollector(console).setName("microP(t=0.9)")
				.addFilter(new ThresholdFilter(0.9)));
		evaluator.addMetricValuesCollector(new MicroRecallValuesCollector()
				.addOutputCollector(console).setName("microR(t=0.9)")
				.addFilter(new ThresholdFilter(0.9)));
		evaluator.addMetricValuesCollector(new MicroFMeasureValuesCollector()
				.addOutputCollector(console).setName("microF1(t=0.9)")
				.addFilter(new ThresholdFilter(0.9)));

		evaluator.run();
	}
}
